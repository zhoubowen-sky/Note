/**
 * 说明：APP采用的是多Activity + 多Fragment的方式编写的
 * 其中APP启动后首页的Tab采用的是 Fragment + ViewPager
 */

package com.lingdongkuaichuan.note.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.db.DbHelper;
import com.lingdongkuaichuan.note.db.NoteDB;
import com.lingdongkuaichuan.note.fragment.FolderFragment;
import com.lingdongkuaichuan.note.fragment.HomeFragment;
import com.lingdongkuaichuan.note.fragment.UserFragment;
import com.lingdongkuaichuan.note.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, HomeFragment.HomeToMainActivity, FolderFragment.FolderToMainActivity {

    private final String TAG = this.getClass().getSimpleName();

    // 用于keydowm back按键的响应判断
    private static boolean isResetEditTab = false;

    // 对首页三个Fragment进行编号
    private static final int FRAGMENT_HOME   = 0;
    private static final int FRAGMENT_FOLDER = 1;
    private static final int FRAGMENT_USER   = 2;

    // Intent 跳转时携带的识别参数
    public static final String INTENT_ADD_NOTE  = "add_note";  // 添加一个Note
    public static final String INTENT_EDIT_NOTE = "edit_note"; // 编辑已有的Note
    public static final String ADD_OR_EDIT_NOTE = "add_or_edit_note";
    public static final String NOTE_ID          = "note_id";   // Note 的ID


    private ViewPager mViewPager;
    private FrameLayout fl_note_list;
    private FragmentPagerAdapter mFragmentPagerAdapter; // 适配器
    private List<Fragment> mFragments;                  // 存储三个Fragment的List

    // Tab 上三个图片按钮
    private ImageButton img_btn_home;
    private ImageButton img_btn_folder;
    private ImageButton img_btn_user;
    // edit tab 上三个按钮
    private ImageButton img_btn_mark;
    private ImageButton img_btn_delete;
    private ImageButton img_btn_move_to_folder;

    // 三个Tab所在的三个布局
    private LinearLayout mHome;
    private LinearLayout mFolder;
    private LinearLayout mUser;

    // 包含两个tab的两个linearlayout
    private LinearLayout ll_note_tab;
    private LinearLayout ll_note_tab_edit;

    private LinearLayout id_tab_mark;
    private LinearLayout id_tab_delete;
    private LinearLayout id_tab_move_to_folder;


    public static Context mContext;

    // 顶部head布局
    private ImageButton img_btn_search_notes;    // 左上角搜索按钮
    private ImageButton img_btn_add_notes;       // 右上角添加表便签按钮
    private ImageButton img_btn_add_folders;     // 右上角添加文件夹按钮
    private TextView tv_head_title;              // 中间标题
    private ImageButton img_btn_back;            // 左上角返回 平时处于 gone 状态

    // SharedPreferences 相关常量
    private static final String firstStart_spf = "firstStart_spf";
    private static final String isFirstStart = "isFirstStart";

    // 移动便签到文件夹的dialog
    private AlertDialog chooseFolderDialog;
    // 移动便签到文件夹的文件夹列表
    List<Folder> chooseFolderList = new ArrayList<Folder>();
    // 存储文件夹名称的列表
    private String[] folder_items;
    // 用户选中的文件夹
    private static Folder choosedFolder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示toolebar
        setContentView(R.layout.activity_main);

        setContext();

        NoteDB.createDatebase(mContext);

        checkFirstStart();

        initView();

        initEvent();

        setPagerSelect(FRAGMENT_HOME);

    }

    /**
     * 检查是否是第一次启动APP，如果是则执行首次启动相关操作
     * @return
     */
    private boolean checkFirstStart() {
        SharedPreferences spf = getSharedPreferences(firstStart_spf,mContext.MODE_PRIVATE);
        if (!spf.getBoolean(isFirstStart, true)){
            Log.d(TAG, "APP 非首次启动");
            return false;
        }
        // SharedPreferences 存储首次启动的状态
        SharedPreferences.Editor edtior = spf.edit();
        edtior.putBoolean(isFirstStart, false);
        edtior.commit();
        // 首次启动，执行写入默认数据的操作
        // 默认给folder创建一条数据，默认文件夹
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, DbHelper.DEFAULT_FOLDER_NAME, DateUtil.getCurrentDateLine()));
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, "分组一", DateUtil.getCurrentDateLine()));
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, "分组二", DateUtil.getCurrentDateLine()));

        Log.d(TAG, "APP 首次启动");
        return true;
    }

    private void setContext() {
//        mContext = getApplicationContext();
        mContext = MainActivity.this;
    }

    /**
     * 设置默认要展示的Page/Fragment
     * @param currentItem 当前fragment的ID序号
     */
    private void setPagerSelect(int currentItem) {
        // 首先要更改Tab按钮的样式
        setTab(currentItem);
        mViewPager.setCurrentItem(currentItem);
    }

    /**
     * 初始化布局的函数
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        fl_note_list = (FrameLayout) findViewById(R.id.fl_note_list);

        mHome   = (LinearLayout) findViewById(R.id.id_tab_home);
        mFolder = (LinearLayout) findViewById(R.id.id_tab_folder);
        mUser   = (LinearLayout) findViewById(R.id.id_tab_user);
        id_tab_mark = (LinearLayout) findViewById(R.id.id_tab_mark);
        id_tab_delete = (LinearLayout) findViewById(R.id.id_tab_delete);
        id_tab_move_to_folder = (LinearLayout) findViewById(R.id.id_tab_move_to_folder);

        // 两个tab的布局
        ll_note_tab      = (LinearLayout) findViewById(R.id.ll_note_tab);
        ll_note_tab_edit = (LinearLayout) findViewById(R.id.ll_note_tab_edit);
//        ll_note_tab.setVisibility(View.GONE);
//        ll_note_tab_edit.setVisibility(View.VISIBLE);

        img_btn_home           = (ImageButton) findViewById(R.id.img_btn_home);
        img_btn_folder         = (ImageButton) findViewById(R.id.img_btn_folder);
        img_btn_user           = (ImageButton) findViewById(R.id.img_btn_user);
        img_btn_mark           = (ImageButton) findViewById(R.id.img_btn_mark);
        img_btn_delete         = (ImageButton) findViewById(R.id.img_btn_delete);
        img_btn_move_to_folder = (ImageButton) findViewById(R.id.img_btn_move_to_folder);

        // head布局的搜索和添加按钮
        img_btn_search_notes = (ImageButton) findViewById(R.id.img_btn_search_notes);
        img_btn_add_notes    = (ImageButton) findViewById(R.id.img_btn_add_notes);
        img_btn_add_folders  = (ImageButton) findViewById(R.id.img_btn_add_folders);
        tv_head_title        = (TextView) findViewById(R.id.tv_head_title);
        img_btn_back         = (ImageButton) findViewById(R.id.img_btn_back);

        // 分别实例化这三个Fragment 并放入List
        mFragments = new ArrayList<Fragment>();
        Fragment mHome   = new HomeFragment();
        {
            // 向HomeFragment 传递参数 用以区分是从MainActivity跳转进去的
            Bundle bundle = new Bundle();
            bundle.putInt(DbHelper.TABLE_NOTE_COLUMN_FOLDER_ID, 0);
            mHome.setArguments(bundle);
        }
        Fragment mFolder = new FolderFragment();
        Fragment mUser   = new UserFragment();
        mFragments.add(mHome);
        mFragments.add(mFolder);
        mFragments.add(mUser);

        // 实例化一个数据适配器，传入这个List
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        // 设置适配器
        mViewPager.setAdapter(mFragmentPagerAdapter);

        // Page页面改变时，即左右滑动，所要执行的操作
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem); // 更改Tab样式

                Log.d(TAG, "当前ViewPager页面ID为：" + currentItem);
                // 更改 head 布局以及 head上面按钮
                switch (currentItem){
                    case FRAGMENT_HOME:
                        changeHeadTitle("首页");
                        setHeadImgBtnVisible(View.VISIBLE , FRAGMENT_HOME);
//                        HomeFragment.noteList.clear();
//                        HomeFragment.noteList = NoteDB.getAllNotes(0);

                        break;
                    case FRAGMENT_FOLDER:
                        changeHeadTitle("文件夹");
                        setHeadImgBtnVisible(View.VISIBLE , FRAGMENT_FOLDER);
                        // 右上角添加note按钮gone 添加文件夹按钮 visible

                        break;
                    case FRAGMENT_USER:
                        changeHeadTitle("我的");
                        setHeadImgBtnVisible(View.GONE , FRAGMENT_USER);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 更改 head 布局 title文字的内容
     * @param string
     */
    private void changeHeadTitle(String string) {
        tv_head_title.setText(string);
    }

    /**
     * 设定head里 查找和添加按钮 是否为可见
     * @param visible
     * @param currentItem
     */
    private void setHeadImgBtnVisible(int visible , int currentItem){
        if (currentItem == FRAGMENT_HOME || currentItem == FRAGMENT_USER){
            img_btn_add_notes.setVisibility(visible);
            img_btn_search_notes.setVisibility(visible);
            img_btn_add_folders.setVisibility(View.GONE);
        }else if (currentItem == FRAGMENT_FOLDER){
            // 隐藏掉右上角添加note按钮 显示添加文件夹按钮
            img_btn_add_notes.setVisibility(View.GONE);
            img_btn_add_folders.setVisibility(visible);
            img_btn_search_notes.setVisibility(visible);
        }
    }


    /**
     * 初始化事件监听
     */
    private void initEvent() {
        // 给Tab的三个布局添加点击事件监听
        mHome.setOnClickListener(this);
        mFolder.setOnClickListener(this);
        mUser.setOnClickListener(this);
        // 给edit Tab的三个布局添加点击事件监听
        id_tab_mark.setOnClickListener(this);
        id_tab_delete.setOnClickListener(this);
        id_tab_move_to_folder.setOnClickListener(this);
        // 给head的按钮添加监听事件
        img_btn_add_notes.setOnClickListener(this);
        img_btn_search_notes.setOnClickListener(this);
        img_btn_add_folders.setOnClickListener(this);
        img_btn_back.setOnClickListener(this);

    }

    /**
     * 更改按钮的样式
     * @param currentItem 当前Page的ID序号
     */
    private void setTab(int currentItem){
        //首先还原所有Tab的样式
        resetTab();
        switch (currentItem){
            case FRAGMENT_HOME:
                img_btn_home.setBackgroundResource(R.drawable.ic_home_selected);
                break;
            case FRAGMENT_FOLDER:
                img_btn_folder.setBackgroundResource(R.drawable.ic_folder_selected);
                break;
            case FRAGMENT_USER:
                img_btn_user.setBackgroundResource(R.drawable.ic_user_selected);
                break;
        }
    }

    /**
     * 将所有按钮设置为未选中
     */
    private void resetTab() {
        img_btn_home.setBackgroundResource(R.drawable.ic_home);
        img_btn_folder.setBackgroundResource(R.drawable.ic_folder);
        img_btn_user.setBackgroundResource(R.drawable.ic_user);
    }


    @Override
    public void onClick(View v) {
        // 点击事件监听
        switch (v.getId()){
            case R.id.id_tab_home:
                setPagerSelect(FRAGMENT_HOME);
                break;
            case R.id.id_tab_folder:
                setPagerSelect(FRAGMENT_FOLDER);
                break;
            case R.id.id_tab_user:
                setPagerSelect(FRAGMENT_USER);
                break;
            case R.id.id_tab_mark:
                // 标记选中的便签，置顶
                Toast.makeText(getApplicationContext(), "id_tab_mark", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_tab_delete:
                // 删除选中的便签
                deleteNotes(getSelectedNotes(HomeFragment.noteList));
                Toast.makeText(getApplicationContext(), "已删除" + getSelectedNotes(HomeFragment.noteList).size() + "条便签", Toast.LENGTH_SHORT).show();
                // 刷新界面
                initView();
                break;
            case R.id.id_tab_move_to_folder:
                // 将选中的便签移动到指定的分组
                Toast.makeText(getApplicationContext(), "id_tab_move_to_folder", Toast.LENGTH_SHORT).show();
                // 获取listview中选中的便签 以及弹出框中选中的 folder
                showSelectFolderDialog();

                break;
            case R.id.img_btn_search_notes:
                // 按照关键字查找便签

                break;
            case R.id.img_btn_add_notes:
                // 添加便签
                // 跳转到Note编辑界面 是一个新的Activity 跳转时传入参数用以区分是新建Note还是再次编辑已有Note
                Intent editNoteIntent = new Intent();
                editNoteIntent.putExtra(ADD_OR_EDIT_NOTE, INTENT_ADD_NOTE);
                editNoteIntent.setClass(getApplicationContext(), EditNoteActivity.class);
                MainActivity.this.startActivityForResult(editNoteIntent, 1);

                break;
            case R.id.img_btn_add_folders:
                // 弹出编辑框，输入文件夹的名称
                showAddFolderDialog();
                break;
            case R.id.img_btn_back:
                // 当展示出了新的tab后，此按钮才显示出来，点击它要取消 setEditTab()做出的操作
                reSetEditTab("点击了左上角 back 按钮传入的参数");
                break;
        }
    }

    /**
     * 展示选择分组的弹出框
     */
    private void showSelectFolderDialog() {
        chooseFolderList = NoteDB.getAllFolders();
        folder_items = new String[chooseFolderList.size()];
        for (int i = 0 ; i < chooseFolderList.size(); i++){
            folder_items[i] = chooseFolderList.get(i).getName();
        }
        AlertDialog.Builder alertBuiider = new AlertDialog.Builder(this);
        alertBuiider.setTitle("请选择要移动到的分组");
        alertBuiider.setSingleChoiceItems(folder_items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, folder_items[which], Toast.LENGTH_SHORT).show();
                // 根据ID得到选中的folder对象
                choosedFolder = chooseFolderList.get(which);
                Log.v(TAG, "选中的文件夹的ID：" + choosedFolder.getId());
            }
        });
        alertBuiider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "点了确定....", Toast.LENGTH_SHORT).show();
                // 操作数据库
                moveNoteToFolder(getSelectedNotes(HomeFragment.noteList), choosedFolder);
                // 关闭弹出框
                chooseFolderDialog.dismiss();
            }
        });
        alertBuiider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "点了取消....", Toast.LENGTH_SHORT).show();
                // 关闭弹出框
                chooseFolderDialog.dismiss();
            }
        });
        chooseFolderDialog = alertBuiider.create();
        chooseFolderDialog.show();
    }

    /**
     * 将便签移动到指定的分组
     * @param noteList 便签数组
     * @param folder 文件夹
     */
    private void moveNoteToFolder(List<Note> noteList, Folder folder) {
        if(noteList.size() <= 0){
            return;
        }else {
            Log.v(TAG, "moveNoteToFolder 选中便签个数：" + noteList.size());
            NoteDB.moveNoteToFolder(noteList, folder);
        }
    }

    /**
     * 删除便签
     * @param notes
     */
    public void deleteNotes(List<Note> notes){
        if (notes.size() <= 0){
            Toast.makeText(getApplication(), "未选中标签，未执行删除操作", Toast.LENGTH_SHORT).show();
        }else {
            NoteDB.deleteNotes(notes);
        }
    }

    /**
     * 获取listview已经选中的Notes
     * @param notes
     * @return
     */
    public List<Note> getSelectedNotes(List<Note> notes){
        List<Note> selectedNotes = new ArrayList<Note>();
        for (int i = 0; i < notes.size(); i++){
            if (notes.get(i).isChecked()){
                selectedNotes.add(notes.get(i));
            }
        }
        return selectedNotes;
    }

    /**
     * 展示添加文件夹的 带编辑框的dailog
     */
    private void showAddFolderDialog() {
        final EditText ed_folder_name = new EditText(mContext);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("新建分组")
                .setView(ed_folder_name)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!(ed_folder_name.getText().toString() == null || ed_folder_name.getText().toString().equals(""))){
                    // 执行向数据表 folder 写入一条数据增加一个分组操作
                    long rows = NoteDB.insertOneFolder(new Folder(0, ed_folder_name.getText().toString(), DateUtil.getCurrentDateLine()));
                    if (rows <= 0){
                        Log.e(TAG, "分组添加失败");
                        Toast.makeText(getApplicationContext(), "分组添加失败", Toast.LENGTH_LONG).show();
                    }else {
                        Log.e(TAG, "分组添加成功");
                        initView();
                        setPagerSelect(FRAGMENT_FOLDER);
                        Toast.makeText(getApplicationContext(), "分组添加成功", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "分组名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                // INTENT_ADD_NOTE 添加新便签
                initView();
                break;
            case 2:
                // INTENT_EDIT_NOTE 编辑已有的便签
                initView();
                break;
            case 3:
                // 进入了editlistactivity
                Log.e(TAG, "进入了editlistactivity 并跳转回MainActivity");
                break;
        }
    }

    /**
     * 继承 HomeFragment 中自定义的接口 HomeToMainActivity 用于向MainActivity通信
     * @param string
     */
    @Override
    public void setEditTab(String string) {
        isResetEditTab = true;
        Log.d(TAG, "homefragment中定义的接口HomeToMainActivity 中 setEditTab 方法调用成功！传过来的参数为：" + string);
        // 隐藏原有的Tab ll_note_tab 展示新的Tab ll_note_tab_edit
        ll_note_tab.setVisibility(View.GONE);
        ll_note_tab_edit.setVisibility(View.VISIBLE);
        // 隐藏顶部添加note和查找按钮
        img_btn_add_notes.setVisibility(View.GONE);
        img_btn_search_notes.setVisibility(View.GONE);
        // 展示左上角展示返回的图片按钮
        img_btn_back.setVisibility(View.VISIBLE);
        // viewpager 不可滑动
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 设置为trunk不允许滑动
                return true;
            }
        });
    }

    /**
     * 继承 HomeFragment 中自定义的接口 HomeToMainActivity
     * @param string
     */
    @Override
    public void reSetEditTab(String string) {
        // 隐藏CheckBox
        HomeFragment.isShowCheckBox = false;
        HomeFragment.noteAdapter.notifyDataSetChanged();
        // 隐藏Tab ll_note_tab_edit 展示Tab ll_note_tab
        ll_note_tab.setVisibility(View.VISIBLE);
        ll_note_tab_edit.setVisibility(View.GONE);
        // 展示顶部添加note和查找按钮
        img_btn_add_notes.setVisibility(View.VISIBLE);
        img_btn_search_notes.setVisibility(View.VISIBLE);
        // 隐藏左上角展示返回的图片按钮
        img_btn_back.setVisibility(View.GONE);
        // viewpager 可滑动
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 设置为trunk不允许滑动
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.e(TAG, "按下了back键 onKeyDown()");
            // 此处通过一个标志位判断当前是否是长按了listview
            if (isResetEditTab){
                reSetEditTab("按下了back键 onKeyDown() 按钮传入的参数");
                isResetEditTab = false;
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void reFreshView(String string) {
        initView();
        setPagerSelect(FRAGMENT_FOLDER);
    }
}
