package com.ciastkaipiwo.android.scrummajster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ciastkaipiwo.android.scrummajster.Project;
import com.ciastkaipiwo.android.scrummajster.Task;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.ProjectTable;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.TasksTable;

import java.util.Date;

/**
 * Created by Daniel on 18.04.2018.
 */

public class ProjectsDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "projects.db";

    public ProjectsDBHelper (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableProjects = "create table " + ProjectTable.NAME + "(" +
                ProjectTable.Cols.PROJECT_ID + " integer primary key autoincrement, " +
                ProjectTable.Cols.NAME + ", " +
                ProjectTable.Cols.START_DATE + " datetime, " +
                ProjectTable.Cols.END_DATE  + " datetime" +
                ")";

        String createTableTasks = "create table " + TasksTable.NAME + "(" +
                TasksTable.Cols.TASK_ID + " integer primary key autoincrement, " +
                TasksTable.Cols.PROJECT_ID + ", " +
                TasksTable.Cols.SPRINT_ID + ", " +
                TasksTable.Cols.STORY + ", " +
                TasksTable.Cols.WEIGHT + ", " +
                TasksTable.Cols.TIME  +
                ")" ;

        db.execSQL(createTableProjects);
        db.execSQL(createTableTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTable.NAME);
        onCreate(db);
    }

    public void dropTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTable.NAME);
    }
    public Cursor getProjects() {
        String query = "SELECT * FROM " + ProjectTable.NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBacklogTasks(int project_id) {
        String query = "SELECT * FROM " + TasksTable.NAME + " WHERE project_id = " + project_id + " AND sprint_id = -1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addProject (Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectTable.Cols.NAME, project.getTitle());
        values.put(ProjectTable.Cols.START_DATE, project.getStartDate().getTimeInMillis());
        values.put(ProjectTable.Cols.END_DATE, project.getEndDate().getTimeInMillis());

        long result = db.insert(ProjectTable.NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addTask (int project_id, int sprint, Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksTable.Cols.PROJECT_ID, project_id);
        values.put(TasksTable.Cols.SPRINT_ID, sprint);
        values.put(TasksTable.Cols.STORY, task.getStory());
        values.put(TasksTable.Cols.WEIGHT, task.getWeight());
        values.put(TasksTable.Cols.TIME, task.getTime());


        long result = db.insert(TasksTable.NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
