package com.example.everydayreminderapp.DialogFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.everydayreminderapp.Adapter.EventRecycleViewAdapter;
import com.example.everydayreminderapp.Model.DayOfWeekModel;
import com.example.everydayreminderapp.Model.EventModel;
import com.example.everydayreminderapp.R;

import java.util.Objects;

public class DetailPopupDialogFragment extends DialogFragment {
    private final EventModel eventModel;
    private final EventRecycleViewAdapter.Type type;
    public DetailPopupDialogFragment(EventModel eventModel, EventRecycleViewAdapter.Type type) {
        this.eventModel = eventModel;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_popup, container, false);

        FrameLayout banner = view.findViewById(R.id.detail_banner);
        if (type == EventRecycleViewAdapter.Type.over) {
            banner.setBackgroundResource(R.drawable.over_detail_banner);
        }
        else if (type == EventRecycleViewAdapter.Type.next) {
            banner.setBackgroundResource(R.drawable.next_detail_banner);
        }
        else if (type == EventRecycleViewAdapter.Type.upcoming) {
            banner.setBackgroundResource(R.drawable.upcoming_detail_banner);
        }
        else {
            banner.setBackgroundResource(R.drawable.normal_detail_banner);
        }

        TextView titleTextView = view.findViewById(R.id.detailTitleTextView);
        titleTextView.setText(eventModel.getTitle());

        TextView descriptionTextView = view.findViewById(R.id.detailDescriptionTextView);
        if (eventModel.getDescription().length() == 0) {
            descriptionTextView.setVisibility(View.GONE);
        }
        else {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(eventModel.getDescription());
        }


        TextView whenTextView = view.findViewById(R.id.detailWhenTextView);
        whenTextView.setText(eventModel.getWhen());

        LinearLayout hasRepeatTimeDetailLinearLayout = view.findViewById(R.id.hasRepeatTimeDetailLinearLayout);
        if (eventModel.getHasRepeatTime()) {
            hasRepeatTimeDetailLinearLayout.setVisibility(View.VISIBLE);

            TextView everyTextView = view.findViewById(R.id.detailEveryTextView);
            everyTextView.setText(eventModel.getRepeatTimeModel().getName());

            TextView endTextView = view.findViewById(R.id.detailEndTextView);
            endTextView.setText(eventModel.getEnd());
        }
        else {
            hasRepeatTimeDetailLinearLayout.setVisibility(View.GONE);
        }

        TextView mondayTextView = view.findViewById(R.id.detailMondayTextView);
        mondayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView tuesdayTextView = view.findViewById(R.id.detailTuesdayTextView);
        tuesdayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView wednesdayTextView = view.findViewById(R.id.detailWednesdayTextView);
        wednesdayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView thursdayTextView = view.findViewById(R.id.detailThursdayTextView);
        thursdayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView fridayTextView = view.findViewById(R.id.detailFridayTextView);
        fridayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView saturdayTextView = view.findViewById(R.id.detailSaturdayTextView);
        saturdayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        TextView sundayTextView = view.findViewById(R.id.detailSundayTextView);
        sundayTextView.setBackgroundResource(R.drawable.addevent_item_border_unselected);

        for (DayOfWeekModel dayOfWeekModel : eventModel.getRepeatDaysList()) {
            if (dayOfWeekModel.getId() == 1) {
                mondayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 2) {
                tuesdayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 3) {
                wednesdayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 4) {
                thursdayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 5) {
                fridayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 6) {
                saturdayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
            else if (dayOfWeekModel.getId() == 7) {
                sundayTextView.setBackgroundResource(R.drawable.addevent_item_border_selected);
            }
        }

        TextView close = view.findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }
}
