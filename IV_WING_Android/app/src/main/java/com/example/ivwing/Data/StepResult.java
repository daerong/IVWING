package com.example.ivwing.Data;

import java.util.ArrayList;

public class StepResult {
    public StepResult(String status, ArrayList<StepData> data, String msg) {
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

    public ArrayList<StepData> getData() {
        return data;
    }

    public void setData(ArrayList<StepData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String status;
    private ArrayList<StepData> data;
    private String msg;


    public class StepData {
        public StepData(int step_id, String step_date, int step_vol, int step_user) {
            this.step_id = step_id;
            this.step_date = step_date;
            this.step_vol = step_vol;
            this.step_user = step_user;
        }

        public int getStep_id() {
            return step_id;
        }

        public void setStep_id(int step_id) {
            this.step_id = step_id;
        }

        public String getStep_date() {
            return step_date;
        }

        public void setStep_date(String step_date) {
            this.step_date = step_date;
        }

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

        private int step_id;
        private String step_date;
        private int step_vol;
        private int step_user;

    }

    public void copy(StepData[] target, int vol){
        for(int i=0; i< vol; i++){
            target[i] = data.get(i);
        }
    }

}
