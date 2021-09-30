package com.trung.cuahangonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trung.cuahangonline.R;
import com.trung.cuahangonline.model.Sanpham;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DonghoopAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraydonghoop;



    public DonghoopAdapter(Context context, ArrayList<Sanpham> arraydonghoop) {
        this.context = context;
        this.arraydonghoop = arraydonghoop;
    }

    @Override
    public int getCount() {
        return arraydonghoop.size();
    }

    @Override
    public Object getItem(int i) {
        return arraydonghoop.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public  class ViewHolder{
        public TextView txttendonghoop, txtgiadonghoop, txtmotadonghoop;
        public ImageView imgdonghoop ;

    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(view == null){
           viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_donghoop,parent,false);

            viewHolder.txttendonghoop= view.findViewById(R.id.textviewdonghoop);
            viewHolder.txtgiadonghoop= view.findViewById(R.id.textviewgiadonghoop);
            viewHolder.txtmotadonghoop= view.findViewById(R.id.textviewmotadonghoop);
            viewHolder.imgdonghoop= view.findViewById(R.id.imgviewdonghoop);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Log.d("adapter", "getView: "+arraydonghoop.size());
        Sanpham sanpham= (Sanpham) getItem(i);
        viewHolder.txttendonghoop.setText(sanpham.getTensanpham());


        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
         viewHolder.txtgiadonghoop.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham())+ "Đ");

        //viewHolder.txtgiadonghocasio.setText("Giá : " + (sanpham.getGiasanpham())+ "Đ");
        viewHolder.txtmotadonghoop.setMaxLines(2);
        viewHolder.txtmotadonghoop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotadonghoop.setText(sanpham.getMotasanpham());
//ban dung co sai picaso no cu roi
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .into(viewHolder.imgdonghoop);
        return view;
    }

}
