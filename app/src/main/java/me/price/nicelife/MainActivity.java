package me.price.nicelife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.ArrayList;

import me.price.nicelife.bean.PlanList;
import me.price.nicelife.db.PlanListDao;
import me.price.nicelife.fragments.AlarmListFragment;
import me.price.nicelife.fragments.CalendarFragment;
import me.price.nicelife.fragments.CountdownFragment;
import me.price.nicelife.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentManager fragmentManager;
    NavigationView navigationView;

    Fragment mainFragment;
    Fragment countdownFragment;
    Fragment calendarFragment;

    ArrayList<FragmentData> fragmentDataList = new ArrayList<>();

    private void initAll() {
        findAll();
        initToolbar();
        initDrawerLayout();
        initDB();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("今日计划");
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.menu_open,
                R.string.menu_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void findAll() {

        mainFragment = MainFragment.newInstance();
        countdownFragment = CountdownFragment.newInstance();
        calendarFragment = CalendarFragment.newInstance();
    }

    private void setNavigationView() {

        navigationView = (NavigationView) findViewById(R.id.navigtion_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        switch (id) {

                            case R.id.nav_all_plan:
                                addNewFragment(mainFragment, "今日计划", menuItem);
                                break;
                            case R.id.nav_countdown:
                                addNewFragment(countdownFragment, "倒计时", menuItem);
                                break;
                            case R.id.nav_calendar:
                                addNewFragment(AlarmListFragment.newInstance(), "闹钟提醒", menuItem);
                                break;
                            case R.id.nav_add_list:
                                Snackbar.make(drawerLayout, "添加清单", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_list_manager:
                                Snackbar.make(drawerLayout, "管理清单", Snackbar.LENGTH_SHORT).show();
                                break;
                            default:;
                        }
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    private void clearNavigationSelected() {
        for(int i=0;i<navigationView.getMenu().size();i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void setSelected(MenuItem menuItem) {
        clearNavigationSelected();
        menuItem.setChecked(true);
    }

    private void setFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void setFragment(Fragment fragment, String title, MenuItem menuItem) {
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();
        setTitle(title);
        if(menuItem != null)
            setSelected(menuItem);
    }

    private void setFragment(Fragment fragment, String title) {
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();
        setTitle(title);
        clearNavigationSelected();
    }

    private void initDB() {
        if(new PlanListDao(getContext()).getPlanListAll().size() <= 0) {
            new PlanListDao(getContext()).add(new PlanList("default", 0, 0));
        }
    }

    public void setFragment(FragmentData data) {
        setFragment(data.getFragment(), data.getTitle(), data.getMenuItem());
    }

    protected String getName() {
        return MainActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();

        setNavigationView();

        setFragmentManager();

        addNewFragment(mainFragment, "今日计划", navigationView.getMenu().getItem(0));
    }

    public void addNewFragment(Fragment fragment, String title, MenuItem menuItem) {
        setFragment(fragment, title, menuItem);
        fragmentDataList.add(FragmentData.newInstance(fragment, title, menuItem));
    }

    public void addNewFragment(Fragment fragment, String title) {
        setFragment(fragment, title);
        fragmentDataList.add(FragmentData.newInstance(fragment, title, null));
    }

    public void backFragment() {
        int size = fragmentDataList.size();
        if(size > 1) {
            setFragment(fragmentDataList.get(size - 2));
            fragmentDataList.remove(size - 1);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK) {

            int size = fragmentDataList.size();

            if (size > 1) {

                backFragment();

                return false;

            } else {

                AlertDialog isExit = new AlertDialog.Builder(this)
                        .setTitle("系统提示")
                        .setMessage("是否确认退出")
                        .setPositiveButton("确认", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();

                isExit.show();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public Context getContext() {
        return this;
    }
}


class FragmentData {

    Fragment fragment;
    String title;
    MenuItem menuItem;

    FragmentData(Fragment fragment, String title, MenuItem item) {
        this.fragment = fragment;
        this.title = title;
        this.menuItem = item;
    }

    public static FragmentData newInstance(Fragment fragment, String title, MenuItem item) {

        return new FragmentData(fragment, title, item);
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
    public Fragment getFragment() {
        return this.fragment;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setMenuItem(MenuItem item) {
        this.menuItem = item;
    }
    public MenuItem getMenuItem() {
        return this.menuItem;
    }
}