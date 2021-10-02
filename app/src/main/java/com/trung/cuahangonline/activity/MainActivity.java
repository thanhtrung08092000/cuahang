package com.trung.cuahangonline.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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
import com.trung.cuahangonline.adapter.SanphamAdapter;
import com.trung.cuahangonline.model.Giohang;
import com.trung.cuahangonline.model.LoaiSP;
import com.trung.cuahangonline.model.Sanpham;
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

    ArrayList<Sanpham> mangsanpham;
    SanphamAdapter sanphamAdapter;

    public static ArrayList<Giohang> manggiohang;
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
            CatchOnitemListView();


        }
        else{
            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        //MenuItem itemSearch = menu.findItem(R.id.mnuSearch);
        //itemSearch.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.trung.cuahangonline.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void CatchOnitemListView() {
        listViewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối ");

                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DonghoCasioActivity.class);
                            intent.putExtra("idloaisanpham", mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối ");

                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2 :
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DonghoOpActivity.class);
                            intent.putExtra("idloaisanpham", mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối ");

                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,LienheActivity.class);
                            intent.putExtra("idloaisanpham", mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối ");

                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,ThongtinActivity.class);
                            intent.putExtra("idloaisanpham", mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.showToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối ");

                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }
            }
        });
    }

    private void getdulieusanphammoinhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Server.duongdansanphammoinhat, new Response.Listener<JSONArray>() {

           @Override
           public void onResponse(JSONArray response) {
               if(response!=null)//neu du lieu json ton tai
               {   int ID =  0;
                   String Tensanpham="";
                   Integer Giasanpham =0;
                   String Hinhanhsanpham="";
                   String Motasanpham="";
                   int IDsanpham= 0;
                   for(int i =0; i<response.length();i++){
                       try {
                           JSONObject jsonObject= response.getJSONObject(i);
                           ID = jsonObject.getInt("id");
                           Tensanpham= jsonObject.getString("tensp");
                           Giasanpham=jsonObject.getInt("giasp");
                           Hinhanhsanpham=jsonObject.getString("hinhanhsp");
                           Motasanpham=jsonObject.getString("motasp");
                           IDsanpham=jsonObject.getInt("idsanpham");
                           mangsanpham.add(new Sanpham(ID,Tensanpham,Giasanpham,Hinhanhsanpham,Motasanpham,IDsanpham));
                           sanphamAdapter.notifyDataSetChanged();

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
        requestQueue.add(jsonArrayRequest);
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
                            Log.d("hieu", tenloaisp+ " ....................");
                            hinhanhloaisp=jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new LoaiSP(id,tenloaisp,hinhanhloaisp));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(new LoaiSP(0,"Liên hệ","https://cdn-icons-png.flaticon.com/512/3300/3300409.png"));
                    mangloaisp.add(new LoaiSP(0,"Thông tin","https://cdn-icons-png.flaticon.com/512/3306/3306613.png"));



                    loaispAdapter.notifyDataSetChanged();
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
        mangloaisp=new ArrayList<>();   // hieu roi hai cai trung nhau
        mangloaisp.add(0,new LoaiSP(0,"Trang Chính","https://cdn-icons-png.flaticon.com/512/1147/1147086.png"));
        loaispAdapter=new LoaispAdapter(mangloaisp,getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);
        mangsanpham= new ArrayList<>();
        sanphamAdapter= new SanphamAdapter(getApplicationContext(),mangsanpham);


        recyclerViewmanhinhchinh.setHasFixedSize(true);
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerViewmanhinhchinh.setAdapter(sanphamAdapter);
        //Cap phat vung nho cho mang GioHang
        if (manggiohang == null){
            manggiohang = new ArrayList<>();
        }else {

        }

    }



}