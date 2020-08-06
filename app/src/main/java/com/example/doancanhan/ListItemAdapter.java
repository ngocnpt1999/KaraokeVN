package com.example.doancanhan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListItemAdapter extends ArrayAdapter<Item> {

    Activity context = null;
    ArrayList<Item> myArray = null;
    int LayoutId;

    public ListItemAdapter(Activity context, int LayoutId, ArrayList<Item> arr) {
        super(context, LayoutId, arr);
        this.context = context;
        this.LayoutId = LayoutId;
        this.myArray = arr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);
        final Item myItem = myArray.get(position);
        final TextView maso = convertView.findViewById(R.id.txtMa);
        maso.setText(myItem.getMaso());
        final TextView tieude = convertView.findViewById(R.id.txtTenBH);
        tieude.setText(myItem.getTieude());
        final ImageButton btnlike = convertView.findViewById(R.id.btnLike);
        int thich = myItem.getThich();
        if (thich == 0)
        {
            btnlike.setImageResource(R.drawable.unlike);
            btnlike.setTag(0);
        }
        else {
            btnlike.setImageResource(R.drawable.love);
            btnlike.setTag(1);
        }
        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)btnlike.getTag() == 1)
                {
                    ContentValues values = new ContentValues();
                    values.put("YEUTHICH", 0);
                    MainActivity.database.update("ArirangSongList", values, "MABH=?", new String[]{maso.getText().toString()});
                    btnlike.setImageResource(R.drawable.unlike);
                    btnlike.setTag(0);
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put("YEUTHICH", 1);
                    MainActivity.database.update("ArirangSongList", values, "MABH=?", new String[]{maso.getText().toString()});
                    btnlike.setImageResource(R.drawable.love);
                    btnlike.setTag(1);
                }
            }
        });
        tieude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tieude.setTextColor(Color.RED);
                maso.setTextColor(Color.RED);
                Intent intent1 = new Intent(context, SubActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("maso", maso.getText().toString());
                intent1.putExtra("package", bundle1);
                context.startActivity(intent1);
            }
        });
        return convertView;
    }
}