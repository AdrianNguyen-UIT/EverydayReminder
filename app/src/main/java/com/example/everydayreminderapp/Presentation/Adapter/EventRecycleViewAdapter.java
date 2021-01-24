package com.example.everydayreminderapp.Presentation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everydayreminderapp.Presentation.DialogFragment.DetailPopupDialogFragment;
import com.example.everydayreminderapp.Database.Model.EventModel;
import com.example.everydayreminderapp.R;

import java.util.ArrayList;
import java.util.List;

public class EventRecycleViewAdapter extends RecyclerView.Adapter<EventRecycleViewAdapter.ViewHolder> {
    private List<EventModel> eventList = new ArrayList<>();
    private Fragment fragment;

    public enum Type {
        over,
        next,
        upcoming,
        normal
    }
    private Type type;

    public EventRecycleViewAdapter(Fragment fragment, Type type) {
        this.fragment = fragment;
        this.type = type;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cardview, parent, false);
        CardView cardView = view.findViewById(R.id.event_cardview);

        switch (type) {
            case next:
                cardView.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.nextCardBackgroundColor));
                break;
            case over:
                cardView.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.overCardBackgroundColor));
                break;
            case upcoming:
                cardView.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.upcomingCardBackgroundColor));
                break;
            case normal:
                cardView.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.normalCardBackgroundColor));
            default:
        }

        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final EventModel event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.notifyTime.setText(event.getNotifyTime());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager =  ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                DetailPopupDialogFragment detailPopupDialogFragment = new DetailPopupDialogFragment(event, type);
                detailPopupDialogFragment.show(manager, "Detail Popup Dialog Fragment");

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEvents(List<EventModel> eventList) {
        this.eventList.clear();
        this.eventList.addAll(eventList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView notifyTime;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.EventTitleText);
            notifyTime = view.findViewById(R.id.EventTimeText);
            cardView = view.findViewById(R.id.event_cardview);
        }
    }
}
