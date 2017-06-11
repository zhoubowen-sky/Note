package com.lingdongkuaichuan.note.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.activity.EditNoteActivity;
import com.lingdongkuaichuan.note.activity.MainActivity;
import com.lingdongkuaichuan.note.adapter.FolderAdapter;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.db.DbHelper;
import com.lingdongkuaichuan.note.db.NoteDB;
import com.lingdongkuaichuan.note.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lingdongkuaichuan.note.db.DbHelper.DEFAULT_FOLDER_NAME;

/**
 * Created by 周博文 on 2017/4/22.
 */

public class FolderFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    public static ListView listView;

    public static FolderAdapter folderAdapter;

    public static List<Folder> folderList = new ArrayList<Folder>();

    // 上下文菜单项常量
    private static final int MENU_RENAME = Menu.FIRST;

    private static final int MENU_DELETE = Menu.FIRST + 1;

    public static final String FOLDER_ID = "folder_id";

    private FolderToMainActivity refreshListView;

    private Folder newFolder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        listView = (ListView) view.findViewById(R.id.lv_folder);

        refreshListView();

        setItemClickListener();
        // 给listview注册上下文菜单
        registerForContextMenu(listView);

        return view;
    }

    // 点击 / 长按事件监听器
    private void setItemClickListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击哪个分组，就只展示该分组下面的便签 该分组下没有则展示无
                turnPageToNoteList(folderList.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 长按显示上下文菜单 重命名分组或者删除分组
                listView.showContextMenu();
                return false;
            }
        });

    }

    /**
     * 跳转到 Note列表界面
     */
    private void turnPageToNoteList(Folder folder) {
        Log.e(TAG, "点击了  " + folder.getName() + "  文件夹");

        // 采用 HomeFragment 复用实现跳转 notelist界面
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DbHelper.TABLE_NOTE_COLUMN_FOLDER_ID,folder.getId());
        homeFragment.setArguments(bundle);
        FragmentManager fragmentManager         = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_note_list, homeFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public static void refreshListView() {
        folderList.clear();
        folderList = NoteDB.getAllFolders();
        Log.e("FolderFragment", "folderlist的大小为：" + folderList.size());
        folderAdapter = new FolderAdapter(folderList, MainActivity.mContext);
        listView.setAdapter(folderAdapter);
    }

    /**
     * 重命名文件夹
     * @param folder
     */
    private void reNameFolder(Folder folder){
        if (NoteDB.reNameFolder(folder)){
            Log.v(TAG, "文件夹重命名成功，新名称为：" + folder.getName());
        }else {
            Log.v(TAG, "文件夹重命名失败");
            Toast.makeText(getActivity(), "文件夹重命名失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除一个分组 同时删除该分组下的所有便签
     * @param folder
     */
    private void deleteFolder(Folder folder){
        NoteDB.deleteOneFolderAndNotes(folder);
    }

    private void reNameFolderDialog(final Folder folder) {
        newFolder = folder;
        final EditText ed_folder_name = new EditText(getActivity());
        // 将文件夹原来的名称填充进去
        ed_folder_name.setText(folder.getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("重命名分组")
                .setView(ed_folder_name)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "重命名分组 编辑框输入内容：" + ed_folder_name.getText().toString());
                if (ed_folder_name.getText().toString() == "" || ed_folder_name.getText().toString().equals("") || ed_folder_name.getText().toString() == null){
                    Toast.makeText(getActivity(), "文件夹名称不能为空", Toast.LENGTH_LONG).show();
                }else {
                    newFolder.setName(ed_folder_name.getText().toString());
                    reNameFolder(newFolder);
                    refreshListView();
                }
            }
        });
        builder.show();
    }

    // 创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("编辑分组");
        menu.add(0, MENU_RENAME, Menu.NONE, "重命名");
        menu.add(0, MENU_DELETE, Menu.NONE, "删除分组");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 获取被选中的 item 的信息
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // listview里面item的信息
        Log.v(TAG, "上下文菜单 item seleted ID = "+ menuInfo.id + " position = " + menuInfo.position);
        Folder folder_item = folderList.get(menuInfo.position);
        switch(item.getItemId()) {
            case MENU_RENAME:
                // 弹出编辑框 让用户填写新的文件夹名称
                if (folder_item.getName().equals(DEFAULT_FOLDER_NAME)){
                    // 禁止重命名
                    Toast.makeText(getActivity(), DEFAULT_FOLDER_NAME + " 不能重命名", Toast.LENGTH_LONG).show();
                }else {
                    reNameFolderDialog(folder_item);
                }
                break;
            case MENU_DELETE:
                Log.v(TAG, "删除 item item.getItemId() = "+ item.getItemId());
                if (folder_item.getName().equals(DEFAULT_FOLDER_NAME)){
                    // 禁止删除
                    Toast.makeText(getActivity(), DEFAULT_FOLDER_NAME + " 不能删除", Toast.LENGTH_LONG).show();
                }else {
                    deleteFolder(folder_item);
                    refreshListView();
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        // 调用与MainActivity通信的接口方法 setEditTab()实现更改Tab
        refreshListView = (FolderToMainActivity) context;

    }

    public interface FolderToMainActivity{
        /**
         * 刷新MainActivity view
         * @param string
         */
        public void reFreshView(String string);
    }


}
