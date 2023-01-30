package com.example.eventusajava.screens.eventsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.eventusajava.R;
import com.example.eventusajava.data.models.Event;
import com.example.eventusajava.network.NetworkManager;
import com.example.eventusajava.screens.addEvent.AddEditEventActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.function.Consumer;

public class EventsListActivity extends AppCompatActivity {

    RecyclerView eventsRecyclerView;
    EventsRecyclerAdapter adapter;

    FloatingActionButton newEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        setTitle("Events");

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        adapter = new EventsRecyclerAdapter(event -> {
            gotoEditEventScreen(event);
//            deleteEvent(event);
        });
        eventsRecyclerView.setAdapter(adapter);


        newEventButton = findViewById(R.id.newEventButton);

        newEventButton.setOnClickListener(view -> gotoNewEventScreen());

    }

    private void getEvents() {
        NetworkManager.getEvents(events -> {

            runOnUiThread(() -> {
                adapter.setEvents(events);
                adapter.notifyDataSetChanged();
            });


        }, exception -> {

            runOnUiThread(() -> {
                Toast.makeText(EventsListActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }

    private void deleteEvent(Event event) {
        NetworkManager.postDeleteEvent(event.getEventId(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventsListActivity.this, event.getName() + " event successfully deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void gotoNewEventScreen() {
        Intent intent = new Intent(EventsListActivity.this, AddEditEventActivity.class);
        startActivity(intent);
    }

    private void gotoEditEventScreen(Event event) {
        Intent intent = new Intent(EventsListActivity.this, AddEditEventActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("eventJson", new Gson().toJson(event));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEvents();
    }
}