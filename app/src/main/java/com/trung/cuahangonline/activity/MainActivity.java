package com.trung.cuahangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.trung.cuahangonline.R;
import com.trung.cuahangonline.adapter.LoaispAdapter;
import com.trung.cuahangonline.model.LoaiSP;
import com.trung.cuahangonline.utils.CheckConnection;
import com.trung.cuahangonline.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    NavigationView navigationView;
    ListView listViewmanhinhchinh;
    DrawerLayout drawerLayout;
    RecyclerView recyclerViewmanhinhchinh;
    ArrayList<LoaiSP> mangloaisp;
    LoaispAdapter loaispAdapter;
    int  id= 0;
    String tenloaisp="";
    String hinhanhloaisp="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            //ham bat su kien cho toolbar
            ActionBar();
            //ham bat su kien cho ViewFlipper chay quang cao
            ActionViewFlipper();
            getdulieuloaisp();
            getdulieusanphammoinhat();
            CatchOnItemListView();
        }
        else{
            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }



    private void CatchOnItemListView() {

    }

    private void getdulieusanphammoinhat() {

    }
    private void getdulieuloaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongdanloaisp, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if(response!=null)//neu du lieu json ton tai
                {
                    for(int i=0;i<response.length();i++)  {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            tenloaisp=jsonObject.getString("tenloaisp");
                            hinhanhloaisp=jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new LoaiSP(id,tenloaisp,hinhanhloaisp));
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(3,new LoaiSP(0,"Liên hệ","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRQBRW8EDnycdskgONzvnmOiEqjTRxaygjrXpy9O6SCGe2jPfCm&usqp=CAU"));
                    mangloaisp.add(4,new LoaiSP(0,"Thông tin","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ0La2HZleKC9KaMgJatGhQgxkWyIoK4IQnyxlz0z85s4egHsdf&usqp=CAU"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        requestQueue.add(jsonArrayRequest);
    }
    private void ActionViewFlipper(){
        ArrayList<String> mangquangcao=new ArrayList<>();//khai bao va cap phat bo nho
        mangquangcao.add("https://cdn.tgdd.vn/2021/09/banner/dhtt-800-200-800x200-1.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/09/banner/800-200-800x200-86.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/09/banner/800-200-800x200-6.png");
         mangquangcao.add("https://cdn.tgdd.vn/2021/09/banner/800-200-800x200-87.png");
        for(int i=0;i<mangquangcao.size();i++)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            //Load ảnh từ url trong mangquangcao về imageview
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            //chỉnh kích thước để imageview vừa đủ với ViewFlipper
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        //bắt sự kiện cho viewflipper chạy.
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setAnimation(animation_slide_in);
        viewFlipper.setAnimation(animation_slide_out);
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void init()
    {   toolbar=(Toolbar)findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper=(ViewFlipper) findViewById(R.id.viewfliper);
        recyclerViewmanhinhchinh=(RecyclerView)findViewById(R.id.recyclerview);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        listViewmanhinhchinh=(ListView)findViewById(R.id.listviewmanhinhchinh);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        mangloaisp=new ArrayList<>();
        mangloaisp.add(0,new LoaiSP(0,"Trang Chính","https://imageog.flaticon.com/icons/png/512/25/25694.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF"));
        loaispAdapter=new LoaispAdapter(mangloaisp,getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}