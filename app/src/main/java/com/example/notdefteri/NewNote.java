package com.example.notdefteri;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class NewNote extends AppCompatActivity {

    private Button pickDate,pickTime,add;
    private TextView date,time;
    private CheckBox reminder;
    private FirebaseFirestore db;
    private EditText title,body;
    private int r_year,r_month,r_day,r_hour,r_minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        pickTime = findViewById(R.id.pick_time);
        pickDate = findViewById(R.id.pick_date);
        date = findViewById(R.id.pickedDate);
        time = findViewById(R.id.pickedTime);
        reminder = findViewById(R.id.reminder_checkbox);
        add = findViewById(R.id.addNote);

        pickTime.setVisibility(View.INVISIBLE);
        pickDate.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
    }

    public void pickTime(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePicker = null;
        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.setText(hourOfDay +":"+minute);
                r_hour = hourOfDay;
                r_minute = minute;
            }
        },hour,minute,true);
        timePicker.setTitle("Pick Time");
        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,"Set",timePicker);
        timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Exit",timePicker);
        timePicker.show();
    }

    public void pickDate(View view) {
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH);
        int day = currentTime.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date.setText(year+"/"+month+"/"+dayOfMonth);
                    r_day = dayOfMonth;
                    r_month = month;
                    r_year = year;
                }
            },year,month,day);
        };
        datePicker.setTitle("Pick Date");
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,"Set",datePicker);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Exit",datePicker);
        datePicker.show();
    }

    public void add(View view) {
        HashMap<String,Object> note = new HashMap<>();

        DocumentReference ref = db.collection("Notes").document();
        String id = ref.getId();

        note.put("id",id);
        note.put("title",title.getText().toString());
        note.put("body",body.getText().toString());
        note.put("timestamp",Timestamp.now());
        if(reminder.isChecked()){
            Calendar c = Calendar.getInstance();
            c.set(r_year,r_month,r_day,r_hour,r_minute);
            Timestamp t = new Timestamp(c.getTime());
            note.put("remindTime",t);
            note.put("reminder",true);
        }else{
            note.put("reminder",false);
        }
        db.collection("Notes").document(id).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Not Basariyla Eklendi",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Not Ekleme Basarisiz!!!",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void reminderClicked(View view) {
        if(reminder.isChecked()){
            pickTime.setVisibility(View.VISIBLE);
            pickDate.setVisibility(View.VISIBLE);
        }else{
            time.setText("");
            date.setText("");
            pickTime.setVisibility(View.INVISIBLE);
            pickDate.setVisibility(View.INVISIBLE);
        }
    }
}
