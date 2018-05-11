package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 12.04.2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

interface DoneListener {
    void UpButtonOnClickDone(View v,int position);
}

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.DoneViewHolder> {

    private List<MiniTasks> mDoneListAdapter;
    private DoneListener mListener;

    public DoneAdapter(List<MiniTasks> doneListAdapter, DoneListener listener) {
        this.mDoneListAdapter = doneListAdapter;
        this.mListener = listener;
    }

    @Override
    public DoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_list_row, parent, false);

        return new DoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoneViewHolder holder, int position) {
        holder.mListener = this.mListener;
        holder.miniTask.setText(mDoneListAdapter.get(position).getStory());
        holder.position = position;
    }
    public void removeItem(int position) {
        mDoneListAdapter.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDoneListAdapter.size();
    }


    public class DoneViewHolder extends RecyclerView.ViewHolder {

        public TextView miniTask;
        public int position;
        private DoneListener mListener;
        public ImageButton mDoneUpButton;


        public DoneViewHolder(View view) {
            super(view);
            miniTask = (TextView) view.findViewById(R.id.mini_task_done);

            mDoneUpButton = (ImageButton) view.findViewById(R.id.arrow_up_done);
            mDoneUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.UpButtonOnClickDone(v,getAdapterPosition());
                }
            });
        }

    }
}
