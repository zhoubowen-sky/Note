package com.lingdongkuaichuan.note.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return view;
    }

    private void refreshListView() {
        folderList.clear();
        folderList = NoteDB.getAllFolders();
        Log.e(TAG, "folderlist的大小为：" + folderList.size());
        folderAdapter = new FolderAdapter(folderList, getActivity());
        listView.setAdapter(folderAdapter);
    }
}
