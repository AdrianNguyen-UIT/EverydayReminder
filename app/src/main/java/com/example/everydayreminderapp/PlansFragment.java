package com.example.everydayreminderapp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everydayreminderapp.Adapter.EventRecycleViewAdapter;
import com.example.everydayreminderapp.Database.DatabaseHelper;
import com.example.everydayreminderapp.DialogFragment.AddEventDialogFragment;
import com.example.everydayreminderapp.DialogFragment.EditEventDialogFragment;
import com.example.everydayreminderapp.Model.DayOfWeekModel;
import com.example.everydayreminderapp.Model.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlansFragment extends Fragment implements AddEventDialogFragment.OnEventAddedListener, EditEventDialogFragment.OnEventEditedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlansFragment newInstance(String param1, String param2) {
        PlansFragment fragment = new PlansFragment();
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

    private DateTimeFormatter formatter;
    private List<EventModel> eventModelList;
    public EventRecycleViewAdapter eventRecycleViewAdapter;
    private RecyclerView recyclerView;

    private Spinner spinner;
    private int dayOfWeekIndex;
    private DatabaseHelper databaseHelper;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        List<DayOfWeekModel> dayOfWeekModelList = databaseHelper.GetAllDayOfWeek();
        List<String> value = new ArrayList<>();
        for (DayOfWeekModel dayOfWeekModel : dayOfWeekModelList) {
            value.add(dayOfWeekModel.getName());
        }
        ArrayList<String> arrayList = new ArrayList<>(value);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_textview, arrayList);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner = (Spinner)view.findViewById(R.id.dayOfWeekSpinner);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventModelList = databaseHelper.GetEventByDayOfWeekID(position + 1);
                SortEventList();
                UpdateRecycleViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.addEventFloatingButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAddEventDialog();
            }
        });

        eventRecycleViewAdapter = new EventRecycleViewAdapter(this, EventRecycleViewAdapter.Type.normal);
        recyclerView = view.findViewById(R.id.daysRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(eventRecycleViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        formatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        dayOfWeekIndex = dayOfWeek.getValue();

        spinner.setSelection(dayOfWeekIndex - 1);

        return view;
    }

    private void SortEventList() {

        Collections.sort(eventModelList, new Comparator<EventModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(EventModel o1, EventModel o2) {
                LocalTime o1Time = LocalTime.parse(o1.getNotifyTime(), formatter);
                LocalTime o2Time = LocalTime.parse(o2.getNotifyTime(), formatter);
                return o1Time.compareTo(o2Time);
            }
        });
    }

    private void ShowAddEventDialog() {
        AddEventDialogFragment addEventDialogFragment = new AddEventDialogFragment();
        addEventDialogFragment.show(getChildFragmentManager(), "Add Event Dialog Fragment");
        addEventDialogFragment.AddOnEventAddedListener(this);

    }

    private void UpdateRecycleViews() {
        eventRecycleViewAdapter.setEvents(eventModelList);
        eventRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        eventModelList = databaseHelper.GetEventByDayOfWeekID(dayOfWeekIndex);
        SortEventList();
        UpdateRecycleViews();

        listener.refreshFrag();
    }

    @Override
    public void cancelRefresh() {
        eventRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void editRefresh() {
        refresh();
    }

    public interface OnCreateAddEventDialogListener {
        public void refreshFrag();
    }

    private OnCreateAddEventDialogListener listener;

    public void AddOnEventAddedListenerListener(OnCreateAddEventDialogListener listener) {
        this.listener = listener;
    }

    public void RemoveOnEventAddedListenerListener() {
        this.listener = null;
    }

    public void DeleteEvent(int position) {
        databaseHelper.DeleteEvent(eventModelList.get(position));
        refresh();
    }

    public void ShowEditDialog(int position) {
        EditEventDialogFragment editEventDialogFragment = new EditEventDialogFragment(eventModelList.get(position));
        editEventDialogFragment.show(getChildFragmentManager(), "Edit Event Dialog Fragment");
        editEventDialogFragment.AddOnEventEditedListener(this);
    }
}