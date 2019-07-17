package com.example.ivwing.InnerDB;

import android.arch.persistence.room.PrimaryKey;

import io.realm.RealmObject;

public class UserVO extends RealmObject {
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_stat() {
        return user_stat;
    }

    public void setUser_stat(String user_stat) {
        this.user_stat = user_stat;
    }

    public int getUser_room() {
        return user_room;
    }

    public void setUser_room(int user_room) {
        this.user_room = user_room;
    }

    public int getUser_linker() {
        return user_linker;
    }

    public void setUser_linker(int user_linker) {
        this.user_linker = user_linker;
    }

    @PrimaryKey
    private int user_id;
    private String user_name;
    private String user_email;
    private String user_phone;
    private int user_age;
    private String user_gender;
    private String user_stat;
    private int user_room;
    private int user_linker;
}