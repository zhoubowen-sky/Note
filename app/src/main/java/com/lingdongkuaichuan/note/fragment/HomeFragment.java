package com.lingdongkuaichuan.note.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.activity.EditNoteActivity;
import com.lingdongkuaichuan.note.activity.MainActivity;
import com.lingdongkuaichuan.note.adapter.NoteAdapter;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.db.NoteDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/4/22.
 */

public class HomeFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();


    private ListView listView;

    private NoteAdapter noteAdapter;

    public static List<Note> noteList = new ArrayList<Note>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.lv_note);
        // 绘制 listview 画面
        refreshListView();

        // item 设置监听器
        setItemClickListener();

        return view;
    }

    private void refreshListView(){
        noteList.clear();
        noteList = NoteDB.getAllNotes();
        noteAdapter = new NoteAdapter(getActivity(), noteList);
        listView.setAdapter(noteAdapter);
    }

    /**
     * listview 设定点击以及长按的监听事件
     */
    private void setItemClickListener(){
        // 点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "点击了listview，点击的position为：" + position);
                // 跳转到便签的编辑界面
                Intent editNoteIntent = new Intent();
                editNoteIntent.setClass(getActivity(), EditNoteActivity.class);
                editNoteIntent.putExtra(MainActivity.ADD_OR_EDIT_NOTE, MainActivity.INTENT_EDIT_NOTE);
                editNoteIntent.putExtra(MainActivity.NOTE_ID, noteList.get(position)); // listview 的 position 获取对象，传递整个对象过去
                getActivity().startActivityForResult(editNoteIntent, 2);
            }
        });

        // 长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }



}
