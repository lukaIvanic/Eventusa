package com.example.eventusajava.screens.addEvent;

import static java.util.Calendar.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventusajava.R;
import com.example.eventusajava.data.models.Event;
import com.example.eventusajava.network.NetworkManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    boolean isEditEvent = false;
    Event event = new Event();
    TextInputEditText nameEditText;
    TextInputEditText locationEditText;
    TextInputEditText descriptionEditText;
    TextInputLayout datumOdLayout;
    TextInputEditText datumOdEditText;
    TextInputLayout datumDoLayout;
    TextInputEditText datumDoEditText;
    Button sendEventButton;

    public static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat UIDateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
    public static final SimpleDateFormat UIDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final SimpleDateFormat UITimeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        isEditEvent = getIntent().getBooleanExtra("isEdit", false);

        setupUI();


    }

    private void setupUI() {
        setupReferences();

        setupInteraction();

        datumOdEditText.setText(UIDateTimeFormat.format(getInstance().getTime()));
        datumDoEditText.setText(UIDateTimeFormat.format(getInstance().getTime()));

        event.setDateFrom(getInstance().getTime());
        event.setDateTo(getInstance().getTime());

        if (isEditEvent) {
            setTitle("Event details");
            loadValues();
        } else {
            setTitle("Add event");
        }
    }

    private void loadValues() {
        try {
            event = new Gson().fromJson(getIntent().getStringExtra("eventJson"), Event.class);
            event.setDateTo(defaultDateFormat.parse(event.getDateToString()));
            event.setDateFrom(defaultDateFormat.parse(event.getDateFromString()));
            nameEditText.setText(event.getName());
            locationEditText.setText(event.getLocation());
            descriptionEditText.setText(event.getDescription());
            datumOdEditText.setText(UIDateTimeFormat.format(event.getDateFrom()));
            datumDoEditText.setText(UIDateTimeFormat.format(event.getDateTo()));
            sendEventButton.setText("UPDATE EVENT");


        } catch (Exception ignored) {
            isEditEvent = false;
        }
    }

    private void setupReferences() {
        nameEditText = findViewById(R.id.naslovEditText);
        locationEditText = findViewById(R.id.lokacijaEditText);
        descriptionEditText = findViewById(R.id.opisEditText);
        datumOdLayout = findViewById(R.id.dateFromInputLayout);
        datumOdEditText = findViewById(R.id.dateFromEditText);
        datumDoLayout = findViewById(R.id.dateToInputLayout);
        datumDoEditText = findViewById(R.id.dateToEditText);
        sendEventButton = findViewById(R.id.sendEventButton);
    }

    private void setupInteraction() {

        datumOdLayout.setEndIconOnClickListener(view -> {
            pickTimeAndDate(true);
        });

        datumDoLayout.setEndIconOnClickListener(view -> {
            pickTimeAndDate(false);
        });

        sendEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEditEvent && event.getDateFrom().getDate() <= getInstance().getTime().getDate()){
                    Toast.makeText(AddEditEventActivity.this, "The date needs to be from tomorrow or onward!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(event.getDateFrom().getTime() > event.getDateTo().getTime()){
                    Toast.makeText(AddEditEventActivity.this, "You picked an end time that's before the event's start time.", Toast.LENGTH_SHORT).show();
                    return;
                }



                populateFields();
                if (isEditEvent) {
                    editEvent();
                } else {
                    addEvent();
                }
            }
        });


    }

    private void pickTimeAndDate(boolean isDatumOd) {
        openDateDialog(isDatumOd);
    }

    private void openDateDialog(boolean isDatumOd) {
        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
            Toast.makeText(AddEditEventActivity.this, "Year: " + year + " Month: " + month + " Day: " + day, Toast.LENGTH_SHORT).show();
            openTimeDialog(year, month, day, isDatumOd);
        };

        Calendar cal = getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.show();
    }

    private void openTimeDialog(int year, int month, int day, boolean isDatumOd) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hour, minute) -> {

            Calendar cal = getInstance();
            cal.set(YEAR, year);
            cal.set(MONTH, month);
            cal.set(DAY_OF_MONTH, day);
            cal.set(HOUR_OF_DAY, hour);
            cal.set(MINUTE, minute);


            if (isDatumOd) {
                event.setDateFrom(cal.getTime());
                datumOdEditText.setText(UIDateTimeFormat.format(event.getDateFrom()));
            } else {
                event.setDateTo(cal.getTime());
                datumDoEditText.setText(UIDateTimeFormat.format(event.getDateTo()));
            }
        };

        Calendar cal = getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, cal.get(HOUR_OF_DAY), cal.get(MINUTE), true);
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.show();
    }


    private void populateFields() {
        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setText("Sastanak u vezi eventuše");
        }

        if (locationEditText.getText().toString().isEmpty()) {
            locationEditText.setText("Ured");
        }

        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setText("Prezentacija početne verzije eventuše od android devlopera.");
        }



    }

    private void makeEvent() {
        event.setName(nameEditText.getText().toString());
        event.setDescription(descriptionEditText.getText().toString());
        event.setLocation(locationEditText.getText().toString());
        event.setCalendar(false);
        event.setDays();
        event.setDateToString(defaultDateFormat.format(event.getDateTo()));
        event.setDateFromString(defaultDateFormat.format(event.getDateFrom()));
    }

    private void addEvent() {

        makeEvent();


        NetworkManager.postEvent(event,
                (eventId) -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Post successful " + event.getName(), Toast.LENGTH_SHORT).show();
                        finish();
                    });

                }, exception -> {
                    runOnUiThread(() -> {
                        Toast.makeText(AddEditEventActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
                });
    }

    private void editEvent() {

        makeEvent();

        NetworkManager.updateEvent(event,
                aBoolean -> runOnUiThread(() -> {
                    Toast.makeText(AddEditEventActivity.this, "Successfully updated.", Toast.LENGTH_SHORT).show();
                    finish();
                }),
                exception -> runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEditEventActivity.this, "Was not able to update event.", Toast.LENGTH_SHORT).show();

                    }
                }));

    }

}