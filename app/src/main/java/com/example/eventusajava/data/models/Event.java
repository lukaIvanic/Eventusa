package com.example.eventusajava.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {

    private int EventId = 0;
    @SerializedName("From")
    private String dateFromString;
    transient private Date dateFrom;
    @SerializedName("To")
    private String dateToString;
    transient private Date dateTo;


    @SerializedName("DateAddded")
    private String dateAddedString;
    private String Name;
    private String Description;
    private String Location;
    private boolean Calendar;

    private int days;


    public Event(){

    }

    public Event(int eventId, String dateFromString, String dateToString, String name, String description, String location, boolean calendar, String dateAddedString, int days) {
        this.EventId = eventId;
        this.dateFromString = dateFromString;
        this.dateToString = dateToString;
        this.Name = name;
        this.Description = description;
        this.Location = location;
        this.Calendar = calendar;
        this.dateAddedString = dateAddedString;
        this.days = days;
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        this.EventId = eventId;
    }

    public String getDateFromString() {
        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public boolean isCalendar() {
        return Calendar;
    }

    public void setCalendar(boolean calendar) {
        this.Calendar = calendar;
    }

    public String getDateAddedString() {
        return dateAddedString;
    }

    public void setDateAddedString(String dateAddedString) {
        this.dateAddedString = dateAddedString;
    }

    public int getDays() {
        return days;
    }

    public void setDays() {
        days = dateFrom.getDay() - dateTo.getDate();
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

}
