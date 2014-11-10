package com.example.badjoras.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.badjoras.control.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class ListViewAdapter extends BaseAdapter {

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
//    public static final String THIRD_COLUMN = "Third";
//    public static final String FOURTH_COLUMN = "Fourth";

    public LinkedList<Product> list;
    Fragment frag;
    Bundle b;
    Context context;

    public ListViewAdapter(Fragment frag, LinkedList<Product> list, Bundle b, Context context) {
        super();
        this.frag = frag;
        this.list = list;
        this.b = b;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView prodName;
        TextView prodQuantity;
//        TextView txtThird;
//        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = frag.getLayoutInflater(b);

        System.out.println("GET VIEW DO LIST_ADAPTER!!!!");

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.colmn_row, null);
            holder = new ViewHolder();

            holder.prodName = (TextView) convertView.findViewById(R.id.TextFirst);
            holder.prodQuantity = (TextView) convertView.findViewById(R.id.TextSecond);
//            holder.txtThird = (TextView) convertView.findViewById(R.id.TextThird);
//            holder.txtFourth = (TextView) convertView.findViewById(R.id.TextFourth);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Product prod = list.get(position);
        holder.prodName.setText(prod.getName());
        holder.prodQuantity.setText(String.valueOf(prod.getQuantity()));
//        holder.txtThird.setText(map.get(THIRD_COLUMN));
//        holder.txtFourth.setText(map.get(FOURTH_COLUMN));

        if (position % 2 == 0)
            convertView.setBackgroundColor(context.getResources().getColor(R.color.even_row));
        else
            convertView.setBackgroundColor(context.getResources().getColor(R.color.odd_row));

        return convertView;
    }

    public void swapItems(LinkedList<Product> items) {
        this.list = items;
        notifyDataSetChanged();
    }

}
