package com.example.eventusajava.screens.eventsList;

import static com.example.eventusajava.screens.addEvent.AddEditEventActivity.UIDateFormat;
import static com.example.eventusajava.screens.addEvent.AddEditEventActivity.UITimeFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventusajava.R;
import com.example.eventusajava.data.models.Event;
import com.example.eventusajava.screens.addEvent.AddEditEventActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.EventsViewHolder> {

    private List<Event> events = new ArrayList<>();
    private Consumer<Event> clickConsumer;

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public EventsRecyclerAdapter(Consumer<Event> clickConsumer) {
        this.clickConsumer = clickConsumer;
    }


    public class EventsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView descriptionTextView;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.rowEventName);
            locationTextView = itemView.findViewById(R.id.locationRowTextView);
            dateTextView = itemView.findViewById(R.id.dateRowTextView);
            timeTextView = itemView.findViewById(R.id.timeRowTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionRowTextView);

            itemView.setOnClickListener(view -> clickConsumer.accept(getItem(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_variant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {

        Event event = getItem(position);

        if (event == null) return;

        holder.nameTextView.setText(event.getName());
        holder.locationTextView.setText(event.getLocation());


        String date = UIDateFormat.format(event.getDateFrom());
        if(event.getDateFrom().getDay() != event.getDateTo().getDay()){
            date += " - " + UIDateFormat.format(event.getDateTo());
        }
        holder.dateTextView.setText(date);

        String time = UITimeFormat.format(event.getDateFrom()) + " - " + UITimeFormat.format(event.getDateTo());
        holder.timeTextView.setText(time);

        holder.descriptionTextView.setText(event.getDescription());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private Event getItem(int position) {
        if (position < events.size()) {
            return events.get(position);
        }
        return null;
    }


}
