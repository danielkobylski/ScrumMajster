package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 05.04.2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;



interface ToDoListener {
    void imageButtonOnClik(int position);
}

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {


    private List<MiniTasks> mToDoListAdapter;
    private ToDoListener mListener;
    private ImageButton Ok;


    public ToDoAdapter(List<MiniTasks> toDoListAdapter, ToDoListener listener) {
        this.mToDoListAdapter = toDoListAdapter;
        this.mListener = listener;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_row, parent, false);

        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {

        holder.mListener = this.mListener;
        holder.miniTask.setText(mToDoListAdapter.get(position).getStory());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mToDoListAdapter.size();
    }


    public static class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView miniTask;
        public int position;
        public ImageButton mToDoArrowButton;
        private ToDoListener mListener;

        public ToDoViewHolder(View view) {
            super(view);
            miniTask = (TextView) view.findViewById(R.id.miniTaskToDo);
            mToDoArrowButton = (ImageButton) view.findViewById(R.id.arrow_down_to_do);
            mToDoArrowButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.imageButtonOnClik(getAdapterPosition());
            }
        }
    }


}
