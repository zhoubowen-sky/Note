package com.lingdongkuaichuan.note.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lingdongkuaichuan.note.bean.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class NoteDB {
    /**
     * 关于数据库的设计说明
     * 1.数据库使用单例模式
     * 2.NoteDB用以封装所有的数据库操作方法
     * 3.
     */

    public static SQLiteDatabase db;

    public NoteDB(Context context){
        db = DbHelper.getInstance(context);
    }

    /**
     * 创建数据表
     * @param context
     */
    public static void createDatebase(Context context){
        NoteDB noteDB = new NoteDB(context);
    }

    public static void insertTestData(){

        for (int i = 0; i< 10 ; i++){
            String sql = "insert into notes( title, content, date) values (\"aa\", \"content\", 9155)";
            //INSERT INTO test(name,age) VALUES("aa",2);
            db.execSQL(sql);
        }

    }

    public static List<Note> getAllNotes(){
        List<Note> noteList = new ArrayList<Note>();
        String sql = "select * from notes";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String date    = cursor.getLong(cursor.getColumnIndex("date")) + "";
            String title   = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            noteList.add(new Note(title, content, date));
        }
        cursor.close();
        return noteList;

    }











}
