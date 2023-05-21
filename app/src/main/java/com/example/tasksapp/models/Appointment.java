package com.example.tasksapp.models;

public class Appointment {
   private String name;
   private String type;
   private String date;
   private String time;
   private long dateTime;
   private Integer importance;
   private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Appointment() {
    }

    public Appointment(String name, String type, String date, String time, long dateTime, Integer priority, String notes) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.importance = priority;
        this.notes = notes;
    }
}
