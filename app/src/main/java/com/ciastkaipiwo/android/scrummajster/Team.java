package com.ciastkaipiwo.android.scrummajster;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klaudia on 10.05.2018.
 */

public class Team implements Parcelable{

    private int mTeamId;
    private String mName;


    Team(int id, String name){
        mTeamId = id;
        mName = name;

    }

    public int getTeamID() {
        return mTeamId;
    }

    public void setTeamID(int teamID) {
        mTeamId = teamID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    Team(JSONObject team) {
        try {
            mTeamId = Integer.valueOf(team.getString("teamId"));
            mName = team.getString("name");


        } catch(JSONException e) {
            e.printStackTrace();
        }
    }


    protected Team(Parcel in) {
        mTeamId = in.readInt();
        mName = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTeamId);
        dest.writeString(mName);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };


}
