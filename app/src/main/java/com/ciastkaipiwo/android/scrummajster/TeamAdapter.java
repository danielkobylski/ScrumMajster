package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Klaudia on 10.05.2018.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<Team> mTeamList;

    public TeamAdapter(List<Team> teamList) {
        this.mTeamList = teamList;
    }

    @Override
    public TeamAdapter.TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_row, parent, false);

        return new TeamAdapter.TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamAdapter.TeamViewHolder holder, int position) {

        holder.name.setText(mTeamList.get(position).getName());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final int REQUEST_CODE_EDIT_TEAM = 1;
        public TextView name;

        public int position;
        public ImageButton mTeamMenu;


        public TeamViewHolder(final View view) {
            super(view);
            view.setOnClickListener(this);


            name = (TextView) view.findViewById(R.id.name);


            mTeamMenu = (ImageButton) view.findViewById(R.id.team_menu);

            final PopupMenu pum = new PopupMenu(view.getContext(),mTeamMenu);
            pum.inflate(R.menu.team_popup_menu);

            pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.delete_team_button: {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                            builder1.setMessage("Are you sure?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            deleteTeam(position);
                                            //Intent tempIntent = new Intent(new Intent(view.getContext(), MainActivity.class));
                                            //tempIntent.putExtra("refresher", 1);
                                            //view.getContext().startActivity(tempIntent);
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
                        case R.id.edit_team_button: {
                            Intent intent = TeamConfigActivity.newIntent(view.getContext(),mTeamList.get(position));
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_TEAM);
                            break;
                        }
                    }
                    return true;
                }
            });

            mTeamMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pum.show();
                }
            });

        }
        @Override
        public void onClick(View v) {
            Intent intent = MainActivity.newIntent(v.getContext(),mTeamList.get(this.position));
            v.getContext().startActivity(intent);
        }
    }

    public void deleteTeam(final int position) {
        new AsyncHttpClient().delete("http://192.168.8.101:8080/team/"+mTeamList.get(position).getTeamID(), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mTeamList.remove(position);
                notifyDataSetChanged();
                Log.d("Delete Project result:", "Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Delete Project result:", "Failure");
            }
        });
    }

}
