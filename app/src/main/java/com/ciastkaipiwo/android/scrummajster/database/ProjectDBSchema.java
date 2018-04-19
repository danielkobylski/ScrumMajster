package com.ciastkaipiwo.android.scrummajster.database;

/**
 * Created by Daniel on 18.04.2018.
 */

public class ProjectDBSchema {
    public static final class ProjectTable {
        public static final String NAME = "projects";

        public static final class Cols {
            public static final String PROJECT_ID = "project_id";
            public static final String NAME = "name";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
        }
    }

    public static final class TasksTable {
        public static final String NAME = "tasks";

        public static final class Cols {
            public static final String PROJECT_ID = "project_id";
            public static final String SPRINT_ID = "sprint_id";
            public static final String TASK_ID = "task_id";
            public static final String STORY = "story";
            public static final String WEIGHT = "weight";
            public static final String TIME = "time";
        }
    }
}
