package com.example.everydayreminderapp.Model;

import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private int id;
    private String title;
    private String description;
    private String when;
    private boolean hasRepeatTime;
    private int repeatTimeId;
    private String end;

    private RepeatTimeModel repeatTimeModel;
    private List<DayOfWeekModel> repeatDaysList;
    private String notifyTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public boolean getHasRepeatTime() {
        return hasRepeatTime;
    }
    public void setHasRepeatTime(boolean hasRepeatTime) {
        this.hasRepeatTime = hasRepeatTime;
    }

    public int getRepeatTimeId() {
        return repeatTimeId;
    }

    public void setRepeatTimeId(int repeatTimeId) {
        this.repeatTimeId = repeatTimeId;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public RepeatTimeModel getRepeatTimeModel() {
        return repeatTimeModel;
    }

    public void setRepeatTimeModel(RepeatTimeModel repeatTimeModel) {
        this.repeatTimeModel = repeatTimeModel;
    }

    public List<DayOfWeekModel> getRepeatDaysList() {
        return repeatDaysList;
    }

    public void setRepeatDaysList(List<DayOfWeekModel> repeatDaysList) {
        this.repeatDaysList = repeatDaysList;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }
}
