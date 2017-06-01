package com.lingdongkuaichuan.note.bean;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class Note {

    private String tittle;
    private String content;
    private String date;
    //private int id;

    public Note(/*int id , */String tittle , String content , String date){
        this.tittle = tittle;
        this.content = content;
        this.date = date;
        //this.id = id;
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

/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/
}
