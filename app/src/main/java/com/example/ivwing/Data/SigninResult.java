package com.example.ivwing.Data;

public class SigninResult {
    private String status;
    private String data;
    private String msg;

    SigninResult(){

    }
    SigninResult(String status, String data, String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", data = " + data + ", msg = " + msg + "]";
    }

}
