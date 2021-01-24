package com.example.everydayreminderapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.everydayreminderapp.Database.Model.DayOfWeekModel;
import com.example.everydayreminderapp.Database.Model.EventModel;
import com.example.everydayreminderapp.Database.Model.RepeatTimeModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "EverydayReminderDB";
    public static final String TB_EVENT = "Event";
    public static final String COL_EVENT_ID = "Event_ID";
    public static final String COL_EVENT_TITLE = "Event_Title";
    public static final String COL_EVENT_DESCRIPTION = "Event_Description";
    public static final String COL_EVENT_WHEN = "Event_When";
    public static final String COL_EVENT_HAS_REPEAT_TIME = "Event_HasRepeatTime";
    public static final String COL_EVENT_REPEAT_TIME_ID = "RepeatTime_ID";
    public static final String COL_EVENT_END = "Event_End";

    public static final String TB_REPEAT_TIME = "RepeatTime";
    public static final String COL_REPEAT_TIME_ID = "RepeatTime_ID";
    public static final String COL_REPEAT_TIME_NAME = "RepeatTime_Name";
    public static final String COL_REPEAT_TIME_VALUE = "RepeatTime_Value";

    public static final String TB_DAY_OF_WEEK = "DayOfWeek";
    public static final String COL_DAY_OF_WEEK_ID = "DayOfWeek_ID";
    public static final String COL_DAY_OF_WEEK_NAME = "DayOfWeek_Name";

    public static final String TB_REPEAT_DAY = "RepeatDay";
    public static final String COL_REPEAT_DAY_EVENT_ID = "Event_ID";
    public static final String COL_REPEAT_DAY_DAY_OF_WEEK_ID = "DayOfWeek_ID";

    public static final String TB_NOTIFICATION_SETTING = "NotificationSetting";
    public static final String COL_NOTIFICATION_SETTING_ENABLE = "NotificationSetting_Enable";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createRepeatTimeTableStatement =
                "CREATE TABLE IF NOT EXISTS " + TB_REPEAT_TIME +
                " (" +
                    COL_REPEAT_TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_REPEAT_TIME_NAME + " TEXT NOT NULL, " +
                    COL_REPEAT_TIME_VALUE + " INTEGER NOT NULL DEFAULT 0" +
                ");";

        String createEventTableStatement =
                "CREATE TABLE IF NOT EXISTS " + TB_EVENT +
                " (" +
                    COL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EVENT_TITLE + " TEXT NOT NULL, " +
                    COL_EVENT_DESCRIPTION + " TEXT, " +
                    COL_EVENT_WHEN + " TEXT NOT NULL, " +
                    COL_EVENT_HAS_REPEAT_TIME + " INTEGER NOT NULL DEFAULT 0, " +
                    COL_EVENT_REPEAT_TIME_ID + " INTEGER NOT NULL DEFAULT 1, " +
                    COL_EVENT_END + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + COL_EVENT_REPEAT_TIME_ID + ") " +
                        "REFERENCES " + TB_REPEAT_TIME + "(" + COL_REPEAT_TIME_ID + ")" +
                ");";


        String createDayOfWeekTableStatement =
                "CREATE TABLE IF NOT EXISTS " + TB_DAY_OF_WEEK +
                " (" +
                    COL_DAY_OF_WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DAY_OF_WEEK_NAME + " TEXT NOT NULL" +
                ");";

        String createRepeatDayTableStatement =
                "CREATE TABLE IF NOT EXISTS " + TB_REPEAT_DAY +
                " (" +
                    COL_REPEAT_DAY_EVENT_ID + " INTEGER, " +
                    COL_REPEAT_DAY_DAY_OF_WEEK_ID + " INTEGER, " +
                    "PRIMARY KEY (" + COL_REPEAT_DAY_EVENT_ID + ", " + COL_REPEAT_DAY_DAY_OF_WEEK_ID + "), " +
                    "FOREIGN KEY (" + COL_REPEAT_DAY_EVENT_ID + ") " +
                        "REFERENCES " + TB_EVENT + "(" + COL_EVENT_ID + ") " +
                            "ON DELETE CASCADE " +
                            "ON UPDATE NO ACTION," +
                    "FOREIGN KEY (" + COL_REPEAT_DAY_DAY_OF_WEEK_ID + ") " +
                        "REFERENCES " + TB_DAY_OF_WEEK + "(" + COL_DAY_OF_WEEK_ID + ") " +
                            "ON DELETE CASCADE " +
                            "ON UPDATE NO ACTION" +
                ");";

        String createNotificationSettingTableStatement =
                "CREATE TABLE IF NOT EXISTS " + TB_NOTIFICATION_SETTING +
                        " (" +
                            COL_NOTIFICATION_SETTING_ENABLE + " INTEGER PRIMARY KEY" +
                        ");";

        db.execSQL(createEventTableStatement);
        db.execSQL(createRepeatTimeTableStatement);
        db.execSQL(createDayOfWeekTableStatement);
        db.execSQL(createRepeatDayTableStatement);
        db.execSQL(createNotificationSettingTableStatement);

        InsertRepeatTimeDefaultRecords(db);
        InsertDayOfWeekDefaultRecords(db);
        InsertEventDefaultRecords(db);
        InsertRepeatDayDefaultRecords(db);
        InitNotificationSetting(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_REPEAT_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TB_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TB_DAY_OF_WEEK);
        db.execSQL("DROP TABLE IF EXISTS " + TB_REPEAT_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NOTIFICATION_SETTING);
        onCreate(db);
    }


    //RepeatTime
    public void InsertRepeatTime(RepeatTimeModel repeatTimeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_REPEAT_TIME_NAME, repeatTimeModel.getName());
        cv.put(COL_REPEAT_TIME_VALUE, repeatTimeModel.getValue());

        db.insert(TB_REPEAT_TIME, null, cv);
    }

    private void InsertRepeatTime(SQLiteDatabase db, RepeatTimeModel repeatTimeModel) {
        ContentValues cv = new ContentValues();

        cv.put(COL_REPEAT_TIME_NAME, repeatTimeModel.getName());
        cv.put(COL_REPEAT_TIME_VALUE, repeatTimeModel.getValue());

        db.insert(TB_REPEAT_TIME, null, cv);
    }

    public List<RepeatTimeModel> GetAllRepeatTimes() {
        List<RepeatTimeModel> repeatTimeModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_REPEAT_TIME, null, null, null, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    RepeatTimeModel repeatTimeModel = new RepeatTimeModel();
                    repeatTimeModel.setId(cur.getInt(cur.getColumnIndex(COL_REPEAT_TIME_ID)));
                    repeatTimeModel.setName(cur.getString(cur.getColumnIndex(COL_REPEAT_TIME_NAME)));
                    repeatTimeModel.setValue(cur.getInt(cur.getColumnIndex(COL_REPEAT_TIME_VALUE)));

                    repeatTimeModelList.add(repeatTimeModel);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return repeatTimeModelList;
    }

    public RepeatTimeModel GetRepeatTimeByID(int id) {
        RepeatTimeModel repeatTimeModel = new RepeatTimeModel();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_REPEAT_TIME, null, COL_REPEAT_TIME_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    repeatTimeModel.setId(cur.getInt(cur.getColumnIndex(COL_REPEAT_TIME_ID)));
                    repeatTimeModel.setName(cur.getString(cur.getColumnIndex(COL_REPEAT_TIME_NAME)));
                    repeatTimeModel.setValue(cur.getInt(cur.getColumnIndex(COL_REPEAT_TIME_VALUE)));
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return repeatTimeModel;
    }

    //DayOfWeek
    public void InsertDayOfWeek(DayOfWeekModel dayOfWeekModel) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(COL_DAY_OF_WEEK_ID, dayOfWeekModel.getId());
        cv.put(COL_DAY_OF_WEEK_NAME, dayOfWeekModel.getName());

        db.insert(TB_DAY_OF_WEEK, null, cv);
    }

    private void InsertDayOfWeek(SQLiteDatabase db, DayOfWeekModel dayOfWeekModel) {
        ContentValues cv = new ContentValues();

        cv.put(COL_DAY_OF_WEEK_ID, dayOfWeekModel.getId());
        cv.put(COL_DAY_OF_WEEK_NAME, dayOfWeekModel.getName());

        db.insert(TB_DAY_OF_WEEK, null, cv);
    }


    public List<DayOfWeekModel> GetAllDayOfWeek() {
        List<DayOfWeekModel> dayOfWeekModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_DAY_OF_WEEK, null, null, null, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    DayOfWeekModel dayOfWeekModel = new DayOfWeekModel();

                    dayOfWeekModel.setId(cur.getInt(cur.getColumnIndex(COL_DAY_OF_WEEK_ID)));
                    dayOfWeekModel.setName(cur.getString(cur.getColumnIndex(COL_DAY_OF_WEEK_NAME)));

                    dayOfWeekModels.add(dayOfWeekModel);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return dayOfWeekModels;
    }

    public DayOfWeekModel GetDayOfWeekByID(int id) {
        DayOfWeekModel dayOfWeekModel = new DayOfWeekModel();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_DAY_OF_WEEK, null, COL_DAY_OF_WEEK_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    dayOfWeekModel.setId(cur.getInt(cur.getColumnIndex(COL_DAY_OF_WEEK_ID)));
                    dayOfWeekModel.setName(cur.getString(cur.getColumnIndex(COL_DAY_OF_WEEK_NAME)));
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return dayOfWeekModel;
    }

    //RepeatDay
    public void InsertRepeatDay(int eventID, int dayOfWeekID) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(COL_REPEAT_DAY_EVENT_ID, eventID);
        cv.put(COL_REPEAT_DAY_DAY_OF_WEEK_ID, dayOfWeekID);

        db.insert(TB_REPEAT_DAY, null, cv);
    }

    private void InsertRepeatDay(SQLiteDatabase db, int eventID, int dayOfWeekID) {
        ContentValues cv = new ContentValues();

        cv.put(COL_REPEAT_DAY_EVENT_ID, eventID);
        cv.put(COL_REPEAT_DAY_DAY_OF_WEEK_ID, dayOfWeekID);

        db.insert(TB_REPEAT_DAY, null, cv);
    }

    public void DeleteRepeatDayByEventID(int eventID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_REPEAT_DAY, COL_REPEAT_DAY_EVENT_ID + " = ?", new String[] {String.valueOf(eventID)});
    }

    public List<DayOfWeekModel> GetDayOfWeekByEventID(int eventId) {
        List<DayOfWeekModel> dayOfWeekModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_REPEAT_DAY, null, COL_REPEAT_DAY_EVENT_ID + " = ?", new String[] { String.valueOf(eventId) }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    DayOfWeekModel dayOfWeekModel = GetDayOfWeekByID(cur.getInt(cur.getColumnIndex(COL_REPEAT_DAY_DAY_OF_WEEK_ID)));

                    dayOfWeekModelList.add(dayOfWeekModel);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return dayOfWeekModelList;
    }

    public List<EventModel> GetEventByDayOfWeekID(int dayOfWeekID) {
        List<EventModel> eventModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_REPEAT_DAY, null, COL_REPEAT_DAY_DAY_OF_WEEK_ID + " = ?", new String[] { String.valueOf(dayOfWeekID) }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    EventModel eventModel = GetEventByID(cur.getInt(cur.getColumnIndex(COL_REPEAT_DAY_EVENT_ID)));

                    eventModelList.add(eventModel);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return eventModelList;
    }

    //EventModel
    public void InsertEvent(EventModel eventModel) {

        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COL_EVENT_TITLE, eventModel.getTitle());
            cv.put(COL_EVENT_DESCRIPTION, eventModel.getDescription());
            cv.put(COL_EVENT_WHEN, eventModel.getWhen());
            cv.put(COL_EVENT_HAS_REPEAT_TIME, eventModel.getHasRepeatTime());
            cv.put(COL_EVENT_REPEAT_TIME_ID, eventModel.getRepeatTimeId());
            cv.put(COL_EVENT_END, eventModel.getEnd());

        long newlyID = db.insert(TB_EVENT, null, cv);

        for (DayOfWeekModel dayOfWeekModel : eventModel.getRepeatDaysList()) {
            InsertRepeatDay((int)newlyID, dayOfWeekModel.getId());
        }
    }

    private void InsertEvent(SQLiteDatabase db, EventModel eventModel) {
        ContentValues cv = new ContentValues();

        cv.put(COL_EVENT_TITLE, eventModel.getTitle());
        cv.put(COL_EVENT_DESCRIPTION, eventModel.getDescription());
        cv.put(COL_EVENT_WHEN, eventModel.getWhen());
        cv.put(COL_EVENT_HAS_REPEAT_TIME, eventModel.getHasRepeatTime());
        cv.put(COL_EVENT_REPEAT_TIME_ID, eventModel.getRepeatTimeId());
        cv.put(COL_EVENT_END, eventModel.getEnd());

        db.insert(TB_EVENT, null, cv);
    }

    public List<EventModel> GetAllEvents() {
        List<EventModel> eventModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_EVENT, null, null, null, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    EventModel eventModel = new EventModel();
                    eventModel.setId(cur.getInt(cur.getColumnIndex(COL_EVENT_ID)));
                    eventModel.setTitle(cur.getString(cur.getColumnIndex(COL_EVENT_TITLE)));
                    eventModel.setDescription(cur.getString(cur.getColumnIndex(COL_EVENT_DESCRIPTION)));
                    eventModel.setWhen(cur.getString(cur.getColumnIndex(COL_EVENT_WHEN)));
                    eventModel.setHasRepeatTime(cur.getInt(cur.getColumnIndex(COL_EVENT_HAS_REPEAT_TIME)) == 1);
                    eventModel.setRepeatTimeId(cur.getInt(cur.getColumnIndex(COL_EVENT_REPEAT_TIME_ID)));
                    eventModel.setEnd(cur.getString(cur.getColumnIndex(COL_EVENT_END)));
                    eventModel.setRepeatTimeModel(GetRepeatTimeByID(eventModel.getRepeatTimeId()));
                    eventModel.setRepeatDaysList(GetDayOfWeekByEventID(eventModel.getId()));
                    eventModel.setNotifyTime(eventModel.getWhen());

                    eventModelList.add(eventModel);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return eventModelList;
    }

    public EventModel GetEventByID(int id) {
        EventModel eventModel = new EventModel();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_EVENT, null, COL_EVENT_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    eventModel.setId(cur.getInt(cur.getColumnIndex(COL_EVENT_ID)));
                    eventModel.setTitle(cur.getString(cur.getColumnIndex(COL_EVENT_TITLE)));
                    eventModel.setDescription(cur.getString(cur.getColumnIndex(COL_EVENT_DESCRIPTION)));
                    eventModel.setWhen(cur.getString(cur.getColumnIndex(COL_EVENT_WHEN)));
                    eventModel.setHasRepeatTime(cur.getInt(cur.getColumnIndex(COL_EVENT_HAS_REPEAT_TIME)) == 1);
                    eventModel.setRepeatTimeId(cur.getInt(cur.getColumnIndex(COL_EVENT_REPEAT_TIME_ID)));
                    eventModel.setEnd(cur.getString(cur.getColumnIndex(COL_EVENT_END)));
                    eventModel.setRepeatTimeModel(GetRepeatTimeByID(eventModel.getRepeatTimeId()));
                    eventModel.setRepeatDaysList(GetDayOfWeekByEventID(eventModel.getId()));
                    eventModel.setNotifyTime(eventModel.getWhen());

                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return eventModel;
    }

    public EventModel GetEventByTitle(String title) {
        EventModel eventModel = new EventModel();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_EVENT, null, COL_EVENT_TITLE + " = ?", new String[] { title }, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    eventModel.setId(cur.getInt(cur.getColumnIndex(COL_EVENT_ID)));
                    eventModel.setTitle(cur.getString(cur.getColumnIndex(COL_EVENT_TITLE)));
                    eventModel.setDescription(cur.getString(cur.getColumnIndex(COL_EVENT_DESCRIPTION)));
                    eventModel.setWhen(cur.getString(cur.getColumnIndex(COL_EVENT_WHEN)));
                    eventModel.setHasRepeatTime(cur.getInt(cur.getColumnIndex(COL_EVENT_HAS_REPEAT_TIME)) == 1);
                    eventModel.setRepeatTimeId(cur.getInt(cur.getColumnIndex(COL_EVENT_REPEAT_TIME_ID)));
                    eventModel.setEnd(cur.getString(cur.getColumnIndex(COL_EVENT_END)));
                    eventModel.setRepeatTimeModel(GetRepeatTimeByID(eventModel.getRepeatTimeId()));
                    eventModel.setRepeatDaysList(GetDayOfWeekByEventID(eventModel.getId()));
                    eventModel.setNotifyTime(eventModel.getWhen());

                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();
        return eventModel;
    }

    public void UpdateEvent(EventModel eventModel) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(COL_EVENT_TITLE, eventModel.getTitle());
        cv.put(COL_EVENT_DESCRIPTION, eventModel.getDescription());
        cv.put(COL_EVENT_WHEN, eventModel.getWhen());
        cv.put(COL_EVENT_HAS_REPEAT_TIME, eventModel.getHasRepeatTime());
        cv.put(COL_EVENT_REPEAT_TIME_ID, eventModel.getRepeatTimeId());
        cv.put(COL_EVENT_END, eventModel.getEnd());

        db.update(TB_EVENT, cv, COL_EVENT_ID + " = ?", new String[] { String.valueOf(eventModel.getId()) });

        DeleteRepeatDayByEventID(eventModel.getId());

        for (DayOfWeekModel dayOfWeekModel : eventModel.getRepeatDaysList()) {
            InsertRepeatDay(eventModel.getId(), dayOfWeekModel.getId());
        }
    }

    public void DeleteEvent(EventModel eventModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        DeleteRepeatDayByEventID(eventModel.getId());
        db.delete(TB_EVENT, COL_EVENT_ID + " = ?", new String[] { String.valueOf(eventModel.getId()) });
    }

    private void InitNotificationSetting(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(COL_NOTIFICATION_SETTING_ENABLE, 0);

        db.insert(TB_NOTIFICATION_SETTING, null, cv);
    }

    public boolean GetNotificationEnable() {
        boolean enable = false;

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cur = db.query(TB_NOTIFICATION_SETTING, null, null, null, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    enable = (cur.getInt(cur.getColumnIndex(COL_NOTIFICATION_SETTING_ENABLE)) == 1);
                } while (cur.moveToNext());
            }

        }
        cur.close();
        db.endTransaction();

        return enable;
    }

    public void UpdateNotificationEnable(boolean enable) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(COL_NOTIFICATION_SETTING_ENABLE, enable);

        db.update(TB_NOTIFICATION_SETTING, cv, null, null);
    }


    private void InsertDayOfWeekDefaultRecords(SQLiteDatabase db) {
        DayOfWeekModel dayOfWeekModel = new DayOfWeekModel();
        dayOfWeekModel.setId(1);
        dayOfWeekModel.setName("Monday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(2);
        dayOfWeekModel.setName("Tuesday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(3);
        dayOfWeekModel.setName("Wednesday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(4);
        dayOfWeekModel.setName("Thursday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(5);
        dayOfWeekModel.setName("Friday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(6);
        dayOfWeekModel.setName("Saturday");
        InsertDayOfWeek(db, dayOfWeekModel);

        dayOfWeekModel.setId(7);
        dayOfWeekModel.setName("Sunday");
        InsertDayOfWeek(db, dayOfWeekModel);
    }

    private void InsertRepeatTimeDefaultRecords(SQLiteDatabase db) {
        RepeatTimeModel repeatTimeModel = new RepeatTimeModel();

        repeatTimeModel.setName("1 minute");
        repeatTimeModel.setValue(60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("5 minutes");
        repeatTimeModel.setValue(5 * 60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("15 minutes");
        repeatTimeModel.setValue(15 * 60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("30 minutes");
        repeatTimeModel.setValue(30 * 60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("1 hour");
        repeatTimeModel.setValue(60 * 60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("2 hours");
        repeatTimeModel.setValue(120 * 60);
        InsertRepeatTime(db, repeatTimeModel);

        repeatTimeModel.setName("3 hours");
        repeatTimeModel.setValue(180 * 60);
        InsertRepeatTime(db, repeatTimeModel);
    }

    private void InsertEventDefaultRecords(SQLiteDatabase db) {
        EventModel eventModel = new EventModel();

        eventModel.setTitle("Drink Water");
        eventModel.setDescription("Important!");
        eventModel.setWhen("09:00 AM");
        eventModel.setHasRepeatTime(true);
        eventModel.setRepeatTimeId(5);
        eventModel.setEnd("04:30 PM");
        InsertEvent(db, eventModel);

        eventModel.setTitle("Work out");
        eventModel.setDescription("Chest - Back - Legs - Arms - Shoulder - DeadLift");
        eventModel.setWhen("05:30 PM");
        eventModel.setHasRepeatTime(false);
        eventModel.setRepeatTimeId(1);
        eventModel.setEnd("00:00 AM");
        InsertEvent(db, eventModel);

        eventModel.setTitle("Breakfast");
        eventModel.setDescription("");
        eventModel.setWhen("08:30 AM");
        eventModel.setHasRepeatTime(false);
        eventModel.setRepeatTimeId(1);
        eventModel.setEnd("00:00 AM");
        InsertEvent(db, eventModel);

        eventModel.setTitle("Lunch");
        eventModel.setDescription("");
        eventModel.setWhen("01:30 PM");
        eventModel.setHasRepeatTime(false);
        eventModel.setRepeatTimeId(1);
        eventModel.setEnd("00:00 AM");
        InsertEvent(db, eventModel);

        eventModel.setTitle("Dinner");
        eventModel.setDescription("");
        eventModel.setWhen("07:30 PM");
        eventModel.setHasRepeatTime(false);
        eventModel.setRepeatTimeId(1);
        eventModel.setEnd("00:00 AM");
        InsertEvent(db, eventModel);

        eventModel.setTitle("Math Exam");
        eventModel.setDescription("");
        eventModel.setWhen("10:30 AM");
        eventModel.setHasRepeatTime(false);
        eventModel.setRepeatTimeId(1);
        eventModel.setEnd("00:00 AM");
        InsertEvent(db, eventModel);
    }

    private void InsertRepeatDayDefaultRecords(SQLiteDatabase db) {
        InsertRepeatDay(db, 1, 1);
        InsertRepeatDay(db, 1, 2);
        InsertRepeatDay(db, 1, 3);
        InsertRepeatDay(db, 1, 4);
        InsertRepeatDay(db, 1, 5);
        InsertRepeatDay(db, 1, 6);
        InsertRepeatDay(db, 1, 7);

        InsertRepeatDay(db, 2, 1);
        InsertRepeatDay(db, 2, 2);
        InsertRepeatDay(db, 2, 3);
        InsertRepeatDay(db, 2, 4);
        InsertRepeatDay(db, 2, 5);
        InsertRepeatDay(db, 2, 6);

        InsertRepeatDay(db, 3, 1);
        InsertRepeatDay(db, 3, 2);
        InsertRepeatDay(db, 3, 3);
        InsertRepeatDay(db, 3, 4);
        InsertRepeatDay(db, 3, 5);
        InsertRepeatDay(db, 3, 6);
        InsertRepeatDay(db, 3, 7);

        InsertRepeatDay(db, 4, 1);
        InsertRepeatDay(db, 4, 2);
        InsertRepeatDay(db, 4, 3);
        InsertRepeatDay(db, 4, 4);
        InsertRepeatDay(db, 4, 5);
        InsertRepeatDay(db, 4, 6);
        InsertRepeatDay(db, 4, 7);

        InsertRepeatDay(db, 5, 1);
        InsertRepeatDay(db, 5, 2);
        InsertRepeatDay(db, 5, 3);
        InsertRepeatDay(db, 5, 4);
        InsertRepeatDay(db, 5, 5);
        InsertRepeatDay(db, 5, 6);
        InsertRepeatDay(db, 5, 7);

        InsertRepeatDay(db, 6, 1);

    }
}
