/**
 * 说明：APP采用的是多Activity + 多Fragment的方式编写的
 * 其中APP启动后首页的Tab采用的是 Fragment + ViewPager
 */

package com.lingdongkuaichuan.note.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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

import static com.lingdongkuaichuan.note.db.DbHelper.DEFAULT_FOLDER_NAME;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

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
    private FragmentPagerAdapter mFragmentPagerAdapter; // 适配器
    private List<Fragment> mFragments;                  // 存储三个Fragment的List

    // Tab 上三个图片按钮
    private ImageButton img_btn_home;
    private ImageButton img_btn_folder;
    private ImageButton img_btn_user;

    // 三个Tab所在的三个布局
    private LinearLayout mHome;
    private LinearLayout mFolder;
    private LinearLayout mUser;

    public static Context mContext;

    // 顶部head布局
    private ImageButton img_btn_search_notes;    // 左上角搜索按钮
    private ImageButton img_btn_add_notes;       // 右上角添加表便签按钮
    private ImageButton img_btn_add_folders;     // 右上角添加文件夹按钮
    private TextView tv_head_title;              // 中间标题

    // SharedPreferences 相关常量
    private static final String firstStart_spf = "firstStart_spf";
    private static final String isFirstStart = "isFirstStart";



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
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, DEFAULT_FOLDER_NAME, System.currentTimeMillis() + ""));
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, "文件夹一", System.currentTimeMillis() + ""));
        NoteDB.insertOneFolder(new Folder(EditNoteActivity.NEW_NOTE_FOLDER_ID, "文件夹二", System.currentTimeMillis() + ""));

        Log.d(TAG, "APP 首次启动");
        return true;
    }

    private void setContext() {
        mContext = getApplicationContext();
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

        mHome   = (LinearLayout) findViewById(R.id.id_tab_home);
        mFolder = (LinearLayout) findViewById(R.id.id_tab_folder);
        mUser   = (LinearLayout) findViewById(R.id.id_tab_user);

        img_btn_home   = (ImageButton) findViewById(R.id.img_btn_home);
        img_btn_folder = (ImageButton) findViewById(R.id.img_btn_folder);
        img_btn_user   = (ImageButton) findViewById(R.id.img_btn_user);

        // head布局的搜索和添加按钮
        img_btn_search_notes = (ImageButton) findViewById(R.id.img_btn_search_notes);
        img_btn_add_notes    = (ImageButton) findViewById(R.id.img_btn_add_notes);
        img_btn_add_folders  = (ImageButton) findViewById(R.id.img_btn_add_folders);
        tv_head_title        = (TextView) findViewById(R.id.tv_head_title);

        // 分别实例化这三个Fragment 并放入List
        mFragments = new ArrayList<Fragment>();
        Fragment mHome   = new HomeFragment();
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
        // 给head的按钮添加监听事件
        img_btn_add_notes.setOnClickListener(this);
        img_btn_search_notes.setOnClickListener(this);
        img_btn_add_folders.setOnClickListener(this);
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
            // 按照关键字查找便签
            case R.id.img_btn_search_notes:

                break;
            // 添加便签
            case R.id.img_btn_add_notes:
                // 跳转到Note编辑界面 是一个新的Activity 跳转时传入参数用以区分是新建Note还是再次编辑已有Note
                Intent editNoteIntent = new Intent();
                editNoteIntent.putExtra(ADD_OR_EDIT_NOTE, INTENT_ADD_NOTE);
                editNoteIntent.setClass(getApplicationContext(), EditNoteActivity.class);
                MainActivity.this.startActivityForResult(editNoteIntent, 1);

                break;
            case R.id.img_btn_add_folders:
                Toast.makeText(getApplicationContext(), "点击了 添加文件夹按钮", Toast.LENGTH_SHORT).show();
                //
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                initView();
                break;
            case 2:
                initView();
                break;

        }
    }
}
