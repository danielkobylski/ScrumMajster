package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Daniel on 30.03.2018.
 */

public class Project implements Parcelable {

    private String mTitle;
    private GregorianCalendar mStartDate;
    private GregorianCalendar mEndDate;
    private ArrayList<Sprint> mSprints;

    Project(String title, GregorianCalendar startDate, GregorianCalendar endDate) {
        mTitle = title;
        mStartDate = startDate;
        mEndDate = endDate;
        mSprints = new ArrayList<Sprint>();
    }


    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {mTitle = title;}
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
    public ArrayList<Sprint> getSprints() {return mSprints;}
    public void setSprints(ArrayList<Sprint> sprints) {mSprints = sprints;}

    public void addSprint(Sprint sprint) {
        mSprints.add(sprint);
    }


    protected Project(Parcel in) {
        mTitle = in.readString();
        mStartDate = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
        mEndDate = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mSprints = new ArrayList<Sprint>();
            in.readList(mSprints, Sprint.class.getClassLoader());
        } else {
            mSprints = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeValue(mStartDate);
        dest.writeValue(mEndDate);
        if (mSprints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSprints);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}

////////////////////////////////////////////////
/*
public class Project implements Parcelable {

    private String mTitle;
    private GregorianCalendar mStartDate;
    private GregorianCalendar mEndDate;
    private ArrayList<Sprint> mSprints;

    Project(String title, GregorianCalendar startDate, GregorianCalendar endDate) {
        mTitle = title;
        mStartDate = startDate;
        mEndDate = endDate;
        mSprints = new ArrayList<Sprint>();
    }

    protected Project(Parcel in) {
        mTitle = in.readString();
        mStartDate = (GregorianCalendar) in.readSerializable();
        mEndDate = (GregorianCalendar) in.readSerializable();
        //mSprints =  in.readList(mSprints, null);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {mTitle = title;}
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
    public ArrayList<Sprint> getSprints() {return mSprints;}
    public void setSprints(ArrayList<Sprint> sprints) {mSprints = sprints;}

    public void addSprint(Sprint sprint) {
        mSprints.add(sprint);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeSerializable(mStartDate);
        parcel.writeSerializable(mEndDate);
    }
}
*/
