package com.example.ivwing.Data;

import java.util.ArrayList;

public class RecordResult {

    public RecordResult(String status, ArrayList<RecordData> data, String msg) {
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

    public ArrayList<RecordData> getData() {
        return data;
    }

    public void setData(ArrayList<RecordData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String status;
    private ArrayList<RecordData> data;
    private String msg;

    public class RecordData {

        public RecordData(int record_id, int record_gtt, char record_stat, int iv_id, String iv_name, int iv_max, int iv_now, char iv_stat, int iv_percent, int iv_time) {
            this.record_id = record_id;
            this.record_gtt = record_gtt;
            this.record_stat = record_stat;
            this.iv_id = iv_id;
            this.iv_name = iv_name;
            this.iv_max = iv_max;
            this.iv_now = iv_now;
            this.iv_stat = iv_stat;
            this.iv_percent = iv_percent;
            this.iv_time = iv_time;
        }

        public int getRecord_id() {
            return record_id;
        }

        public void setRecord_id(int record_id) {
            this.record_id = record_id;
        }

        public int getRecord_gtt() {
            return record_gtt;
        }

        public void setRecord_gtt(int record_gtt) {
            this.record_gtt = record_gtt;
        }

        public char getRecord_stat() {
            return record_stat;
        }

        public void setRecord_stat(char record_stat) {
            this.record_stat = record_stat;
        }

        public int getIv_id() {
            return iv_id;
        }

        public void setIv_id(int iv_id) {
            this.iv_id = iv_id;
        }

        public String getIv_name() {
            return iv_name;
        }

        public void setIv_name(String iv_name) {
            this.iv_name = iv_name;
        }

        public int getIv_max() {
            return iv_max;
        }

        public void setIv_max(int iv_max) {
            this.iv_max = iv_max;
        }

        public int getIv_now() {
            return iv_now;
        }

        public void setIv_now(int iv_now) {
            this.iv_now = iv_now;
        }

        public char getIv_stat() {
            return iv_stat;
        }

        public void setIv_stat(char iv_stat) {
            this.iv_stat = iv_stat;
        }

        public int getIv_percent() {
            return iv_percent;
        }

        public void setIv_percent(int iv_percent) {
            this.iv_percent = iv_percent;
        }

        public int getIv_time() {
            return iv_time;
        }

        public void setIv_time(int iv_time) {
            this.iv_time = iv_time;
        }

        private int record_id;
        private int record_gtt;
        private char record_stat;
        private int iv_id;
        private String iv_name;
        private int iv_max;
        private int iv_now;
        private char iv_stat;
        private int iv_percent;
        private int iv_time;
    }

    public void copy(RecordData[] target, int vol){
        for(int i=0; i< vol; i++){
            target[i] = data.get(i);
        }
    }


}
