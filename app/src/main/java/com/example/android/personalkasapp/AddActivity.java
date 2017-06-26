package com.example.android.personalkasapp;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;

public class AddActivity extends AppCompatActivity {

    RadioGroup radio_status;
    EditText et_jumlah, et_keterangan;
    Button btn_simpan;
    RippleView rip_simpan;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        status="";

        radio_status  =(RadioGroup)findViewById(R.id.radio_status);
        et_jumlah     =(EditText) findViewById(R.id.et_jumlah);
        et_keterangan =(EditText) findViewById(R.id.et_keterangan);
        btn_simpan    =(Button) findViewById(R.id.btn_simpan);
        rip_simpan    =(RippleView) findViewById(R.id.rip_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radio_income:
                        status= "MASUK";
                        break;
                    case R.id.radio_outcome:
                        status= "KELUAR";
                        break;
                }
                Log.d("Log Status", status);
            }
        });

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Toast.makeText(AddActivity.this, "Jumlah : " + et_jumlah.getText().toString()
                                + " Keterangan : "+ et_keterangan.getText().toString(),
                        Toast.LENGTH_LONG).show();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CONTOH TOAST
//                Toast.makeText(AddActivity.this, "Jumlah : " + et_jumlah.getText().toString()
//                                + " Keterangan : "+ et_keterangan.getText().toString(),
//                        Toast.LENGTH_LONG).show();
            }
        });

        //set title
        getSupportActionBar().setTitle("Tambahan Data");
        //return to Home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //ketika icon back diclick
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
