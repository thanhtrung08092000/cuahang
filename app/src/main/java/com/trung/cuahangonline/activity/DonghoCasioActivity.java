package com.trung.cuahangonline.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.android.volley.Request.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongho_casio);
        Anhxa();

        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdLoaisp();
            ActionToolbar();
            GetData(page);
        }
        else{
            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
        
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
    }
}