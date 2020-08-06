package com.example.doancanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    public static String DATABASE_NAME="arirang.sqlite";
    TabHost tab;
    EditText edtTim;
    ImageButton btnXoa;
    ListView lv1,lv2,lv3;
    ArrayList<Item> list1, list2, list3;
    ListItemAdapter myarray1, myarray2, myarray3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        database = openOrCreateDatabase("arirang.sqlite", MODE_PRIVATE, null);
        addControl();
        addEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tab.getCurrentTab() == 0)
        {
            getData();
        }
        else if (tab.getCurrentTab() == 1)
        {
            addDanhsach();
        }
        else
        {
            addYeuthich();
        }
    }

    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            //if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void addControl()
    {
        tab = findViewById(R.id.tabhost);
        tab.setup();
        TabHost.TabSpec tab1=tab.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.search));
        tab.addTab(tab1);
        TabHost.TabSpec tab2=tab.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.list));
        tab.addTab(tab2);
        TabHost.TabSpec tab3=tab.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("",getResources().getDrawable(R.drawable.favourite));
        tab.addTab(tab3);
        edtTim = findViewById(R.id.edtTim);
        btnXoa = findViewById(R.id.btnXoa);
        lv1 = findViewById(R.id.lv1);
        lv2 = findViewById(R.id.lv2);
        lv3 = findViewById(R.id.lv3);
        list1 =new ArrayList<Item>();
        list2 =new ArrayList<Item>();
        list3 =new ArrayList<Item>();
        myarray1 = new ListItemAdapter(MainActivity.this, R.layout.list_item, list1);
        myarray2 = new ListItemAdapter(MainActivity.this, R.layout.list_item, list2);
        myarray3 = new ListItemAdapter(MainActivity.this, R.layout.list_item, list3);
        lv1.setAdapter(myarray1);
        lv2.setAdapter(myarray2);
        lv3.setAdapter(myarray3);
    }

    private void addEvent()
    {
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("t2"))
                {
                    addDanhsach();
                }
                if (tabId.equalsIgnoreCase("t3"))
                {
                    addYeuthich();
                }
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTim.setText(null);
            }
        });
        edtTim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getData()
    {
        String inp = edtTim.getText().toString();
        myarray1.clear();
        if (!edtTim.getText().toString().equals(""))
        {
            Cursor c = database.rawQuery("SELECT * FROM ArirangSongList" +
                    " WHERE TENBH1 LIKE '"+"%"+ inp +"%"+"' OR MABH LIKE '"+"%"+ inp +"%"+"'", null);
            c.moveToFirst();
            while(c.isAfterLast()==false)
            {
                list1.add(new Item(c.getString(1), c.getString(2), c.getInt(6)));
                c.moveToNext();
            }
            c.close();
        }
        myarray1.notifyDataSetChanged();
    }

    private void addDanhsach()
    {
        myarray2.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList", null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            list2.add(new Item(c.getString(1), c.getString(2), c.getInt(6)));
            c.moveToNext();
        }
        c.close();
        myarray2.notifyDataSetChanged();
    }

    private void addYeuthich()
    {
        myarray3.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE YEUTHICH = 1", null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            list3.add(new Item(c.getString(1), c.getString(2), c.getInt(6)));
            c.moveToNext();
        }
        c.close();
        myarray3.notifyDataSetChanged();
    }
}