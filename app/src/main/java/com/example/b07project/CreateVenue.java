package com.example.b07project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class CreateVenue extends AppCompatActivity {
    Button sTime, eTime;
    int shour, sminute;
    int ehour, eminute;
    private TextView Date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Venue venue;
    TextView vename;
    boolean stime;
    boolean etime;

    FirebaseDatabase database;
    DatabaseReference reference;
    Button createbut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_venue);
        createbut = findViewById(R.id.CV);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Venues").push();
        venue = new Venue();
        vename = findViewById(R.id.Vename);

        sTime = findViewById(R.id.startTime);
        eTime = findViewById(R.id.endTime);
        Date = findViewById(R.id.Date);
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateVenue.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                venue.date = date;
                Date.setText(date);
            }
        };

        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String venuename = vename.getText().toString().trim();
                if(TextUtils.isEmpty(venuename) || TextUtils.isEmpty(venue.getDate()) || !stime || !etime){
                    Toast.makeText(CreateVenue.this,"No fields can be empty", Toast.LENGTH_SHORT).show();
                }
                else if(venue.getStartHour()>venue.getEndHour() || ((venue.getStartHour()==venue.getEndHour()) && venue.getStartMin()>venue.getEndMin())){
                    Toast.makeText(CreateVenue.this,"Enter valid start and end times", Toast.LENGTH_SHORT).show();
                }
                else {
                    venue.venueName = venuename;
                    reference.setValue(venue).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateVenue.this, "Data added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), AdminMain.class));
                }
            }
        });

    }
    public void popTimePicker1(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                shour = hour;
                sminute = minutes;
                sTime.setText(String.format(Locale.getDefault(), "%02d:%02d", shour, sminute));
                stime = true;
                venue.setStartHour(shour);
                venue.setStartMin(sminute);


            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog picker = new TimePickerDialog(this, style, onTimeSetListener, shour, sminute, true);

        picker.setTitle("Select Start Time");
        picker.show();

    }

    public void popTimePicker2(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                ehour = hour;
                eminute = minutes;
                eTime.setText(String.format(Locale.getDefault(), "%02d:%02d", ehour, eminute));
                etime = true;
                venue.setEndHour(ehour);
                venue.setEndMin(eminute);
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog picker = new TimePickerDialog(this, style, onTimeSetListener, ehour, eminute, true);
        picker.setTitle("Select End Time");
        picker.show();

    }


}
