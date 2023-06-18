package com.example.employee_dtr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class homepage extends AppCompatActivity {

    ImageButton tin_scan; //global variable for timein scan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tin_scan=findViewById(R.id.timeinBtn);

        tin_scan.setOnClickListener(view -> {
            scanCode();
        });

    }

    private void scanCode(){
        
    }
}