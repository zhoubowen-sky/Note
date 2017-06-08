package com.lingdongkuaichuan.note.bean;

/**
 * Created by 周博文 on 2017/6/4.
 */

public class Folder {

    private int id;
    private String name;
    private String date;
    private boolean checked;

    public Folder(int id , String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
        this.checked = false;
    }

    public Folder(int id , String name, String date, boolean checked){
        this.id = id;
        this.name = name;
        this.date = date;
        this.checked = checked;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
