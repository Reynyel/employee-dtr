package com.example.employee_dtr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.database.sqlite.SQLiteDatabase; //import for database

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


public class homepage extends AppCompatActivity {

    ImageButton tin_scan; //global variable for timein scan
    ImageButton tout_scan;//global variable for timeout scan

    SQLiteDatabase db; //global variable for database

    Calendar calendar;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        String passName = intent.getStringExtra("passName");
        String passId = intent.getStringExtra("passId");

        tin_scan=findViewById(R.id.timeinBtn);
        tout_scan=findViewById(R.id.timeoutBtn);

        tin_scan.setOnClickListener(view -> {
            scanTimeIn();
        });

        tout_scan.setOnClickListener(view -> {
            scanTimeOut();
        });

    }

    private void scanTimeIn(){ //Scan code for time in
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        timeIn.launch(options);
    }

    private void scanTimeOut(){ //Scan code for timeout
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        timeOut.launch(options);
    }



    ActivityResultLauncher<ScanOptions> timeIn = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);

            //Get date and time
            calendar = Calendar.getInstance();
            date = calendar.getTime();

            // Format the date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss a");
            String formattedDateTime = dateFormat.format(date);

                builder.setTitle("Time in: " + formattedDateTime); //returns the date and time
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
    });

    ActivityResultLauncher<ScanOptions> timeOut = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);

            //Get date and time
            calendar = Calendar.getInstance();
            date = calendar.getTime();

            // Format the date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            String formattedDateTime = dateFormat.format(date);

            builder.setTitle("Time out: " + formattedDateTime); //returns the date and time
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}