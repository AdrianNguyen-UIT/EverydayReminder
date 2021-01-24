package com.example.everydayreminderapp.Presentation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everydayreminderapp.Database.DatabaseHelper;
import com.example.everydayreminderapp.Database.Model.EventModel;
import com.example.everydayreminderapp.Presentation.Adapter.EventRecycleViewAdapter;
import com.example.everydayreminderapp.R;
import com.example.everydayreminderapp.Utility.EventHandler;
import com.example.everydayreminderapp.Utility.ReminderBroadcast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class TodayEventsFragment extends Fragment implements PlansFragment.OnCreateAddEventDialogListener, ReminderBroadcast.OnNotifyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayEventsFragment newInstance(String param1, String param2) {
        TodayEventsFragment fragment = new TodayEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private boolean notificationEnable;
    private boolean[] isExpands;
    private DateTimeFormatter formatter;
    private List<EventModel> eventModelList;


    private EventRecycleViewAdapter pastEventRecycleViewAdapter;
    private EventRecycleViewAdapter nextEventRecycleViewAdapter;
    private EventRecycleViewAdapter upcomingEventRecycleViewAdapter;

    private RecyclerView overRecyclerView;
    private RecyclerView nextRecyclerView;
    private RecyclerView upcomingRecyclerView;

    private DatabaseHelper databaseHelper;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_events, container, false);
        formatter = DateTimeFormatter.ofPattern("hh:mm a");
        databaseHelper = new DatabaseHelper(requireContext());

        isExpands = new boolean[3];
        isExpands[0] = true;
        isExpands[1] = true;
        isExpands[2] = true;

        pastEventRecycleViewAdapter = new EventRecycleViewAdapter(this, EventRecycleViewAdapter.Type.over);
        nextEventRecycleViewAdapter = new EventRecycleViewAdapter(this, EventRecycleViewAdapter.Type.next);
        upcomingEventRecycleViewAdapter = new EventRecycleViewAdapter(this, EventRecycleViewAdapter.Type.upcoming);

        overRecyclerView = view.findViewById(R.id.overRecycleView);
        nextRecyclerView = view.findViewById(R.id.nextRecycleView);
        upcomingRecyclerView = view.findViewById(R.id.upcomingRecycleView);

        overRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        overRecyclerView.setAdapter(pastEventRecycleViewAdapter);
        nextRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        nextRecyclerView.setAdapter(nextEventRecycleViewAdapter);
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        upcomingRecyclerView.setAdapter(upcomingEventRecycleViewAdapter);

        refreshFrag();

        final ImageButton[] buttons = new ImageButton[3];
        buttons[0] = (ImageButton)view.findViewById(R.id.overButton);
        buttons[1] = (ImageButton)view.findViewById(R.id.nextButton);
        buttons[2] = (ImageButton)view.findViewById(R.id.upcomingButton);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpands[0] = !isExpands[0];
                if (isExpands[0]) {
                    buttons[0].setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    overRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    buttons[0].setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    overRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpands[1] = !isExpands[1];
                if (isExpands[1]) {
                    buttons[1].setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    nextRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    buttons[1].setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    nextRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpands[2] = !isExpands[2];
                if (isExpands[2]) {
                    buttons[2].setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    upcomingRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    buttons[2].setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    upcomingRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        notificationEnable = databaseHelper.GetNotificationEnable();
        final FloatingActionButton notificationButton = view.findViewById(R.id.notificationButton);
        if (notificationEnable) {
            notificationButton.setImageResource(R.drawable.ic_baseline_notification_on_32);
            notificationButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.notificationOn));
        }
        else {
            notificationButton.setImageResource(R.drawable.ic_baseline_notification_off_32);
            notificationButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.notificationOff));
        }

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationEnable = !notificationEnable;
                databaseHelper.UpdateNotificationEnable(notificationEnable);

                if (notificationEnable) {
                    notificationButton.setImageResource(R.drawable.ic_baseline_notification_on_32);
                    notificationButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.notificationOn));
                    refreshFrag();
                }
                else {
                    notificationButton.setImageResource(R.drawable.ic_baseline_notification_off_32);
                    notificationButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.notificationOff));
                    CancelAlarm();
                }
            }
        });
        ReminderBroadcast.AddOnNotifyListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateEventCardView() {
        List<EventModel> overEventList = EventHandler.GetOverEventList();
        pastEventRecycleViewAdapter.setEvents(overEventList);
        pastEventRecycleViewAdapter.notifyDataSetChanged();

        List<EventModel> nextEventList = EventHandler.GetNextEventList();
        nextEventRecycleViewAdapter.setEvents(nextEventList);
        nextEventRecycleViewAdapter.notifyDataSetChanged();

        List<EventModel> upcomingEventList = EventHandler.GetUpcomingEventList();
        upcomingEventRecycleViewAdapter.setEvents(upcomingEventList);
        upcomingEventRecycleViewAdapter.notifyDataSetChanged();
    }

    private void RetrieveEventModelListByDay() {
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        int dayOfWeekIndex = dayOfWeek.getValue();

        eventModelList = databaseHelper.GetEventByDayOfWeekID(dayOfWeekIndex);
        EventHandler.SetEventModelList(eventModelList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void refreshFrag() {
        RetrieveEventModelListByDay();
        CreateEventCardView();
        NotifyNextEvent();

    }

    public void RegisterOnCreateAddEventDialogListener(PlansFragment fragment) {
        fragment.AddOnEventAddedListenerListener(this);
    }

    private void StartAlarm(String time, LocalDate date) {

        if (time.length() == 0)
            return;

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        LocalTime notifyTime = LocalTime.parse(time, formatter);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, date.getYear());
        c.set(Calendar.MONTH, date.getMonthValue() - 1);
        c.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, notifyTime.getHour());
        c.set(Calendar.MINUTE, notifyTime.getMinute());
        c.set(Calendar.SECOND, 0);

        Log.i("TODAY",  time + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void CancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onNotify() {
        refreshFrag();
    }

    private void NotifyNextEvent() {
        CancelAlarm();

        if (!notificationEnable)
            return;

        LocalDate localDate = LocalDate.now();

        if (EventHandler.GetHasNoNextEvent()) {
            int dayOfWeekIndex;
            boolean stop = false;
            List<EventModel> nextDayEventModelList;

            for (int index = 0; index < 6; ++index) {
                if (stop) {
                    break;
                }

                localDate = localDate.plusDays(1);
                DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
                dayOfWeekIndex = dayOfWeek.getValue();

                nextDayEventModelList = databaseHelper.GetEventByDayOfWeekID(dayOfWeekIndex);
                EventHandler.SetEventModelList(nextDayEventModelList);

                if (nextDayEventModelList.size() != 0) {
                    stop = true;
                    StartAlarm(nextDayEventModelList.get(0).getNotifyTime(), localDate);
                }
            }

            if (!stop) {
                if (eventModelList.size() != 0) {
                    localDate = LocalDate.now();
                    StartAlarm(eventModelList.get(0).getNotifyTime(), localDate);
                }
            }
        }
        else {
                StartAlarm(EventHandler.GetNextEventList().get(0).getNotifyTime(), localDate);
        }
    }
}