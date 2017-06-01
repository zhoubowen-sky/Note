/**
 * 说明：APP采用的是多Activity + 多Fragment的方式编写的
 * 其中APP启动后首页的Tab采用的是 Fragment + ViewPager
 */

package com.lingdongkuaichuan.note.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.db.DbHelper;
import com.lingdongkuaichuan.note.db.NoteDB;
import com.lingdongkuaichuan.note.fragment.FolderFragment;
import com.lingdongkuaichuan.note.fragment.HomeFragment;
import com.lingdongkuaichuan.note.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    // 对首页三个Fragment进行编号
    private static final int FRAGMENT_HOME   = 0;
    private static final int FRAGMENT_FOLDER = 1;
    private static final int FRAGMENT_USER   = 2;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter; // 适配器
    private List<Fragment> mFragments;                  // 存储三个Fragment的List

//    private ImageButton img_btn_search;    // 左上角搜索按钮
//    private ImageButton img_btn_add_notes; //右上角添加计事按钮

    // Tab 上三个图片按钮
    private ImageButton img_btn_home;
    private ImageButton img_btn_folder;
    private ImageButton img_btn_user;

    // 三个Tab所在的三个布局
    private LinearLayout mHome;
    private LinearLayout mFolder;
    private LinearLayout mUser;

    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示title
        setContentView(R.layout.activity_main);

        setContext();

        NoteDB.createDatebase(mContext);

        initView();

        initEvent();

        setPagerSelect(FRAGMENT_HOME);

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
                // 更改Tab样式
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 给Tab的三个布局添加点击事件监听
        mHome.setOnClickListener(this);
        mFolder.setOnClickListener(this);
        mUser.setOnClickListener(this);
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
        }
    }
}
