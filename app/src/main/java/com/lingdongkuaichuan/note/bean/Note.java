package com.lingdongkuaichuan.note.bean;

import java.io.Serializable;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class Note implements Serializable {

    private String tittle;
    private String content;
    private String date;
    private int id;
    private int folder_id;
    private boolean checked;

    public Note(int id , String tittle , String content , String date , int folder_id, boolean checked){
        this.tittle = tittle;
        this.content = content;
        this.date = date;
        this.id = id;
        this.folder_id = folder_id;
        this.checked = checked;
    }

    public Note(int id , String tittle , String content , String date , int folder_id){
        this.tittle = tittle;
        this.content = content;
        this.date = date;
        this.id = id;
        this.folder_id = folder_id;
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }


    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
