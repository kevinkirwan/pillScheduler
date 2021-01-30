package com.kevinkirwansoftware.capsule;

import android.provider.BaseColumns;

public class ReminderColumns {

    private ReminderColumns(){}

    public static final class ReminderEntry implements BaseColumns{
        public static final String TABLE_NAME = "reminderList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DAILY_REMINDERS = "daily_reminders";
        public static final String COLUMN_MENU_VISIBLE = "menu_visible";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_SCHEDULE_ID = "schedule_id";
    }
}
