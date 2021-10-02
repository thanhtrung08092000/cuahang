package com.trung.cuahangonline.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.trung.cuahangonline.R;
import com.trung.cuahangonline.adapter.GiohangAdapter;

public class Giohang extends AppCompatActivity {
    ListView lvgiohang;
    TextView txtthongbao;
    static TextView txttongtien;
    Button btnthanhtoan, btntieptucmuahang;
    Toolbar toolbargiohang;
    GiohangAdapter giohangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        Anhxa();
        actionToolbar();
        CheckData();
    }

    private void CheckData() {
        if (MainActivity.manggiohang.size()<=0){
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.VISIBLE);
            lvgiohang.setVisibility(View.INVISIBLE);
        }else {
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.INVISIBLE);
            lvgiohang.setVisibility(View.VISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void EventUtil() {
        long tongtien = 0;
        for (int i=0;i<MainActivity.manggiohang.size();i++){
            tongtien+=MainActivity.manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtien)+" Đ");
    }

    private void actionToolbar() {
        setSupportActionBar(toolbargiohang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbargiohang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        lvgiohang = findViewById(R.id.listviewgiohang);
        txtthongbao = findViewById(R.id.textviewthongbao);
        txttongtien = findViewById(R.id.txtGiaGioHang);
        btnthanhtoan = findViewById(R.id.buttonthanhtoangiohang);
        btntieptucmuahang = findViewById(R.id.buttontieptucmuahang);
        toolbargiohang = findViewById(R.id.toolbargiohang);
        giohangAdapter = new GiohangAdapter(Giohang.this,MainActivity.manggiohang);
        lvgiohang.setAdapter(giohangAdapter);

    }
}