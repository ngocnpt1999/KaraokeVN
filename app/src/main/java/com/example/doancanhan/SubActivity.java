package com.example.doancanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    TextView txtmaso,txtbaihat,txtloibaihat,txttacgia;
    ImageButton btnthich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtmaso = findViewById(R.id.txtMaSo);
        txtbaihat = findViewById(R.id.txtTen);
        txtloibaihat = findViewById(R.id.txtLoi);
        txttacgia = findViewById(R.id.txtTG);
        btnthich = findViewById(R.id.btnLove);
        Intent callerIntent1 = getIntent();
        Bundle backagecaller1 = callerIntent1.getBundleExtra("package");
        String maso = backagecaller1.getString("maso");
        Cursor c = MainActivity.database.rawQuery("SELECT * FROM ArirangSongList WHERE MABH LIKE'"+maso+"'", null);
        txtmaso.setText(maso);
        c.moveToFirst();
        txtbaihat.setText(c.getString(2));
        txtloibaihat.setText(c.getString(3));
        txttacgia.setText(c.getString(4));
        if (c.getInt(6) == 0)
        {
            btnthich.setImageResource(R.drawable.unlike);
            btnthich.setTag(0);
        }
        else {
            btnthich.setImageResource(R.drawable.love);
            btnthich.setTag(1);
        }
        c.close();
        btnthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)btnthich.getTag() == 1)
                {
                    ContentValues values = new ContentValues();
                    values.put("YEUTHICH", 0);
                    MainActivity.database.update("ArirangSongList", values, "MABH=?", new String[]{txtmaso.getText().toString()});
                    btnthich.setImageResource(R.drawable.unlike);
                    btnthich.setTag(0);
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put("YEUTHICH", 1);
                    MainActivity.database.update("ArirangSongList", values, "MABH=?", new String[]{txtmaso.getText().toString()});
                    btnthich.setImageResource(R.drawable.love);
                    btnthich.setTag(1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}