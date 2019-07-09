package com.example.ivwing.RequestData;

public class User {
//    int user_id;
    String user_name;
    String user_email;
    String user_pwd;
    String user_salt;
    String user_phone;
    int user_age;
    char user_gender;
//    char user_stat;
//    int user_room;
//    int user_linker;

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

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_salt() {
        return user_salt;
    }

    public void setUser_salt(String user_salt) {
        this.user_salt = user_salt;
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

    public char getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(char user_gender) {
        this.user_gender = user_gender;
    }
}