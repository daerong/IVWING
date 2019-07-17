package com.example.ivwing.Data;

import java.util.ArrayList;

public class LoginResult {

    public LoginResult(String status, String token, LoginData data, String msg) {
        this.status = status;
        this.token = token;
        this.data = data;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String status;
    private String token;
    private LoginData data;
    private String msg;

    public class LoginData {
        public LoginData(LoginData sample){
            this.user_id = sample.user_id;
            this.user_name = sample.user_name;
            this.user_email = sample.user_email;
            this.user_phone = sample.user_phone;
            this.user_age = sample.user_age;
            this.user_gender = sample.user_gender;
            this.user_stat = sample.user_stat;
            this.user_room = sample.user_room;
            this.user_linker = sample.user_linker;
        }
        public LoginData(int user_id, String user_name, String user_email, String user_phone, int user_age, char user_gender, char user_stat, int user_room, int user_linker) {
            this.user_id = user_id;
            this.user_name = user_name;
            this.user_email = user_email;
            this.user_phone = user_phone;
            this.user_age = user_age;
            this.user_gender = user_gender;
            this.user_stat = user_stat;
            this.user_room = user_room;
            this.user_linker = user_linker;
        }

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

        public char getUser_gender() {
            return user_gender;
        }

        public void setUser_gender(char user_gender) {
            this.user_gender = user_gender;
        }

        public char getUser_stat() {
            return user_stat;
        }

        public void setUser_stat(char user_stat) {
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

        private int user_id;
        private String user_name;
        private String user_email;
        private String user_phone;
        private int user_age;
        private char user_gender;
        private char user_stat;
        private int user_room;
        private int user_linker;
    }

    public void copy(LoginData target, int vol){
            target = data;
    }
}
