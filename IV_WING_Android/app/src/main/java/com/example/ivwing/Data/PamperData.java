package com.example.ivwing.Data;

public class PamperData {
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    String month;
    String day;
    int step;


    public PamperData(){

    }

    public PamperData(String month, String day, int step){
        this.month = month;
        this.day = day;
        this.step = step;
    }
}
