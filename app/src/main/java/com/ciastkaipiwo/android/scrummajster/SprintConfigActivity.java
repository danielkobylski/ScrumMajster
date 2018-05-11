package com.ciastkaipiwo.android.scrummajster;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class SprintConfigActivity extends AppCompatActivity {

    private static final String NEW_SPRINT = "com.ciastkaipiwo.android.scrummajster.new_sprint";
    private static final String SPRINT_TO_EDIT = "com.ciastkaipiwo.android.scrummajster.sprint_to_edit";
    private static final String OLD_SPRINT = "com.ciastkaipiwo.android.scrummajster.old_sprint";
    public String mUrl = "http://s12.mydevil.net:8080/";

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
    private Sprint mSprintToEdit;
    private List<Sprint> mAllSprintList = new ArrayList<Sprint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_config);
        startEditText = (TextView) findViewById(R.id.sprintStartDate);
        endEditText = (TextView) findViewById(R.id.sprintEndDate);

        mSprintToEdit = getIntent().getParcelableExtra(SPRINT_TO_EDIT);

        if (mSprintToEdit != null) {
            startEditText.setText(new SimpleDateFormat("dd/MM/yyyy").format(mSprintToEdit.getStartDate().getTimeInMillis()));
            endEditText.setText(new SimpleDateFormat("dd/MM/yyyy").format(mSprintToEdit.getEndDate().getTimeInMillis()));
        }


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
                int startMonth = Integer.valueOf(startDate[1])-1;
                int startYear = Integer.valueOf(startDate[2]);
                int endDay = Integer.valueOf(endDate[0]);
                int endMonth = Integer.valueOf(endDate[1])-1;
                int endYear = Integer.valueOf(endDate[2]);

                GregorianCalendar s = new GregorianCalendar(startYear,startMonth,startDay);
                GregorianCalendar e = new GregorianCalendar(endYear,endMonth,endDay);
                if (e.after(s)) {
                    Sprint newSprint = new Sprint(0, s, e);
                    Intent data = new Intent();
                    data.putExtra(NEW_SPRINT, newSprint);
                    data.putExtra(OLD_SPRINT, mSprintToEdit);
                    setResult(RESULT_OK, data);
                    finish();
                }
                else{
                    Toast.makeText(SprintConfigActivity.this,"End date is incorrect",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public static Sprint getNewSprint(Intent result) {
        return (Sprint) result.getParcelableExtra(NEW_SPRINT);
    }

    public static Intent newIntent(Context packageContext, Sprint sprint){
        Intent intent = new Intent(packageContext, SprintConfigActivity.class);
        intent.putExtra(SPRINT_TO_EDIT,sprint);
        return intent;
    }

    public static Sprint getOldSprint(Intent result) {
        return (Sprint) result.getParcelableExtra(OLD_SPRINT);
    }


}