package com.example.employee_dtr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
    Button view;

    SQLiteDatabase db; //global variable for database

    Calendar calendar;
    Date date;

    String passName;
    String passId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        passName = intent.getStringExtra("passName");
        passId = intent.getStringExtra("passId");

        tin_scan=findViewById(R.id.timeinBtn);
        tout_scan=findViewById(R.id.timeoutBtn);
        view = findViewById(R.id.viewBtn);

        db = openOrCreateDatabase("EmployeeDTR", Context.MODE_PRIVATE, null); //creates the database
        db.execSQL("CREATE TABLE IF NOT EXISTS employee(employee_id VARCHAR, employee_name VARCHAR, time_in VARCHAR, time_out VARCHAR);");

        tin_scan.setOnClickListener(view -> {
            scanTimeIn();

        });

        tout_scan.setOnClickListener(view -> {
            scanTimeOut();
        });

        view.setOnClickListener(view ->{
            getDbContents();
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

    private void getDbContents(){
        Cursor c = db.rawQuery("SELECT * FROM employee", null);
        StringBuilder stringBuilder = new StringBuilder();

        if (c.moveToFirst()) {
            do {
                String employeeId = c.getString(0);
                String employeeName = c.getString(1);
                String timeIn = c.getString(2);
                String timeOut = c.getString(3);

                stringBuilder.append("Employee ID: ").append(employeeId).append("\n");
                stringBuilder.append("Employee Name: ").append(employeeName).append("\n");
                stringBuilder.append("Time In: ").append(timeIn).append("\n");
                stringBuilder.append("Time Out: ").append(timeOut).append("\n\n");
            } while (c.moveToNext());
        }

        c.close();

        String message = stringBuilder.toString();
        showMessageDialog(message);
    }

    private void showMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Database Contents");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
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

            // Insert time-in into the database
            db.execSQL("INSERT INTO employee (employee_id, employee_name, time_in, time_out) VALUES (?, ?, ?, ?)",
                    new String[]{passId, passName, formattedDateTime, ""});

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

            // Insert time-out into the database
            db.execSQL("UPDATE employee SET time_out = ? WHERE employee_id = ?",
                    new String[]{formattedDateTime, passId});

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