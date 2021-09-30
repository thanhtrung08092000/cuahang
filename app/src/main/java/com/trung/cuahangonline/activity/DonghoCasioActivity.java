package com.trung.cuahangonline.activity;



import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trung.cuahangonline.R;
import com.trung.cuahangonline.adapter.DonghocasioAdapter;
import com.trung.cuahangonline.model.Sanpham;
import com.trung.cuahangonline.utils.CheckConnection;
import com.trung.cuahangonline.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonghoCasioActivity extends AppCompatActivity {
    Toolbar toolbardhcasio;
    ListView lvdhcasio;
    DonghocasioAdapter donghocasioAdapter;
    ArrayList<Sanpham> mangdhcasio,results;
    int iddhcasio = 0;
    int id ;
    int page = 1;
    View footerView;
    boolean isLoading = false;
    MyHandler myHandler;
    Boolean limitData = false; //xac nhan da het du lieu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongho_casio);
        Anhxa();

        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdLoaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }
        else{
            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
        
    }

    private void LoadMoreData() {

        lvdhcasio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chitietsanpham.class);
                intent.putExtra("thongtinsanpham",mangdhcasio.get(i));
                startActivity(intent);
            }
        });

        lvdhcasio.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem,  int visibleItem, int totalItem) {
                if(firstItem + visibleItem == totalItem && totalItem!=0 && isLoading==false && limitData==false){
                    isLoading = true;
                    ThreadData threadData= new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.duongdandonghocasio + String.valueOf(Page);
        //String duongdan = "http://192.168.1.5/server/getsanpham.php?page=" + page;
        StringRequest stringRequest= new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
       // JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(duongdan, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(String response) {
                    int id= 0 ;
                    String Tendhcasio = "";
                    int Giadhcasio= 0;
                    String Hinhanhdhcasio="";
                    String Mota = "";
                    int Idspdhcasio= 0;
                    if(response != null && response.length() !=2){
                        lvdhcasio.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);


                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendhcasio = jsonObject.getString("tensp");
                            Giadhcasio = jsonObject.getInt("giasp");
                            Hinhanhdhcasio = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspdhcasio = jsonObject.getInt("idsanpham");
                            mangdhcasio.add(new Sanpham(id, Tendhcasio, Giadhcasio, Hinhanhdhcasio, Mota, Idspdhcasio));
                            donghocasioAdapter.notifyDataSetChanged();
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                        limitData = true;
                        lvdhcasio.removeFooterView(footerView);
                        CheckConnection.showToast_short(getApplicationContext(),"Đã hết dữ liệu ");
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("idsanpham",String.valueOf(iddhcasio));
                return param;
                //   return super.getParams();
            }
        };
        //requestQueue.add(jsonArrayRequest);
       requestQueue.add(stringRequest);
    }


    private void ActionToolbar() {
        setSupportActionBar(toolbardhcasio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardhcasio.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdLoaisp() {
        iddhcasio = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",iddhcasio+"");
    }

    private void Anhxa() {
        toolbardhcasio = (Toolbar) findViewById(R.id.toolbardonghocasio);
        lvdhcasio= (ListView) findViewById(R.id.listviewdonghocasio);
        //Lay du lieu vao mangDienthoai
        Intent intent = getIntent();
        iddhcasio = intent.getIntExtra("idloaisanpham",-1);
        mangdhcasio = new ArrayList<>();
        donghocasioAdapter = new DonghocasioAdapter(getApplicationContext(),mangdhcasio);
        lvdhcasio.setAdapter(donghocasioAdapter);

        //video 15
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar,null);
        myHandler = new MyHandler();
    }
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvdhcasio.addFooterView(footerView);
                    break;
                case 1:
                    GetData(++page); //tang bien page len 1 roi moi thuc hien function
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
            super.run();
        }
    }
}