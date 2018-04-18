package com.ciastkaipiwo.android.scrummajster;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SprintAdapter extends RecyclerView.Adapter<SprintAdapter.SprintViewHolder> {

    private List<Task> taskListAdapter;

    public SprintAdapter(List<Task> taskListAdapter) {
        this.taskListAdapter = taskListAdapter;
    }


    @Override
    public SprintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sprint_list_row, parent, false);

        return new SprintViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SprintViewHolder holder, int position) {
        holder.weight.setText("#"+String.valueOf(taskListAdapter.get(position).getWeight()));
        holder.story.setText(taskListAdapter.get(position).getStory());
        holder.time.setText("Time: "+String.valueOf(taskListAdapter.get(position).getTime())+"h");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return taskListAdapter.size();
    }


    public class SprintViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView weight;
        public TextView story;
        public TextView time;
        public int position;

        public SprintViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            weight = (TextView) view.findViewById(R.id.weight);
            story = (TextView) view.findViewById(R.id.story);
            time = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public void onClick(View view) {
            Intent intent = KanbanActivity.newIntent(view.getContext(), taskListAdapter.get(this.position));
            view.getContext().startActivity(intent);

        }

    }
}