package com.ciastkaipiwo.android.scrummajster;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class SprintConfigActivity extends AppCompatActivity {

    private static final String NEW_SPRINT = "com.ciastkaipiwo.android.scrummajster.new_sprint";

    EditText nameEditText;
    TextView startEditText;
    TextView endEditText;
    DatePickerDialog.OnDateSetListener mDateSetListenerS;
    DatePickerDialog.OnDateSetListener mDateSetListenerE;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_config);


        startEditText = (TextView) findViewById(R.id.sprintStartDate);
        startEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DatePickerDialog dialog = new DatePickerDialog(SprintConfigActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar, mDateSetListenerS, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String startDate = day+"/"+month+"/"+year;
                startEditText.setText(startDate);
            }
        };

        endEditText = (TextView) findViewById(R.id.sprintEndDate);
        endEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DatePickerDialog dialog = new DatePickerDialog(SprintConfigActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar, mDateSetListenerE, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String endDate = day+"/"+month+"/"+year;
                endEditText.setText(endDate);
            }
        };

        String startDateSprint = startEditText.getText().toString();
        String endDateSprint = endEditText.getText().toString();

        okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){

                String[] startDate = startEditText.getText().toString().split("/",0);
                String[] endDate = endEditText.getText().toString().split("/",0);
                int startDay = Integer.valueOf(startDate[0]);
                int startMonth = Integer.valueOf(startDate[1]);
                int startYear = Integer.valueOf(startDate[2]);
                int endDay = Integer.valueOf(endDate[0]);
                int endMonth = Integer.valueOf(endDate[1]);
                int endYear = Integer.valueOf(endDate[2]);


                Sprint newSprint = new Sprint(new GregorianCalendar(startYear,startMonth,startDay), new GregorianCalendar(endYear,endMonth,endDay));
                Intent data = new Intent();
                data.putExtra(NEW_SPRINT, newSprint);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public static Sprint getNewSprint(Intent result) {
        return (Sprint) result.getParcelableExtra(NEW_SPRINT);
    }

}