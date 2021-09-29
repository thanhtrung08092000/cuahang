package com.trung.cuahangonline.adapter;

import android.content.Context;
import android.text.TextUtils;
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

public class DonghocasioAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraydongho;

    public DonghocasioAdapter(Context context, ArrayList<Sanpham> arraydongho) {
        this.context = context;
        this.arraydongho = arraydongho;
    }

    @Override
    public int getCount() {
        return arraydongho.size();
    }

    @Override
    public Object getItem(int i) {
        return arraydongho.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public  class ViewHolder{
        public  TextView txttendonghocasio, txtgiadonghocasio, txtmotadonghocasio;
        public ImageView imgdonghocasio ;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder= null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_donghocasio,null);

            viewHolder.txttendonghocasio= view.findViewById(R.id.textviewdonghocasio);
            viewHolder.txtgiadonghocasio= view.findViewById(R.id.textviewgiasanpham);
            viewHolder.txtmotadonghocasio= view.findViewById(R.id.textviewmotadonghocasio);
            viewHolder.imgdonghocasio= view.findViewById(R.id.imgviewdonghocasio);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham= (Sanpham) getItem(i);
        viewHolder.txttendonghocasio.setText(sanpham.getTensanpham());
        //DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
       // viewHolder.txtgiadonghocasio.setText("Gía :"+decimalFormat.format(sanpham.getGiasanpham()) +"Đ"  );
       viewHolder.txtmotadonghocasio.setMaxLines(2);
        viewHolder.txtmotadonghocasio.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotadonghocasio.setText(sanpham.getMotasanpham());
       Picasso.with(context).load(sanpham.getHinhanhsanpham())
               .into(viewHolder.imgdonghocasio);
        return view;
    }
}
