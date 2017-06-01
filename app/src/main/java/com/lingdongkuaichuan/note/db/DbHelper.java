package com.lingdongkuaichuan.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class DbHelper extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static DbHelper dbHelper;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "note_db";
    private static final String TABLE_NOTE_NAME = "notes";

    private static final String TABLE_NOTE_COLUMN_TITTLE = "title";
    private static final String TABLE_NOTE_COLUMN_CONTENT = "content";
    private static final String TABLE_NOTE_COLUMN_DATE = "date";
    private static final String TABLE_NOTE_COLUMN_ID = "_id";

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

        // 此处创建数据表
        String table_note_sql = "create table " + TABLE_NOTE_NAME + " ( "
                + TABLE_NOTE_COLUMN_ID + " integer primary key autoincrement, "
                + TABLE_NOTE_COLUMN_TITTLE + " text not null, "
                + TABLE_NOTE_COLUMN_CONTENT + " text not null, "
                + TABLE_NOTE_COLUMN_DATE + " integer );";
        db.execSQL(table_note_sql);
        Log.e(TAG, "数据表 note 创建成功！");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
