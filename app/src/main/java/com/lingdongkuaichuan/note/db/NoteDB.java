package com.lingdongkuaichuan.note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lingdongkuaichuan.note.activity.MainActivity;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.utils.DateUtil;

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
     * 3.数据库有改动，即使是字段名称的改动，也只需要更多此类以及 DbHelper 类即可，其他业务逻辑无需更改
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
        // 创建数据库数据表
        NoteDB noteDB = new NoteDB(context);
    }


    public static long insertOneNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TABLE_NOTE_COLUMN_TITTLE, note.getTittle());
        cv.put(DbHelper.TABLE_NOTE_COLUMN_CONTENT, note.getContent());
        cv.put(DbHelper.TABLE_NOTE_COLUMN_DATE, note.getDate());
        cv.put(DbHelper.TABLE_NOTE_COLUMN_FOLDER_ID, note.getFolder_id());

        long rows = db.insert(DbHelper.TABLE_NOTE_NAME, DbHelper.TABLE_NOTE_COLUMN_TITTLE, cv);
        return rows;
    }

    /**
     * 向数据表folder中插入一条数据，即新建一个文件夹
     * @param folder
     * @return
     */
    public static long insertOneFolder(Folder folder){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TABLE_FOLDER_COLUMN_NAME, folder.getName());
        cv.put(DbHelper.TABLE_FOLDER_COLUMN_DATE, folder.getDate());
        long rows = db.insert(DbHelper.TABLE_FOLDER_NAME, DbHelper.TABLE_FOLDER_COLUMN_NAME, cv);

        return rows;
    }

    /**
     * 取出所有的便签数据
     * @return
     */
    public static List<Note> getAllNotes(){
        List<Note> noteList = new ArrayList<Note>();
        String sql = "select * from " + DbHelper.TABLE_NOTE_NAME + " order by " + DbHelper.TABLE_NOTE_COLUMN_DATE + " desc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String date    = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_NOTE_COLUMN_DATE));
            String title   = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_NOTE_COLUMN_TITTLE));
            String content = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_NOTE_COLUMN_CONTENT));
            int id         = cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_NOTE_COLUMN_ID));
            int folder_id  = cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_NOTE_COLUMN_FOLDER_ID));
            noteList.add(new Note(id, title, content, date, folder_id));
        }
        cursor.close();
        return noteList;
    }

    public static List<Folder> getAllFolders(){
        List<Folder> folderList = new ArrayList<Folder>();
        String sql = "select * from " + DbHelper.TABLE_FOLDER_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id      = cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_FOLDER_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_FOLDER_COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_FOLDER_COLUMN_DATE));
            folderList.add(new Folder(id, name, date));
        }
        cursor.close();
        return folderList;
    }

    /**
     * 更新数据库中的某一条note
     * @param note
     * @return
     */
    public static boolean updateNote(Note note){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DbHelper.TABLE_NOTE_COLUMN_TITTLE, note.getTittle());
//        contentValues.put(DbHelper.TABLE_NOTE_COLUMN_CONTENT, note.getContent());
//        contentValues.put(DbHelper.TABLE_NOTE_COLUMN_DATE, note.getDate());
//        contentValues.put(DbHelper.TABLE_NOTE_COLUMN_ID, note.getId());
//        String[] args = {Integer.toString(note.getId())};
//        int rows = db.update("notes", contentValues, "_id=?", args);

        String update_sql = "update "
                + DbHelper.TABLE_NOTE_NAME
                + " set " + DbHelper.TABLE_NOTE_COLUMN_TITTLE
                + " = \"" + note.getTittle() + "\""
                + "," + DbHelper.TABLE_NOTE_COLUMN_CONTENT
                + " = \"" + note.getContent() + "\""
                + "," + DbHelper.TABLE_NOTE_COLUMN_DATE
                + " = " + note.getDate()
                + " where " + DbHelper.TABLE_NOTE_COLUMN_ID + " = \"" + note.getId() + "\"";
        Log.d("NoteDB", "更新一条便签的SQL语句为：" + update_sql);
        db.execSQL(update_sql);
        return true;
    }









}
