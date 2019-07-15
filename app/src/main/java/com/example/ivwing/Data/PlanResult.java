package com.example.ivwing.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlanResult {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PlanData> getData() {
        return data;
    }

    public void setData(ArrayList<PlanData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String status;
    private ArrayList<PlanData> data;
    private String msg;

    PlanResult(String status, ArrayList<PlanData> data, String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public class PlanData {

        public PlanData(int plan_id, String plan_date, String plan_type, int plan_user, int plan_dept, int plan_doctor, String dept_name, String doctor_name, String doctor_type) {
            this.plan_id = plan_id;
            this.plan_date = plan_date;
            this.plan_type = plan_type;
            this.plan_user = plan_user;
            this.plan_dept = plan_dept;
            this.plan_doctor = plan_doctor;
            this.dept_name = dept_name;
            this.doctor_name = doctor_name;
            this.doctor_type = doctor_type;
        }

        public int getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(int plan_id) {
            this.plan_id = plan_id;
        }

        public String getPlan_date() {
            return plan_date;
        }

        public void setPlan_date(String plan_date) {
            this.plan_date = plan_date;
        }

        public String getPlan_type() {
            return plan_type;
        }

        public void setPlan_type(String plan_type) {
            this.plan_type = plan_type;
        }

        public int getPlan_user() {
            return plan_user;
        }

        public void setPlan_user(int plan_user) {
            this.plan_user = plan_user;
        }

        public int getPlan_dept() {
            return plan_dept;
        }

        public void setPlan_dept(int plan_dept) {
            this.plan_dept = plan_dept;
        }

        public int getPlan_doctor() {
            return plan_doctor;
        }

        public void setPlan_doctor(int plan_doctor) {
            this.plan_doctor = plan_doctor;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }

        public String getDoctor_name() {
            return doctor_name;
        }

        public void setDoctor_name(String doctor_name) {
            this.doctor_name = doctor_name;
        }

        public String getDoctor_type() {
            return doctor_type;
        }

        public void setDoctor_type(String doctor_type) {
            this.doctor_type = doctor_type;
        }

        private int plan_id;
        private String plan_date;
        private String plan_type;
        private int plan_user;
        private int plan_dept;
        private int plan_doctor;
        private String dept_name;
        private String doctor_name;
        private String doctor_type;

        @Override
        public String toString(){
            return "PlanData [plan_id = " + plan_id + ", plan_date = " + plan_date + ", plan_type = " + plan_type + ", plan_user = " + plan_user + ", plan_dept = " + plan_dept + ", plan_doctor = " + plan_doctor + ", dept_name = " + dept_name + ", doctor_name = " + doctor_name + ", doctor_type = " + doctor_type + "]";
        }
    }

    public void copy(PlanData[] target, int vol){
        for(int i=0; i< vol; i++){
            target[i] = data.get(i);
        }
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", data = " + data + ", msg = " + msg + "]";
    }
}
