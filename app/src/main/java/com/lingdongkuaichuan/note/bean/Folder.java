package com.lingdongkuaichuan.note.bean;

/**
 * Created by 周博文 on 2017/6/4.
 */

public class Folder {
    private int id;
    private String name;
    private String date;

    public Folder(int id , String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Folder(String name, String date){
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
