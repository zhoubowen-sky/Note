package com.lingdongkuaichuan.note.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.activity.EditNoteActivity;
import com.lingdongkuaichuan.note.activity.MainActivity;
import com.lingdongkuaichuan.note.adapter.NoteAdapter;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.db.DbHelper;
import com.lingdongkuaichuan.note.db.NoteDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/4/22.
 */

public class HomeFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();


    public static ListView listView;

    public static NoteAdapter noteAdapter;

    public static List<Note> noteList = new ArrayList<Note>();

    private HomeToMainActivity setEditTab;

    public static boolean isShowCheckBox = false;

    private int folder_id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取传进来的数据
        Bundle bundle = getArguments();
        folder_id = bundle.getInt(DbHelper.TABLE_NOTE_COLUMN_FOLDER_ID);
        Log.e(TAG, "传进来的folder_id ：" + folder_id);
        if (folder_id == 0){
            // 从 MainActivity 跳转进来的
            noteList.clear();
            noteList = NoteDB.getAllNotes(0);
        }else {
            // 从 folderFragment 跳转过来的
            noteList.clear();
            noteList = NoteDB.getAllNotes(folder_id);
        }

        Log.e(TAG, "noteList 数据个数为：" + noteList.size());
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


    public static void refreshListView(){
        noteList.clear();
        noteList = NoteDB.getAllNotes(0);
        noteAdapter = new NoteAdapter(MainActivity.mContext, noteList);
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
                // 隐藏底部原来的的tab 展示新的Tab 这里用到接口回调与Activity通信
                setEditTab.setEditTab("HomeFragment....");
                // 展示listview所有的CheckBox 其默认为未选中隐藏，当长按以后显示
                isShowCheckBox = true;
                noteAdapter.notifyDataSetChanged();
                // 如果返回true那么click就不会再被调用了
                return true;
            }
        });
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        // 调用与MainActivity通信的接口方法 setEditTab()实现更改Tab
        setEditTab = (HomeToMainActivity) context;

    }

    public interface HomeToMainActivity {
        /**
         * 更改 MainActivity 里面的tab布局
         * @param string
         */
        public void setEditTab(String string);

        /**
         * 恢复 MainActivity 里面的tab布局
         * @param string
         */
        public void reSetEditTab(String string);
    }





}
