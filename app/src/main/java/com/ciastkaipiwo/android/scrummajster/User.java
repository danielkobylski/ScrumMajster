package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Klaudia on 26.04.2018.
 */

public class User implements Parcelable{

    //private int mUserId;
    private String mPassword;
    private String mFirsName;
    private String mLasName;

    User(String password, String firsName, String lasName){

        this.mPassword = password;
        this.mFirsName = firsName;
        this.mLasName = lasName;
    }

   // public int getUserId() {
        //return mUserId;
    //}


    public String getFirsName() {
        return mFirsName;
    }

    public void setFirsName(String firsName) {
        mFirsName = firsName;
    }

    public String getLasName() {
        return mLasName;
    }

    public void setLasName(String lasName) {
        mLasName = lasName;
    }
    protected User(Parcel in) {
        //mUserId = in.readInt();
        mPassword = in.readString();
        mFirsName = in.readString();
        mLasName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeInt(mUserId);
        dest.writeString(mPassword);
        dest.writeString(mFirsName);
        dest.writeString(mLasName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
