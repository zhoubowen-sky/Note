package com.lingdongkuaichuan.note.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.activity.MainActivity;
import com.lingdongkuaichuan.note.adapter.FolderAdapter;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.db.NoteDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/4/22.
 */

public class FolderFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private ListView listView;

    private FolderAdapter folderAdapter;

    private List<Folder> folderList = new ArrayList<Folder>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        listView = (ListView) view.findViewById(R.id.lv_folder);

        refreshListView();

        setItemClickListener();

        return view;
    }

    // 点击 长按事件监听器
    private void setItemClickListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击哪个分组，就只展示该分组下面的便签 该分组下没有则展示无

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 长按分组 重命名分组或者删除分组

                return false;
            }
        });

    }

    private void refreshListView() {
        folderList.clear();
        folderList = NoteDB.getAllFolders();
        Log.e(TAG, "folderlist的大小为：" + folderList.size());
        folderAdapter = new FolderAdapter(folderList, getActivity());
        listView.setAdapter(folderAdapter);
    }
}
