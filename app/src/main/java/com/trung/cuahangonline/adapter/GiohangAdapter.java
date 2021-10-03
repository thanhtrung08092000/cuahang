package com.trung.cuahangonline.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.trung.cuahangonline.R;
import com.trung.cuahangonline.activity.MainActivity;
import com.trung.cuahangonline.model.Giohang;

import java.util.ArrayList;

public class GiohangAdapter extends BaseAdapter {
    Context context;
    ArrayList<Giohang> arraygiohang;


    public GiohangAdapter(Context context, ArrayList<Giohang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int i) {
        return arraygiohang.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public ImageView imgGioHang;
        public TextView txttengiohang,txtgiagiohang;
        public Button btnminus,btnvalues,btnplus;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(view == null){
            viewHolder = new ViewHolder();
               // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.from(context).inflate(R.layout.dong_giohang,null);
            viewHolder.txttengiohang = view.findViewById(R.id.textviewtengiohang);
            viewHolder.txtgiagiohang = view.findViewById(R.id.textviewgiagiohang);
            viewHolder.imgGioHang = view.findViewById(R.id.imageviewgiohang);
            viewHolder.btnminus = view.findViewById(R.id.buttonminus);
            viewHolder.btnvalues = view.findViewById(R.id.buttonvalues);
            viewHolder.btnplus = view.findViewById(R.id.butonplus);
            view.setTag(viewHolder);


        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Giohang gioHang = (Giohang) getItem(i);
        viewHolder.txttengiohang.setText(gioHang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiagiohang.setText(decimalFormat.format(gioHang.getGiasp())+" Đ");
//        Picasso.with(context).load(gioHang.getHinhSP())
//                                .placeholder(R.drawable.noimage)
//                                .error(R.drawable.error)
//                                .into(viewHolder.imgGioHang);
        Glide.with(context).load(gioHang.getHinhsp())
                .into(viewHolder.imgGioHang);
        viewHolder.btnvalues.setText(gioHang.getSoluongsp()+"");
        final int sl = Integer.parseInt(viewHolder.btnvalues.getText().toString());
        if (sl>=10){
            viewHolder.btnminus.setVisibility(View.VISIBLE);
            viewHolder.btnplus.setVisibility(View.INVISIBLE);
        }else if (sl<=1){
            viewHolder.btnminus.setVisibility(View.INVISIBLE);
            viewHolder.btnplus.setVisibility(View.VISIBLE);
        }else if (sl>=1){
            viewHolder.btnminus.setVisibility(View.VISIBLE);
            viewHolder.btnplus.setVisibility(View.VISIBLE);
        }
        ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) +1;
                int slhientai = MainActivity.manggiohang.get(i).getSoluongsp();
                long giahientai = MainActivity.manggiohang.get(i).getGiasp();
                MainActivity.manggiohang.get(i).setSoluongsp(slmoinhat);
                long giamoinhat = (giahientai * slmoinhat)/slhientai;
                MainActivity.manggiohang.get(i).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.txtgiagiohang.setText(decimalFormat.format(giamoinhat) +" Đ");
                com.trung.cuahangonline.activity.Giohang.EventUntil();
                if (slmoinhat > 9){
                    finalViewHolder.btnplus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
                }else {
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
                }
            }
        });


        viewHolder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) - 1;
                int slhientai = MainActivity.manggiohang.get(i).getSoluongsp();
                long giahientai = MainActivity.manggiohang.get(i).getGiasp();
                MainActivity.manggiohang.get(i).setSoluongsp(slmoinhat);
                long giamoinhat = (giahientai * slmoinhat)/slhientai;
                MainActivity.manggiohang.get(i).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.txtgiagiohang.setText(decimalFormat.format(giamoinhat) +" Đ");
                com.trung.cuahangonline.activity.Giohang.EventUntil();
                if (slmoinhat < 2){
                    finalViewHolder.btnminus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
                }else {
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
                }
            }
        });
        return view;
    }
}
