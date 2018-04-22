package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SprintsAdapter extends RecyclerView.Adapter<SprintsAdapter.SprintViewHolder> {

    private static final String SPRINT_CHOSEN = "com.ciastkaipiwo.android.scrummajster.sprint_chosen";
    private static final String TASK_TO_MOVE = "com.ciastkaipiwo.android.scrummajster.task_to_move";
    private List<Sprint> mSprintsList;
    private int mProjectId;


    public SprintsAdapter(List<Sprint> sprintsList, int projectId) {
        this.mSprintsList = sprintsList;
        mProjectId = projectId;
    }

    @Override
    public SprintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sprint_list_row, parent, false);
        return new SprintViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SprintsAdapter.SprintViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        holder.sprintName.setText("Sprint "+(position+1));
        holder.startDate.setText("Start date: " + dateFormat.format(mSprintsList.get(position).getStartDate().getTimeInMillis()));
        holder.endDate.setText("Start date: " + dateFormat.format(mSprintsList.get(position).getEndDate().getTimeInMillis()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mSprintsList.size();
    }


    public class SprintViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int REQUEST_CODE_EDIT_SPRINT = 2;


        public TextView sprintName;
        public TextView startDate;
        public TextView endDate;
        public int position;
        public ImageButton mSprintMenu;
        public ProjectsDBHelper mDatabaseHelper;

        public SprintViewHolder(final View view) {
            super(view);
            view.setOnClickListener(this);
            sprintName = (TextView) view.findViewById(R.id.sprint_name);
            startDate = (TextView) view.findViewById(R.id.sprint_start_date);
            endDate = (TextView) view.findViewById(R.id.sprint_end_date);

            mDatabaseHelper = new ProjectsDBHelper(view.getContext());
            mSprintMenu = (ImageButton) view.findViewById(R.id.sprint_menu);

            final PopupMenu pum = new PopupMenu(view.getContext(),mSprintMenu);
            pum.inflate(R.menu.sprint_popup_menu);

            pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.delete_sprint_button: {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                            builder1.setMessage("Are you sure?");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mDatabaseHelper.removeSprint(mSprintsList.get(position));
                                            Intent tempIntent = new Intent(new Intent(view.getContext(), SprintMainActivity.class));
                                            tempIntent.putExtra("refresher", 1);
                                            view.getContext().startActivity(tempIntent);
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            break;
                        }
                        case R.id.edit_sprint_button: {
                            Intent intent = SprintConfigActivity.newIntent(view.getContext(),mSprintsList.get(position));
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_SPRINT);
                            break;
                        }
                    }
                    return true;
                }
            });

            mSprintMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pum.show();
                }
            });

        }

        @Override
        public void onClick(View v) {
           // if(String.valueOf(v.getContext().getClass()).equals("class com.ciastkaipiwo.android.scrummajster.SprintChoiceActivity")) {
           //     Intent data = new Intent();
           //     data.putExtra(SPRINT_CHOSEN, mSprintsList.get(position));
           //    // data.putExtra(TASK_TO_MOVE, getIntent().getIntExtra());
           //     ((Activity) v.getContext()).setResult(RESULT_OK, data);
           //     ((Activity) v.getContext()).finish();
           // }
        }

    }
}