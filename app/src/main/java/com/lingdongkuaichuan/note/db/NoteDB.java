package com.lingdongkuaichuan.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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













}
