package com.lingdongkuaichuan.note.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.db.DbHelper;
import com.lingdongkuaichuan.note.db.NoteDB;
import com.lingdongkuaichuan.note.fragment.HomeFragment;
import com.lingdongkuaichuan.note.utils.DateUtil;

public class EditNoteActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private String stringFromMainActivity;

    private EditText et_note_edit;

    private static Note note;

    private static Note note_before;

    private static long new_note_id;

    public static int NEW_NOTE_FOLDER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        et_note_edit = (EditText) findViewById(R.id.et_note_edit);

        // 获取从MainActivity传过来的参数，如果是新建Note就直接新建，
        // 如果是编辑已有Note，则需要根据Note ID从数据库中取出其数据
        stringFromMainActivity = getStringFromMainActivity();
        Log.d(TAG, "MainActivity传到EditNoteActivity的参数为：" + stringFromMainActivity);
        switch (stringFromMainActivity){
            case MainActivity.INTENT_ADD_NOTE:
                note = new Note(1, getNoteTitle(), getNoteContent(), getNoteDate(), NEW_NOTE_FOLDER_ID);
                new_note_id = addOneNote(note);
                break;
            case MainActivity.INTENT_EDIT_NOTE:
                // 获取从MainActivity传递过来的整个Note对象，填充到 editText
                note_before = (Note) getIntent().getSerializableExtra(MainActivity.NOTE_ID);
                Log.d(TAG, "从MainActivity传递过来的Note对象数据：" + " id " + note_before.getId() + " content " + note_before.getContent());
                setNoteToEditText(note_before);
                break;
        }




    }

    private String getNoteDate() {
        return DateUtil.getCurrentDateLine();
    }

    private String getNoteContent() {
        return et_note_edit.getText().toString();
    }

    private String getNoteTitle() {
        String content = et_note_edit.getText().toString();
        if (content == null || "".equals(content)){
            return "";
        }
        int end = content.indexOf("\n");
        return content.substring(0, end == -1 ? content.length() : end);
    }

    /**
     * 获取从MainActivity通过Intent传过来的值
     * @return
     */
    private String getStringFromMainActivity(){
        Intent editNoteIntent = getIntent();
        String stringExtra = editNoteIntent.getStringExtra(MainActivity.ADD_OR_EDIT_NOTE);
        return stringExtra;
    }

    /**
     * 将note内容填充到编辑框中
     * @param note
     */
    private void setNoteToEditText(Note note){
        et_note_edit.setText(note.getContent());
    }

    private void updateNoteToDB(Note note){
        boolean bool = NoteDB.updateNote(note);
        Log.d(TAG, "updateNoteToDB id" + note.getId() + " title" + note.getTittle()
                + " content" + note.getContent() + " date" + note.getDate() + "folder_id " + note.getFolder_id());
        if (bool){
            Log.d(TAG, "updateNoteToDB success !");
        }else {
            Log.e(TAG, "updateNoteToDB fail !");
        }
    }


    /**
     * 添加一条便签
     * @param note
     */
    private long addOneNote(Note note) {
        long row = NoteDB.insertOneNote(note);
        if (row <= 0){
            Log.e(TAG, "新建一条便签失败");
        }else {
            Log.d(TAG, "新建一条便签成功，便签在数据表中的ID为：" + row);
        }
        return row;
    }

    @Override
    protected void onPause(){
        // 自动保存书签内容 需要更新title以及日期
        super.onPause();
        if (stringFromMainActivity.equals(MainActivity.INTENT_ADD_NOTE)){
            note = null;
            note = new Note((int) new_note_id, getNoteTitle(), getNoteContent(), getNoteDate(), NEW_NOTE_FOLDER_ID);
        }else if (stringFromMainActivity.equals(MainActivity.INTENT_EDIT_NOTE)){
            note = null;
            note = new Note(note_before.getId(), getNoteTitle(), getNoteContent(), getNoteDate(), note_before.getFolder_id());
        }
        updateNoteToDB(note);
    }




}
