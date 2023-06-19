package com.example.employee_dtr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText name;
    EditText id;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.nameTxt);
        id = findViewById(R.id.idTxt);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passName = name.getText().toString();
                String passId = id.getText().toString();


                Intent intent = new Intent(MainActivity.this, homepage.class);
                intent.putExtra("passName", passName);
                intent.putExtra("passId", passId);
                startActivity(intent);


            }
        });

    }
}