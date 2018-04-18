package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ProjectConfigActivity extends Activity {

    private static final String NEW_PROJECT = "com.ciastkaipiwo.android.scrummajster.new_project";

    private EditText mProjectTitle;
    private TextView mStartDate;
    private TextView mEndDate;
    private Button mSubmitButton;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_config);


        mProjectTitle = (EditText) findViewById(R.id.project_name_config);
        mSubmitButton = (Button) findViewById(R.id.project_submit_button);
        mStartDate = (TextView) findViewById(R.id.start_date);
        mEndDate = (TextView) findViewById(R.id.end_date);



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

                Project newProject = new Project(title, new GregorianCalendar(startYear,startMonth,startDay), new GregorianCalendar(endYear,endMonth,endDay));
                Intent data = new Intent();
                data.putExtra(NEW_PROJECT, newProject);
                setResult(RESULT_OK, data);
                finish();
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

    public static class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder>{

        private List<Task> tasksList;

        public TasksAdapter(List<Task> projectList) {
            this.tasksList = projectList;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tasks_list_row, parent, false);

            return new TaskViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.name.setText(tasksList.get(position).getStory());
            holder.weight.setText("Weight: "+tasksList.get(position).getWeight());
            holder.time.setText("Time: "+tasksList.get(position).getTime());
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return tasksList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView weight;
            public TextView time;
            public int position;

            public TaskViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.task_row_story);
                weight = (TextView) view.findViewById(R.id.task_row_weight);
                time = (TextView) view.findViewById(R.id.task_row_time);
            }

        }
    }
}
