package com.example.ivwing.Data;

public class ScheduleData {
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) { this.min = min; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    private String hour;
    private String min;
    private String type;
    private String room;
    private String doctor;

    public ScheduleData(int arr_vol){

    }

    public ScheduleData(String h, String m, String t, String r, String d){
        this.hour = h;
        this.min = m;
        this.type = t;
        this.room = r;
        this.doctor = d;
    }
}
