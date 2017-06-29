package com.example.android.personalkasapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.android.personalkasapp.dbHelper.SqliteHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txt_masuk, txt_keluar, txt_saldo;
    ListView list_anggaran;
    String query_kas, query_total;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    ArrayList<HashMap<String, String>> arraykas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_masuk  =(TextView)findViewById(R.id.txt_masuk);
        txt_keluar =(TextView)findViewById(R.id.txt_keluar);
        txt_saldo  =(TextView)findViewById(R.id.txt_saldo);

        list_anggaran =(ListView)findViewById(R.id.list_anggaran);

        sqliteHelper = new SqliteHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent ke AddActivity dgn bentuk lain
                startActivity(new Intent(MainActivity.this, AddActivity.class));

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        query_kas =
                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi_id ORDER BY transaksi_id DESC";

        query_total =
                "SELECT SUM(jumlah) AS total, (SELECT SUM(jumlah) FROM transaksi WHERE status='MASUK') as masuk," +
                        "(SELECT SUM(jumlah) FROM transaksi WHERE status='KELUAR')as keluar FROM transaksi";

        KasAdapter();

    }

    private void KasAdapter() {

        arraykas.clear();list_anggaran.setAdapter(null);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(query_kas, null);
        cursor.moveToFirst();

        for (int i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Log.d("status", cursor.getString(1));

            HashMap<String, String>map = new HashMap<>();
            map.put("transaksi_id", cursor.getString(0));
            map.put("status",       cursor.getString(1));
            map.put("jumlah",       cursor.getString(2));
            map.put("keterangan",   cursor.getString(3));
//            map.put("tanggal",      cursor.getString(4));
            map.put("tanggal",      cursor.getString(5));

            arraykas.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arraykas, R.layout.list_anggaran,
                new String[]{"transaksi_id","status","jumlah","keterangan","tanggal"},
                new int[] {R.id.txt_transaksi_id, R.id.txt_status, R.id.txt_jumlah, R.id.txt_keterangan, R.id.txt_tanggal} );

        list_anggaran.setAdapter(simpleAdapter);

        KasTotal();
    }

    private void KasTotal(){

        NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(query_total, null);
        cursor.moveToFirst();

        txt_masuk.setText( rupiah.format(cursor.getDouble(1)) );
        txt_keluar.setText( rupiah.format(cursor.getDouble(2)) );
        txt_saldo.setText(
                rupiah.format(cursor.getDouble(1) - cursor.getDouble(2) )
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
