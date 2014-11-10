package com.example.badjoras.smarthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badjoras.control.Product;

import java.util.LinkedList;


public class ListViewAdapter extends BaseAdapter {

//    public static final String FIRST_COLUMN = "First";
//    public static final String SECOND_COLUMN = "Second";
//    public static final String THIRD_COLUMN = "Third";
//    public static final String FOURTH_COLUMN = "Fourth";

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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = frag.getLayoutInflater(b);

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
                // TODO Auto-generated method stub
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) v.getTag());
            }
        });

//        holder.btn_delete_row.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
////                final int x = (int) getItemId(position);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder();
//
//                // set title
//                alertDialogBuilder.setTitle("Your Title");
//
//                // set dialog message
//                alertDialogBuilder
//                        .setMessage("Click yes to exit!")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int id) {
//                                Toast.makeText(context, "Yes clicked", Toast.LENGTH_LONG).show();
//
//
//                            } }).
//
//                            setNegativeButton("No",new DialogInterface.OnClickListener() {
//                                @SuppressLint("NewApi")
//                                public void onClick (DialogInterface dialog,int id){
//
//                                    dialog.cancel();
//
//                                }
//                            }
//
//                            );
//
//                            // create alert dialog
//                            AlertDialog alertDialog = alertDialogBuilder.create();
//                            // show it
//                            alertDialog.show();
//                        }
//            }
//
//            );


//        holder.btn_delete_row.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
////                final int position = list_view.;
//
//                v.
//                Toast.makeText(context, "Remoção de item!", Toast.LENGTH_LONG).show();
//
////                Dialog dialog = removeProductDialog(v);
////                dialog.show();
//
////                View layout = inflater.inflate(R.layout.popup_windows_remove_confirmation,
////                        (ViewGroup) findViewById(R.id.popup_windows_confirm_remove));
////
////                pwindo = new PopupWindowsProgRemove(layout);
////                pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
////
////                viewHolder.btnConfirm = (Button) layout.findViewById(R.id.popup_windows_btn_confirm);
////                viewHolder.btnCancel = (Button) layout.findViewById(R.id.popup_windows_btn_cancel);
////                viewHolder.progToRemove = (TextView) layout.findViewById(R.id.popup_windows_remove_prog);
////
////                viewHolder.progToRemove.setText(Window.schedule.get(pos).getString());
////
////                viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////                    public void onClick(View v) {
////                        Window.schedule.remove(pos);
////                        finish();
////                        startActivity(getIntent());
////                    }
////                });
////
////                viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////                    public void onClick(View v) {
////                        pwindo.dismiss();
////
////                    }
////                });
//
//            }
//        });

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

//    private void setClickListeners(View view) {
//        view.setOnClickListener(clickListener);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        int position = (Integer) v.getTag(R.id.key_position);
//        System.out.println("Postion" + position);
//
//        if (v.getId() == R.id.callButton) {
//            System.out.println("Call Button");
//
//        } else if (v.getId() == R.id.msgButton) {
//            System.out.println("Message Button");
//
//        }
//    }


}