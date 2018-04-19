package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Daniel on 02.04.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class Sprint implements Parcelable {

    private int mId;
    private GregorianCalendar mStartDate;
    private GregorianCalendar mEndDate;
    public ArrayList<Task> mTasksList;

    Sprint(){
        this.mStartDate= new GregorianCalendar();
        this.mEndDate = new GregorianCalendar();
        this.mTasksList = new ArrayList<Task>();
    }

    Sprint(int id, GregorianCalendar startDate, GregorianCalendar endDate)
    {
        this.mId = id;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mTasksList = new ArrayList<Task>();
    }

    public int getId() {return mId;}
    public GregorianCalendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        mStartDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return mEndDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        mEndDate = endDate;
    }

    public ArrayList<Task> getTasksList() {return mTasksList;}

    public void setTasksList(ArrayList<Task> tasksList) {mTasksList = tasksList;}


    protected Sprint(Parcel in) {
        mId = in.readInt();
        mStartDate = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
        mEndDate = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mTasksList = new ArrayList<Task>();
            in.readList(mTasksList, Task.class.getClassLoader());
        } else {
            mTasksList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeValue(mStartDate);
        dest.writeValue(mEndDate);
        if (mTasksList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mTasksList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Sprint> CREATOR = new Parcelable.Creator<Sprint>() {
        @Override
        public Sprint createFromParcel(Parcel in) {
            return new Sprint(in);
        }

        @Override
        public Sprint[] newArray(int size) {
            return new Sprint[size];
        }
    };


}













/////////////////////////////////////////////////
/*
import android.os.Parcel;
        import android.os.Parcelable;

        import java.util.*;

public class Sprint implements Parcelable{

    private GregorianCalendar mStartDate;
    private GregorianCalendar mEndDate;
    //public Dictionary mTasksDict;

    Sprint(){
        this.mStartDate= new GregorianCalendar();
        this.mEndDate = new GregorianCalendar();
        //this.mTasksDict = new Hashtable();
    }

    Sprint(GregorianCalendar startDate, GregorianCalendar endDate)
    {
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        //this.mTasksDict = new Hashtable();

    }

    protected Sprint(Parcel in) {
        mStartDate = (GregorianCalendar) in.readSerializable();
        mEndDate = (GregorianCalendar) in.readSerializable();
    }

    public static final Creator<Sprint> CREATOR = new Creator<Sprint>() {
        @Override
        public Sprint createFromParcel(Parcel in) {
            return new Sprint(in);
        }

        @Override
        public Sprint[] newArray(int size) {
            return new Sprint[size];
        }
    };

    public GregorianCalendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        mStartDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return mEndDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        mEndDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(mStartDate);
        parcel.writeSerializable(mEndDate);
    }

    // public Dictionary getTasksDict() {return mTasksDict;}

    //public void setTasksDict(Dictionary tasksDict) {mTasksDict = tasksDict;}
}
*/