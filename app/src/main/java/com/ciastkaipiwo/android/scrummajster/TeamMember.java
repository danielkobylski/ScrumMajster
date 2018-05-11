package com.ciastkaipiwo.android.scrummajster;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klaudia on 10.05.2018.
 */

public class TeamMember {

    private int mMemberId;
    private int mTeamId;
    private int mUserId;


    TeamMember(int memberId,int teamId, int userId){
        mMemberId = memberId;
        mTeamId=teamId;
        mUserId=userId;
    }

    TeamMember(JSONObject teamMember) {
        try {
            mMemberId = Integer.valueOf(teamMember.getString("memberId"));
            mTeamId = Integer.valueOf(teamMember.getString("teamId"));
            mUserId = Integer.valueOf(teamMember.getString("userId"));


        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
