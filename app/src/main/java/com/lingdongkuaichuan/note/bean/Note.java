package com.lingdongkuaichuan.note.bean;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class Note {

    private String tittle;
    private String content;
    private long date;
    private int id;

    public Note(String tittle , String content , long date , int id){
        this.tittle = tittle;
        this.content = content;
        this.date = date;
        this.id = id;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
