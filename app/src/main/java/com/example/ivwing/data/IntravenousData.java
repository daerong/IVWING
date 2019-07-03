package com.example.ivwing.data;

public class IntravenousData {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getGtt() {
        return gtt;
    }

    public void setGtt(int gtt) {
        this.gtt = gtt;
    }

    private String name;
    private int left;
    private int max;
    private int time;
    private int gtt;

    public IntravenousData(String s, String s1, int i){

    }

    public IntravenousData(String n, int l, int m, int t, int g){
        this.name = n;
        this.left = l;
        this.max = m;
        this.time = t;
        this.gtt = g;
    }
}
