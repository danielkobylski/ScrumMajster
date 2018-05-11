package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProjectConfigActivity extends Activity {

    private static final String NEW_PROJECT = "com.ciastkaipiwo.android.scrummajster.new_project";
    private static final String PROJECT_TO_EDIT = "com.ciastkaipiwo.android.scrummajster.project_to_edit";
    private static final String OLD_PROJECT = "com.ciastkaipiwo.android.scrummajster.old_project";

    private EditText mProjectTitle;
    private TextView mStartDate;
    private TextView mEndDate;
    private Button mSubmitButton;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;
    private Project mProjectToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_config);

        mProjectToEdit = getIntent().getParcelableExtra(PROJECT_TO_EDIT);

        mProjectTitle = (EditText) findViewById(R.id.project_name_config);
        mSubmitButton = (Button) findViewById(R.id.project_submit_button);
        mStartDate = (TextView) findViewById(R.id.start_date);
        mEndDate = (TextView) findViewById(R.id.end_date);

        if (mProjectToEdit != null) {
            mProjectTitle.setText(mProjectToEdit.getTitle());
            mStartDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(mProjectToEdit.getStartDate().getTimeInMillis()));
            mEndDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(mProjectToEdit.getEndDate().getTimeInMillis()));
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] startDate = mStartDate.getText().toString().split("\\.",0);

                String[] endDate = mEndDate.getText().toString().split("\\.",0);


                int startDay = Integer.valueOf(startDate[2]);
                int startMonth = Integer.valueOf(startDate[1]);
                int startYear = Integer.valueOf(startDate[0]);
                int endDay = Integer.valueOf(endDate[2]);
                int endMonth = Integer.valueOf(endDate[1]);
                int endYear = Integer.valueOf(endDate[0]);
                String title = mProjectTitle.getText().toString();

                GregorianCalendar s = new GregorianCalendar(startYear,startMonth-1,startDay);
                GregorianCalendar e = new GregorianCalendar(endYear,endMonth-1,endDay);
                if(e.after(s)){
                    Project newProject = new Project(0,title, s, e,0);
                    Intent data = new Intent();
                    data.putExtra(NEW_PROJECT, newProject);
                    data.putExtra(OLD_PROJECT, mProjectToEdit);
                    setResult(RESULT_OK, data);
                    finish();
                }
                else{
                    Toast.makeText(ProjectConfigActivity.this, "End date is incorrect", Toast.LENGTH_SHORT).show();
                }


            }

        });
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCalendar(mStartDateSetListener);
            }
        });

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = year + "." + ("00"+String.valueOf(month+1)).substring(String.valueOf(month+1).length()) + "." + ("00"+String.valueOf(day)).substring(String.valueOf(day).length());
                mStartDate.setText(date);
            }
        };

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCalendar(mEndDateSetListener);
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = year + "." + ("00"+String.valueOf(month+1)).substring(String.valueOf(month+1).length()) + "." + ("00"+String.valueOf(day)).substring(String.valueOf(day).length());
                mEndDate.setText(date);
            }
        };
    }

    public void makeCalendar(DatePickerDialog.OnDateSetListener date) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                ProjectConfigActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                date,
                year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static Project getNewProject(Intent result) {
        return (Project) result.getParcelableExtra(NEW_PROJECT);
    }

    public static Project getOldProject(Intent result) {
        return (Project) result.getParcelableExtra(OLD_PROJECT);
    }

    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, ProjectConfigActivity.class);
        intent.putExtra(PROJECT_TO_EDIT,project);

        return intent;
    }


}
