package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klaudia on 10.05.2018.
 */

public class User implements Parcelable {

    private int mUserId;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mMail;

    User(int id, String mail, String password, String firstName, String lastName){
        mUserId = id;
        mMail = mail;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;

    }

    User(JSONObject user) {
        try {
            mUserId = Integer.valueOf(user.getString("userId"));
            mMail = user.getString("mail");
            mPassword = user.getString("password");
            mFirstName = user.getString("firstName");
            mLastName = user.getString("lastName");

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getMail() {
        return mMail;
    }

    public void setMail(String mail) {
        mMail = mail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firsName) {
        mFirstName = firsName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lasName) {
        mLastName = lasName;
    }
    protected User(Parcel in) {
        mUserId = in.readInt();
        mPassword = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mMail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mUserId);
        dest.writeString(mPassword);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mMail);
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
