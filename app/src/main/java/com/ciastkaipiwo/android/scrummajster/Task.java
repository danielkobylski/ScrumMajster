package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Task implements Parcelable {
    private int mId;
    private int mProjectId;
    private int mSprintId;
    private String mStory;
    private int mWeight;
    private int mTime;


    Task(int id, int sprintId, int projectId, String story, int weight, int time) {
        mId = id;
        mProjectId = projectId;
        mSprintId = sprintId;
        mStory = story;
        mWeight = weight;
        mTime = time;

    }

    Task(int id, String story, int weight, int time){
        mId = id;
        mStory = story;
        mWeight = weight;
        mTime = time;

    }

    Task(JSONObject task) {
        try {
            mId = Integer.valueOf(task.getString("taskId"));
            //mSprintId = Integer.valueOf(task.getString("sprintId"));
            mProjectId = Integer.valueOf(task.getString("projectId"));
            mStory = task.getString("story");
            mWeight = Integer.valueOf(task.getString("weight"));
            mTime = Integer.valueOf(task.getString("time"));

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {return mId;}

    public String getStory() {
        return mStory;
    }

    public void setStory(String story) {
        mStory = story;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
    }



    protected Task(Parcel in) {
        mId = in.readInt();
        mStory = in.readString();
        mWeight = in.readInt();
        mTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mStory);
        dest.writeInt(mWeight);
        dest.writeInt(mTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
