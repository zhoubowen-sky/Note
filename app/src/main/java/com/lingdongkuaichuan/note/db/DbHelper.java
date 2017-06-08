package com.lingdongkuaichuan.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.utils.DateUtil;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class DbHelper extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static DbHelper dbHelper;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "note.db";
    public static final String TABLE_NOTE_NAME = "note";
    public static final String TABLE_FOLDER_NAME = "folder";
    public static final String DEFAULT_FOLDER_NAME = "默认分组";

    public static final String TABLE_NOTE_COLUMN_TITTLE = "note_title";
    public static final String TABLE_NOTE_COLUMN_CONTENT = "note_content";
    public static final String TABLE_NOTE_COLUMN_DATE = "note_date";
    public static final String TABLE_NOTE_COLUMN_ID = "note_id";
    public static final String TABLE_NOTE_COLUMN_FOLDER_ID = "note_folder_id";

    public static final String TABLE_FOLDER_COLUMN_ID = "folder_id";
    public static final String TABLE_FOLDER_COLUMN_NAME = "folder_name";
    public static final String TABLE_FOLDER_COLUMN_DATE = "folder_date";

    private SQLiteDatabase db;

    // 第三个参数 factory 为空代表使用系统默认的工厂类 获取游标 cursor 实例
    // 私有化构造函数
    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 单例模式操作数据库
    public static SQLiteDatabase getInstance(Context context){
        if (dbHelper == null){
            dbHelper = new DbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return dbHelper.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        // 此处创建note数据表
        String table_note_sql = "create table " + TABLE_NOTE_NAME + " ( "
                + TABLE_NOTE_COLUMN_ID + " integer primary key autoincrement, "
                + TABLE_NOTE_COLUMN_TITTLE + " text not null, "
                + TABLE_NOTE_COLUMN_CONTENT + " text not null, "
                + TABLE_NOTE_COLUMN_FOLDER_ID + " integer, "
                + TABLE_NOTE_COLUMN_DATE + " integer );";
        db.execSQL(table_note_sql);
        Log.d(TAG, "数据表 note 创建成功！");

        // 此处创建folder表
        String table_folder_sql = "create table " + TABLE_FOLDER_NAME + " ( "
                + TABLE_FOLDER_COLUMN_ID + " integer primary key autoincrement, "
                + TABLE_FOLDER_COLUMN_NAME + " text not null, "
                + TABLE_FOLDER_COLUMN_DATE + " integer );";
        db.execSQL(table_folder_sql);
        Log.d(TAG, "数据表folder 创建成功！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
