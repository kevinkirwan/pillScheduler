package com.kevinkirwansoftware.capsule;

import android.provider.BaseColumns;

public class SingleReminderColumns {

    private SingleReminderColumns(){}

    public static final class SingleReminderEntry implements BaseColumns{
        public static final String TABLE_NAME = "singleReminderList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_SCHEDULE_ID = "schedule_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
