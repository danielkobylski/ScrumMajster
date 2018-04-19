package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Daniel on 31.03.2018.
 */

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
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>{

    private List<Project> projectList;

    public ProjectsAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_list_row, parent, false);

        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        holder.title.setText(projectList.get(position).getTitle());
        holder.startDate.setText("Start date: " + dateFormat.format(projectList.get(position).getStartDate().getTimeInMillis()));
        holder.endDate.setText("End date:   " + dateFormat.format(projectList.get(position).getEndDate().getTimeInMillis()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final int REQUEST_CODE_EDIT_PROJECT = 2;
        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public int position;
        public ImageButton mProjectMenu;
        public ProjectsDBHelper mDatabaseHelper;


        public ProjectViewHolder(final View view) {
            super(view);
            view.setOnClickListener(this);

            mDatabaseHelper = new ProjectsDBHelper(view.getContext());

            title = (TextView) view.findViewById(R.id.title);
            startDate = (TextView) view.findViewById(R.id.start_date);
            endDate = (TextView) view.findViewById(R.id.end_date);

            mProjectMenu = (ImageButton) view.findViewById(R.id.project_menu);

            final PopupMenu pum = new PopupMenu(view.getContext(),mProjectMenu);
            pum.inflate(R.menu.project_popup_menu);

            pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Toast.makeText(view.getContext(), menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                    switch (menuItem.getItemId()) {
                        case R.id.delete_project_button: {
                            //mDatabaseHelper.removeProject(projectList.get(position));

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                            builder1.setMessage("Are you sure?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mDatabaseHelper.removeProject(projectList.get(position));
                                            view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
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
                        case R.id.edit_project_button: {
                            Intent intent = ProjectConfigActivity.newIntent(view.getContext(),projectList.get(position));
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_PROJECT);
                            break;
                        }
                    }
                    return true;
                }
            });

            mProjectMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pum.show();
                }
            });

        }
        @Override
        public void onClick(View v) {
            Intent intent = ProjectActivity.newIntent(v.getContext(),projectList.get(this.position));
            v.getContext().startActivity(intent);
        }
    }
}