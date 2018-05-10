package com.ciastkaipiwo.android.scrummajster;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klaudia on 09.05.2018.
 */

public class MiniTasks {

    private int mId;
    private int mTaskId;
    private String mStory;
    private int mKanbanFlag;

    MiniTasks(int id, int taskId, String story, int kanbanFlag){
        mId = id;
        mTaskId = taskId;
        mStory = story;
        mKanbanFlag = kanbanFlag;
    }

    MiniTasks(JSONObject miniTask) {
        try {
            mId = Integer.valueOf(miniTask.getString("miniTaskId"));
            // mSprintId = Integer.valueOf(task.getString("sprintId"));
            mTaskId = Integer.valueOf(miniTask.getString("taskId"));
            mStory = miniTask.getString("story");
            mKanbanFlag = Integer.valueOf(miniTask.getString("kanbanFlag"));

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int taskId) {
        mTaskId = taskId;
    }

    public String getStory() {
        return mStory;
    }

    public void setStory(String story) {
        mStory = story;
    }

    public int getKanbanFlag() {
        return mKanbanFlag;
    }

    public void setKanbanFlag(int kanbanFlag) {
        mKanbanFlag = kanbanFlag;
    }
}
