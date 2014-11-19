package com.example.badjoras.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.badjoras.control.Product;

import java.util.LinkedList;


public class ListViewAdapter extends BaseAdapter {

    public LinkedList<Product> list;
    public Fragment frag;
    public Bundle b;
    public Context context;

    private BtnClickListener mClickListener = null;

    public ListViewAdapter(Fragment frag, LinkedList<Product> list, Bundle b,
                           Context context, BtnClickListener clickListener) {
        super();
        this.frag = frag;
        this.list = list;
        this.b = b;
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        System.out.println("Tou dentro desta classe de merda:" +position);

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView prodName;
        TextView prodQuantity;
        Button btn_delete_row;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = frag.getLayoutInflater(b);

        System.out.println("TODO LA DENTRO " + position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.colmn_row, parent, false);
            holder = new ViewHolder();

            holder.prodName = (TextView) convertView.findViewById(R.id.TextFirst);
            holder.prodQuantity = (TextView) convertView.findViewById(R.id.TextSecond);
            holder.btn_delete_row = (Button) convertView.findViewById(R.id.list_row_btn_delete);
            holder.btn_delete_row.setTag(position);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product prod = list.get(position);
        holder.prodName.setText(prod.getName());
        holder.prodQuantity.setText(String.valueOf(prod.getQuantity()));

        holder.btn_delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    {
                    System.out.println("O GUI E PANAO: " + position);
                    mClickListener.onBtnClick((Integer) v.getTag());}
            }
        });

        if (prod.getQuantity() == 0)

        {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.stock_low));
        } else

        {
            if (position % 2 == 0)
                convertView.setBackgroundColor(context.getResources().getColor(R.color.even_row));
            else
                convertView.setBackgroundColor(context.getResources().getColor(R.color.odd_row));
        }

        return convertView;
    }

    public void swapItems(LinkedList<Product> items) {
        this.list = items;
        notifyDataSetChanged();
    }

}