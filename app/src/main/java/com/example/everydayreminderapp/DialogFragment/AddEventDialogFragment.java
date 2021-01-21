package com.example.everydayreminderapp.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import android.text.format.DateFormat;
import android.widget.Toast;

import com.example.everydayreminderapp.Database.DatabaseHelper;
import com.example.everydayreminderapp.Model.DayOfWeekModel;
import com.example.everydayreminderapp.Model.EventModel;
import com.example.everydayreminderapp.Model.RepeatTimeModel;
import com.example.everydayreminderapp.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddEventDialogFragment extends DialogFragment {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView whenTimeTextView;
    private CheckBox hasRepeatTimeCheckBox;
    private TextView endTimeTextView;
    private ArrayList<Integer> dayList;
    EventModel eventModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        final DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        eventModel = new EventModel();

        //titleEditText
        {
            titleEditText = view.findViewById(R.id.titleEditText);
            titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        titleEditText.setBackgroundResource(R.drawable.addevent_item_border_selected);
                    } else {
                        if (titleEditText.getText().length() != 0) {
                            titleEditText.setBackgroundResource(R.drawable.addevent_item_border_selected);
                        }
                        else {
                            titleEditText.setBackgroundResource(R.drawable.addevent_item_border_unselected);
                        }
                    }
                }
            });
        }

        //descriptionEditText
        {
            descriptionEditText = view.findViewById(R.id.descriptionEditText);
            descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        descriptionEditText.setBackgroundResource(R.drawable.addevent_item_border_selected);
                    } else {
                        if (descriptionEditText.getText().length() != 0) {
                            descriptionEditText.setBackgroundResource(R.drawable.addevent_item_border_selected);
                        }
                        else {
                            descriptionEditText.setBackgroundResource(R.drawable.addevent_item_border_unselected);
                        }
                    }
                }
            });
        }

        //startTimeLinearLayout
        {
            final LinearLayout startTimeLinearLayout = view.findViewById(R.id.StartTimeLinearLayout);

            whenTimeTextView = view.findViewById(R.id.ShowWhenTimeTextView);
            whenTimeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int HOUR = calendar.get(Calendar.HOUR);
                    int MINUTE = calendar.get(Calendar.MINUTE);
                    boolean is24HourFormat = DateFormat.is24HourFormat(getContext());

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            startHour = hourOfDay;
                            startMinute = minute;

                            calendar.set(0, 0, 0, startHour, startMinute);

                            whenTimeTextView.setText(DateFormat.format("hh:mm aa", calendar));
                        }
                    }, HOUR, MINUTE, is24HourFormat);

                    timePickerDialog.show();
                }
            });

            whenTimeTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    startTimeLinearLayout.setBackgroundResource(R.drawable.addevent_item_border_selected);
                }
            });
        }

        //hasRepeatTimeLinearLayout
        {
            final LinearLayout hasRepeatTimeLinearLayout = view.findViewById(R.id.HasRepeatTimeLinearLayout);
            final LinearLayout hasRepeatTimePropertiesLinearLayout = view.findViewById(R.id.HasRepeatTimePropertiesLinearLayout);
            hasRepeatTimeCheckBox = view.findViewById(R.id.HasRepeatTimeCheckBox);
            hasRepeatTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        hasRepeatTimePropertiesLinearLayout.setVisibility(View.VISIBLE);
                        hasRepeatTimeLinearLayout.setBackgroundResource(R.drawable.addevent_item_border_selected);
                    }
                    else {
                        hasRepeatTimePropertiesLinearLayout.setVisibility(View.GONE);
                        hasRepeatTimeLinearLayout.setBackgroundResource(R.drawable.addevent_item_border_unselected);
                    }
                }
            });
        }

        //endTimeLinearLayout
        {
            endTimeTextView = view.findViewById(R.id.ShowEndTimeTextView);
            endTimeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int HOUR = calendar.get(Calendar.HOUR);
                    int MINUTE = calendar.get(Calendar.MINUTE);
                    boolean is24HourFormat = DateFormat.is24HourFormat(getContext());

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            endHour = hourOfDay;
                            endMinute = minute;
                            calendar.set(0, 0, 0, endHour, endMinute);

                            endTimeTextView.setText(DateFormat.format("hh:mm aa", calendar));
                        }
                    }, HOUR, MINUTE, is24HourFormat);

                    timePickerDialog.show();
                }
            });

        }

        //RepeatTimeSpinner
        {
            List<RepeatTimeModel> repeatTimeModelList = databaseHelper.GetAllRepeatTimes();
            List<String> value = new ArrayList<>();
            for (RepeatTimeModel repeatTimeModel : repeatTimeModelList) {
                value.add(repeatTimeModel.getName());
            }
            ArrayList<String> arrayList = new ArrayList<>(value);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_textview, arrayList);

            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
            final Spinner repeatSpinner = view.findViewById(R.id.RepeatSpinner);
            repeatSpinner.setAdapter(arrayAdapter);

            repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    eventModel.setRepeatTimeId(position + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //SelectRepeatDaysTextView
        {
            final TextView selectRepeatDaysTextView = view.findViewById(R.id.SelectRepeatDaysTextView);
            dayList = new ArrayList<>();

            List<DayOfWeekModel> dayOfWeekModelList = databaseHelper.GetAllDayOfWeek();
            final String[] dayArray = new String[dayOfWeekModelList.size()];

            for (int index = 0; index < dayArray.length; ++index) {
                dayArray[index] = dayOfWeekModelList.get(index).getName();
            }
            final boolean[] selectedDay = new boolean[dayArray.length];

            selectRepeatDaysTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.SelectDayHint);

                    builder.setCancelable(false);

                    builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                dayList.add(which);
                                Collections.sort(dayList);
                            }
                            else {
                                dayList.remove(which);
                            }
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int index = 0; index < dayList.size(); ++index) {
                                stringBuilder.append(dayArray[dayList.get(index)]);

                                if (index != dayList.size() - 1) {
                                    stringBuilder.append(", ");
                                }
                            }

                            selectRepeatDaysTextView.setText(stringBuilder.toString());
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int index = 0; index < selectedDay.length; ++index) {
                                selectedDay[index] = false;
                                dayList.clear();
                                selectRepeatDaysTextView.setText("");

                            }
                        }
                    });

                    builder.show();
                }
            });

            final LinearLayout selectDaysLinearLayout = view.findViewById(R.id.SelectDaysLinearLayout);

            selectRepeatDaysTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        selectDaysLinearLayout.setBackgroundResource(R.drawable.addevent_item_border_selected);
                    }
                    else {
                        selectDaysLinearLayout.setBackgroundResource(R.drawable.addevent_item_border_unselected);
                    }
                }
            });
        }

        //yesButton
        {
            ImageButton yesButton = view.findViewById(R.id.yesButton);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    if (!CheckConstraint())
                        return;

                    eventModel.setTitle(titleEditText.getText().toString());
                    eventModel.setDescription(descriptionEditText.getText().toString());
                    eventModel.setWhen(whenTimeTextView.getText().toString());
                    eventModel.setHasRepeatTime(hasRepeatTimeCheckBox.isChecked());
                    eventModel.setEnd(endTimeTextView.getText().toString());

                    List<DayOfWeekModel> dayOfWeekModels = new ArrayList<>();

                    for (int index = 0; index < dayList.size(); ++index) {
                        DayOfWeekModel dayOfWeekModel = new DayOfWeekModel();
                        dayOfWeekModel.setId(dayList.get(index) + 1);
                        dayOfWeekModels.add(dayOfWeekModel);
                    }
                    eventModel.setRepeatDaysList(dayOfWeekModels);

                    databaseHelper.InsertEvent(eventModel);

                    listener.refresh();

                    dismiss();
                }
            });
        }

        //noButton
        {
            ImageButton noButton = view.findViewById(R.id.noButton);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AppCompatDialog(getActivity(), R.style.MyFragmentDialogStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean CheckTimeConstraint(StringBuilder builder)
    {
        if (!hasRepeatTimeCheckBox.isChecked())
            return true;

        if (endTimeTextView.getText().length() == 0) {
            builder.append("\nMissing End!");
            return false;
        }
        LocalTime start = LocalTime.of(startHour, startMinute);
        LocalTime end = LocalTime.of(endHour, endMinute);

        if (end.compareTo(start) > 0) {
            return true;
        }
        else {
            builder.append("\nEnd must be GREATER than When!");
            return false;
        }
    }

    //listener
    public interface OnEventAddedListener {
        public void refresh();
    }

    private OnEventAddedListener listener;

    public void AddOnEventAddedListener(OnEventAddedListener listener) {
        this.listener = listener;
    }

    public void RemoveOnEventAddedListener() {
        this.listener = null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean CheckConstraint() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        if (titleEditText.getText().length() == 0) {
            builder.append("Missing Title!");
            ++count;
        }

        if (whenTimeTextView.getText().length() == 0) {
            builder.append("\nMissing When!");
            ++count;
        }

        if (dayList.size() == 0) {
            builder.append("\nMissing Day(s)!");
            ++count;
        }

        if (!CheckTimeConstraint(builder)) {
            ++count;
        }

        if (count != 0) {
            Toast toast = Toast.makeText(getContext(),  builder.toString(), Toast.LENGTH_SHORT);
            TextView v = (TextView) Objects.requireNonNull(toast.getView()).findViewById(android.R.id.message);
            v.setTextColor(Color.RED);
            toast.show();
            return false;
        }
        return true;
    }
}
