package com.example.tasksapp.models;

public class Appointment {
   private String name;
   private String type;
   private String date;
   private String noticeDate;
   private String time;
   private String noticeTime;
   private long dateTime;
   private String importance;
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

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
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

    public Appointment(String name, String type, String date, String noticeDate, String time, String noticeTime, long dateTime, String priority, String notes) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.noticeDate = noticeDate;
        this.time = time;
        this.noticeTime = noticeTime;
        this.dateTime = dateTime;
        this.importance = priority;
        this.notes = notes;
    }
}
