package com.lingdongkuaichuan.note.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.adapter.NoteAdapter;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.db.NoteDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/4/22.
 */

public class HomeFragment extends Fragment {

    private ImageButton img_btn_search_notes;    // 左上角搜索按钮
    private ImageButton img_btn_add_notes; //右上角添加计事按钮

    private ListView listView;

    private NoteAdapter noteAdapter;

    private List<Note> noteList = new ArrayList<Note>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        img_btn_search_notes = (ImageButton) view.findViewById(R.id.img_btn_search_notes);
        img_btn_add_notes    = (ImageButton) view.findViewById(R.id.img_btn_add_notes);

        listView = (ListView) view.findViewById(R.id.lv_note);
        // 绘制 listview 画面
        refreshListView();

        img_btn_add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"你点击了添加便签按钮！",Toast.LENGTH_LONG).show();
                NoteDB.insertTestData();
            }
        });


        return view;
    }

    private void refreshListView(){
        noteList.clear();
        noteList = NoteDB.getAllNotes();
        noteAdapter = new NoteAdapter(getActivity(), noteList);
        listView.setAdapter(noteAdapter);
    }















































}
