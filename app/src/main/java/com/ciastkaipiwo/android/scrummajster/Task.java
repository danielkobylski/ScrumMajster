package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Task implements Parcelable {
    private int mId;
    private String mStory;
    private int mWeight;
    private int mTime;
    private ArrayList<String> mToDo;
    private ArrayList<String> mDoing;
    private ArrayList<String> mDone;

    Task(int id, String story, int weight, int time) {
        mId = id;
        mStory = story;
        mWeight = weight;
        mTime = time;
        mToDo = new ArrayList<String>();
        mDoing = new ArrayList<String>();
        mDone = new ArrayList<String>();
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

    public ArrayList<String> getToDo() {return mToDo;}

    public void setToDo(ArrayList<String> toDo) {mToDo = toDo;}

    public ArrayList<String> getDoing() {return mDoing;}

    public void setDoing(ArrayList<String> doing) {mDoing = doing;}

    public ArrayList<String> getDone() {return mDone;}

    public void setDone(ArrayList<String> done) {mDone = done;}

    protected Task(Parcel in) {
        mId = in.readInt();
        mStory = in.readString();
        mWeight = in.readInt();
        mTime = in.readInt();
        if (in.readByte() == 0x01) {
            mToDo = new ArrayList<String>();
            in.readList(mToDo, String.class.getClassLoader());
        } else {
            mToDo = null;
        }
        if (in.readByte() == 0x01) {
            mDoing = new ArrayList<String>();
            in.readList(mDoing, String.class.getClassLoader());
        } else {
            mDoing = null;
        }
        if (in.readByte() == 0x01) {
            mDone = new ArrayList<String>();
            in.readList(mDone, String.class.getClassLoader());
        } else {
            mDone = null;
        }
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
        if (mToDo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mToDo);
        }
        if (mDoing == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mDoing);
        }
        if (mDone == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mDone);
        }
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
