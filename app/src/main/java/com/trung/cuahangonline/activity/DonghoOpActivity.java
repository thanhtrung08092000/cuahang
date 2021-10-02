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
import android.view.Menu;
import android.view.MenuItem;
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
import com.trung.cuahangonline.adapter.DonghoopAdapter;
import com.trung.cuahangonline.model.Giohang;
import com.trung.cuahangonline.model.Sanpham;
import com.trung.cuahangonline.utils.CheckConnection;
import com.trung.cuahangonline.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonghoOpActivity extends AppCompatActivity {
    Toolbar toolbardhop;
    ListView lvdhop;
    DonghoopAdapter donghoopAdapter;
    ArrayList<Sanpham> mangdhop;
    int iddhop = 0;
    int id ;
    int page = 1;
    View footerView;
    boolean isLoading = false;
    MyHandler myHandler;
    Boolean limitData = false; //xac nhan da het du lieu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongho_op);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menushoppingcart,menu);
        //MenuItem itemSearch = menu.findItem(R.id.mnuSearch);
        //itemSearch.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void Anhxa() {
        toolbardhop = (Toolbar) findViewById(R.id.toolbardonghoop);
        lvdhop= (ListView) findViewById(R.id.listviewdonghoop);
        //Lay du lieu vao mangDienthoai
        Intent intent = getIntent();
        iddhop= intent.getIntExtra("idloaisanpham",-1);
        mangdhop= new ArrayList<>();
        donghoopAdapter = new DonghoopAdapter(getApplicationContext(),mangdhop);
        lvdhop.setAdapter(donghoopAdapter);

        //video 15
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar,null);
        myHandler = new MyHandler();

    }
    private void GetIdLoaisp() {
        iddhop = getIntent().getIntExtra("idloaisanpham",-1);

    }
    private void ActionToolbar() {
        setSupportActionBar(toolbardhop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardhop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.duongdandonghocasio + String.valueOf(Page);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            // JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(duongdan, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(String response) {
                int id= 0 ;
                String Tendhop = "";
                int Giadhop= 0;
                String Hinhanhdhop="";
                String Mota = "";
                int Idspdhop= 0;
                if(response != null && response.length() !=2){
                   // lvdhop.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);


                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendhop = jsonObject.getString("tensp");
                            Giadhop = jsonObject.getInt("giasp");
                            Hinhanhdhop = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspdhop = jsonObject.getInt("idsanpham");
                            mangdhop.add(new Sanpham(id, Tendhop, Giadhop, Hinhanhdhop, Mota, Idspdhop));
                            donghoopAdapter.notifyDataSetChanged();
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    limitData = true;
                    lvdhop.removeFooterView(footerView);
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
                param.put("idsanpham",String.valueOf(iddhop));
                return param;
                //   return super.getParams();
            }
        };
        //requestQueue.add(jsonArrayRequest);
        requestQueue.add(stringRequest);
    }
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvdhop.addFooterView(footerView);
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
    private void LoadMoreData() {

        lvdhop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chitietsanpham.class);
                intent.putExtra("thongtinsanpham",mangdhop.get(i));
                startActivity(intent);
            }
        });

        lvdhop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem,  int visibleItem, int totalItem) {
                if(firstItem + visibleItem == totalItem && totalItem!=0 && isLoading==false && limitData==false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }
}