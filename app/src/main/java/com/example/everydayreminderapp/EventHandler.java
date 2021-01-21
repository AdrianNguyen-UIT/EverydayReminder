package com.example.everydayreminderapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.everydayreminderapp.Model.EventModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
class EventHandler {

    private static ArrayList<Integer> notifyIndexList = new ArrayList<>();
    private static List<EventModel> eventModelList = new ArrayList<>();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    private static boolean hasNoNextEvent = false;

    public static void setEventModelList(List<EventModel> p_EventModelList) {
        eventModelList.clear();
        eventModelList.addAll(p_EventModelList);

        InitEventModelList();
        CategorizeEvent();
    }

    public static boolean GetHasNoNextEvent () {
        return hasNoNextEvent;
    }

    private static void CategorizeEvent() {
        SortEventList();

        LocalTime now = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());

        notifyIndexList.clear();

        for (int index = 0; index < eventModelList.size(); ++index) {
            LocalTime notifyTime = LocalTime.parse(eventModelList.get(index).getNotifyTime(), formatter);
            if (notifyTime.compareTo(now) > 0) {
                notifyIndexList.add(index);
                break;
            }
        }

        if (notifyIndexList.size() == 0) {
            hasNoNextEvent = true;
        }
        else {
            hasNoNextEvent = false;
            LocalTime firstNotifyTime = LocalTime.parse(eventModelList.get(notifyIndexList.get(0)).getNotifyTime(), formatter);
            for (int index = notifyIndexList.get(0) + 1; index < eventModelList.size(); ++index) {

                LocalTime notifyTime = LocalTime.parse(eventModelList.get(index).getNotifyTime(), formatter);
                if (notifyTime.compareTo(firstNotifyTime) == 0) {
                    notifyIndexList.add(index);
                }
            }

        }
    }

    private static void InitEventModelList() {
        LocalTime now = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());

        for (EventModel eventModel : eventModelList) {
            if (eventModel.getHasRepeatTime()) {
                LocalTime endTime = LocalTime.parse(eventModel.getEnd(), formatter);
                LocalTime notifyTime = LocalTime.parse(eventModel.getNotifyTime(), formatter);

                if (now.compareTo(endTime) <= 0) {
                    while (notifyTime.compareTo(now) <= 0) {
                        notifyTime = notifyTime.plusSeconds(eventModel.getRepeatTimeModel().getValue());
                    }
                }
                else {
                    while (notifyTime.compareTo(endTime) <= 0) {
                        notifyTime = notifyTime.plusSeconds(eventModel.getRepeatTimeModel().getValue());
                    }
                    notifyTime = notifyTime.minusSeconds(eventModel.getRepeatTimeModel().getValue());
                }
                eventModel.setNotifyTime(notifyTime.format(formatter));
            }
        }
    }

    public static void RepeatCurrentEvent() {

        for (int index = 0; index < notifyIndexList.size(); ++index) {
            EventModel eventModel = eventModelList.get(notifyIndexList.get(index));

            if (eventModel.getHasRepeatTime()) {
                LocalTime endTime = LocalTime.parse(eventModel.getEnd(), formatter);
                LocalTime notifyTime = LocalTime.parse(eventModel.getNotifyTime(), formatter);
                notifyTime = notifyTime.plusSeconds(eventModel.getRepeatTimeModel().getValue());

                if (notifyTime.compareTo(endTime) >= 0) {
                    notifyTime = notifyTime.minusSeconds(eventModel.getRepeatTimeModel().getValue());
                }

                eventModel.setNotifyTime(notifyTime.format(formatter));
            }
        }

        CategorizeEvent();
    }

    private static void SortEventList() {

        Collections.sort(eventModelList, new Comparator<EventModel>() {
            @Override
            public int compare(EventModel o1, EventModel o2) {
                LocalTime o1Time = LocalTime.parse(o1.getNotifyTime(), formatter);
                LocalTime o2Time = LocalTime.parse(o2.getNotifyTime(), formatter);
                return o1Time.compareTo(o2Time);
            }
        });
    }

    public static List<EventModel> GetOverEventList() {
        List<EventModel> overEventList =  new ArrayList<>();

        if (notifyIndexList.size() != 0) {
            for (int index = 0; index < notifyIndexList.get(0); ++index) {
                overEventList.add(eventModelList.get(index));
            }
        }

        return overEventList;
    }

    public static List<EventModel> GetNextEventList() {
        List<EventModel> nextEventList =  new ArrayList<>();

        for (int index = 0; index < notifyIndexList.size(); ++index) {
            nextEventList.add(eventModelList.get(notifyIndexList.get(index)));
        }

        return nextEventList;
    }

    public static List<EventModel> GetUpcomingEventList() {
        List<EventModel> upcomingEventList =  new ArrayList<>();

        if (notifyIndexList.size() != 0) {
            for (int index = notifyIndexList.get(notifyIndexList.size() - 1) + 1; index < eventModelList.size(); ++index) {
                upcomingEventList.add(eventModelList.get(index));
            }
        }

        return upcomingEventList;
    }
}
