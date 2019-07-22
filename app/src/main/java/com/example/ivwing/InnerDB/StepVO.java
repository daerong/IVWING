package com.example.ivwing.InnerDB;

import android.arch.persistence.room.PrimaryKey;

import io.realm.RealmObject;

public class StepVO extends RealmObject {
    public int getStep_vol() {
        return step_vol;
    }

    public void setStep_vol(int step_vol) {
        this.step_vol = step_vol;
    }

    public int getStep_user() {
        return step_user;
    }

    public void setStep_user(int step_user) {
        this.step_user = step_user;
    }

    @PrimaryKey
    private int step_user;
    private int step_vol;
}
