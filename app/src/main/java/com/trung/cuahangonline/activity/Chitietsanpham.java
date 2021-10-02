package com.trung.cuahangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trung.cuahangonline.R;
import com.trung.cuahangonline.model.Giohang;
import com.trung.cuahangonline.model.Sanpham;

import java.text.DecimalFormat;


public class Chitietsanpham extends AppCompatActivity {
    Toolbar toolbarchitiet;
    ImageView imgChiTietSanPham;
    TextView txtTenSP, txtGiaSP, txtMoTaSP;
    Spinner spinner;
    Button btnDatMua;
    int id = 0;
    String Tenchitiet = "";
    int Giachitiet = 0;
    String Hinhanhchitiet = "";
    String Motachitiet = "";
    int Idsanpham = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        Anhxa();
        ActionToolBar();
        getdatafromrecyclerview();
        CatchEventSpinner();
        EventButton();

    }

    private void EventButton() {
        btnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.manggiohang.size()>0){
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;

                    for (int i=0; i< MainActivity.manggiohang.size(); i++) {
                        if (MainActivity.manggiohang.get(i).getIdsp() == id) {
                            MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp() + sl);
                            if (MainActivity.manggiohang.get(i).getSoluongsp() > 10) {
                                MainActivity.manggiohang.get(i).setSoluongsp(10);

                            }
                            MainActivity.manggiohang.get(i).setGiasp(MainActivity.manggiohang.get(i).getSoluongsp()*Giachitiet);
                            exists = true;
                        }
                    }
                    if (exists==false) {
                        int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soLuong * Giachitiet;
                        MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soLuong));

                    }
                    }else {
                    int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soLuong * Giachitiet;
                    MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soLuong));
                }
                Intent intent = new Intent(getApplicationContext(), com.trung.cuahangonline.activity.Giohang.class);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        toolbarchitiet=(Toolbar)findViewById(R.id.toolbarchitietsanpham);
        imgChiTietSanPham=(ImageView)findViewById(R.id.imagechotietsanpham);
        txtTenSP=(TextView)findViewById(R.id.textviewtenchitietsanpham);
        txtGiaSP=(TextView)findViewById(R.id.textviewgiachitietsanpham);
        txtMoTaSP=(TextView)findViewById(R.id.textviewmotachitietsanpham);
        spinner=(Spinner)findViewById(R.id.spinner);
        btnDatMua=(Button)findViewById(R.id.butondatmua);
    }


    public void getdatafromrecyclerview(){

        Intent intent=getIntent();
        if(intent.getExtras()!=null){
            Sanpham sanPham=(Sanpham) intent.getSerializableExtra("thongtinsanpham");
            id=sanPham.getID();
            Tenchitiet=sanPham.getTensanpham();
            Giachitiet=sanPham.getGiasanpham();
            Hinhanhchitiet=sanPham.getHinhanhsanpham();
            Motachitiet=sanPham.getMotasanpham();
            Idsanpham = sanPham.getIDsanpham();
            txtTenSP.setText(Tenchitiet);
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtGiaSP.setText("Giá: "+decimalFormat.format(Giachitiet)+" Đ");
            txtMoTaSP.setText(Motachitiet);
            Glide.with(getApplicationContext()).load(Hinhanhchitiet)
                    .into(imgChiTietSanPham);
        }
    }
    private void CatchEventSpinner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong);
        spinner.setAdapter(arrayAdapter);
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

            }