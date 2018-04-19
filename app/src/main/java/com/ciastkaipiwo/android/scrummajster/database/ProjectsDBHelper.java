package com.ciastkaipiwo.android.scrummajster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ciastkaipiwo.android.scrummajster.Project;
import com.ciastkaipiwo.android.scrummajster.Sprint;
import com.ciastkaipiwo.android.scrummajster.Task;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.ProjectTable;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.SprintsTable;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.TasksTable;
import com.ciastkaipiwo.android.scrummajster.database.ProjectDBSchema.MiniTasksTable;

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

        String createTableSprints = "create table " + SprintsTable.NAME + "(" +
                SprintsTable.Cols.SPRINT_ID     + " integer primary key autoincrement, " +
                SprintsTable.Cols.PROJECT_ID    + ", " +
                SprintsTable.Cols.START_DATE    + ", " +
                SprintsTable.Cols.END_DATE  +  ", " +
                "foreign key (project_id) references projects(project_id) on delete cascade)";

        String createTableTasks = "create table " + TasksTable.NAME + "(" +
                TasksTable.Cols.TASK_ID + " integer primary key autoincrement, " +
                TasksTable.Cols.PROJECT_ID + ", " +
                TasksTable.Cols.SPRINT_ID + ", " +
                TasksTable.Cols.STORY + ", " +
                TasksTable.Cols.WEIGHT + ", " +
                TasksTable.Cols.TIME  +  ", " +
                "foreign key (sprint_id) references sprints(sprint_id) on delete set null," +
                "foreign key (project_id) references projecs(project_id) on delete cascade)";

        String createTableMiniTasks = "create table " + MiniTasksTable.NAME + "(" +
                MiniTasksTable.Cols.MINI_TASK_ID + " integer primary key autoincrement, " +
                MiniTasksTable.Cols.TASK_ID + ", " +
                MiniTasksTable.Cols.STORY + ", " +
                MiniTasksTable.Cols.KANBAN_FLAG + ", " +
                "foreign key (task_id) references tasks(task_id) on delete cascade)";

        db.execSQL(createTableProjects);
        db.execSQL(createTableSprints);
        db.execSQL(createTableTasks);
        db.execSQL(createTableMiniTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTable.NAME);
        onCreate(db);
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

    public boolean removeProject(Project project) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(ProjectTable.NAME, ProjectTable.Cols.PROJECT_ID + "=" + project.getId(),null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean editProject(Project oldProject, Project newProject) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(ProjectTable.Cols.NAME, newProject.getTitle());
        values.put(ProjectTable.Cols.START_DATE, newProject.getStartDate().getTimeInMillis());
        values.put(ProjectTable.Cols.END_DATE, newProject.getEndDate().getTimeInMillis());
        long result = db.update(ProjectTable.NAME,values,ProjectTable.Cols.PROJECT_ID + "=" + oldProject.getId() ,null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addSprint(int project_id, Sprint sprint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SprintsTable.Cols.PROJECT_ID, project_id);
        values.put(SprintsTable.Cols.START_DATE, sprint.getStartDate().getTimeInMillis());
        values.put(SprintsTable.Cols.END_DATE, sprint.getEndDate().getTimeInMillis());

        long result = db.insert(SprintsTable.NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean removeSprint(Sprint sprint) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(SprintsTable.NAME, SprintsTable.Cols.SPRINT_ID + "=" + sprint.getId(),null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean editSprint(Sprint oldSprint, Sprint newSprint) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(SprintsTable.Cols.START_DATE, newSprint.getStartDate().getTimeInMillis());
        values.put(SprintsTable.Cols.END_DATE, newSprint.getEndDate().getTimeInMillis());

        long result = db.update(SprintsTable.NAME,values,SprintsTable.Cols.SPRINT_ID + "=" + oldSprint.getId() ,null);
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

    public boolean removeTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TasksTable.NAME, TasksTable.Cols.TASK_ID + "=" + task.getId(),null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean editTask(Task oldTask, Task newTask) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(TasksTable.Cols.STORY, newTask.getStory());
        values.put(TasksTable.Cols.WEIGHT, newTask.getWeight());
        values.put(TasksTable.Cols.TIME, newTask.getTime());
        long result = db.update(TasksTable.NAME,values,TasksTable.Cols.TASK_ID + "=" + oldTask.getId() ,null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
