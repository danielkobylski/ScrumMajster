package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 11.04.2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

interface DoingListener {
    void DownButtonOnClik(View v,int position);
    void UpButtonOnClick(View v,int position);
}

public class DoingAdapter extends RecyclerView.Adapter<DoingAdapter.DoingViewHolder> {

    private List<MiniTasks> mDoingListAdapter;
    private DoingListener mListener;

    public DoingAdapter(List<MiniTasks> doingListAdapter, DoingListener listener) {
        this.mDoingListAdapter = doingListAdapter;
        this.mListener = listener;
    }


    @Override
    public DoingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doing_list_row, parent, false);

        return new DoingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoingViewHolder holder, int position) {
        holder.mListener = this.mListener;
        holder.miniTask.setText(mDoingListAdapter.get(position).getStory());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mDoingListAdapter.size();
    }


    public class DoingViewHolder extends RecyclerView.ViewHolder {

        public TextView miniTask;
        public int position;
        private DoingListener mListener;
        public ImageButton mDoingUpButton;
        public ImageButton mDoingDownButton;


        public DoingViewHolder(View view) {
            super(view);
            miniTask = (TextView) view.findViewById(R.id.miniTaskDoing);
            mDoingDownButton = (ImageButton) view.findViewById(R.id.arrow_down_doing);
            mDoingDownButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListener.DownButtonOnClik(v,getAdapterPosition());
                }
            });
            mDoingUpButton = (ImageButton) view.findViewById(R.id.arrow_up_doing);
            mDoingUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.UpButtonOnClick(v,getAdapterPosition());
                }
            });
        }

    }
}

