package com.example.employee_dtr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class homepage extends AppCompatActivity {

    ImageButton tin_scan; //global variable for timein scan
    Calendar calendar;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tin_scan=findViewById(R.id.timeinBtn);

        tin_scan.setOnClickListener(view -> {
            scanCode();
        });

    }

    private void scanCode(){ //Scan code
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);

            //Get date and time
            calendar = Calendar.getInstance();
            date = calendar.getTime();

            // Format the date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            String formattedDateTime = dateFormat.format(date);

            builder.setTitle(formattedDateTime);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}