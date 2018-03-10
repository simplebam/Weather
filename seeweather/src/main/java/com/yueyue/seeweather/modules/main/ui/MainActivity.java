package com.yueyue.seeweather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yueyue.seeweather.R;
import com.yueyue.seeweather.base.BaseActivity;
import com.yueyue.seeweather.common.utils.DoubleClickExit;
import com.yueyue.seeweather.common.utils.RxDrawer;
import com.yueyue.seeweather.common.utils.SpUtil;
import com.yueyue.seeweather.common.utils.ToastUtil;
import com.yueyue.seeweather.common.utils.Util;
import com.yueyue.seeweather.component.OrmLite;
import com.yueyue.seeweather.component.RxBus;
import com.yueyue.seeweather.modules.about.ui.AboutActivity;
import com.yueyue.seeweather.modules.address.ui.AddressCheckActivity;
import com.yueyue.seeweather.modules.main.adapter.HomePagerAdapter;
import com.yueyue.seeweather.modules.main.domain.ChangeCityEvent;
import com.yueyue.seeweather.modules.main.domain.CityORM;
import com.yueyue.seeweather.modules.main.domain.MultiUpdateEvent;
import com.yueyue.seeweather.modules.service.AutoUpdateService;
import com.yueyue.seeweather.modules.setting.ui.SettingActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MULTI_CITY_REQUEST_CODE = 666;
    private static final int CHANGE_CITY_REQUEST_CODE = 777;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private MainFragment mMainFragment;
    private MultiCityFragment mMultiCityFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDrawer();
        initIcon();
        startService(new Intent(this, AutoUpdateService.class));

    }


    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initIcon();
    }

    /**
     * 初始化基础View
     */
    private void initView() {
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(v -> showShareDialog());
        HomePagerAdapter mAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mMainFragment = new MainFragment();
        mMultiCityFragment = new MultiCityFragment();
        mAdapter.addTab(mMainFragment, "主页面");
        mAdapter.addTab(mMultiCityFragment, "多城市");
        mViewPager.setAdapter(mAdapter);
        FabVisibilityChangedListener fabVisibilityChangedListener = new FabVisibilityChangedListener();
        mTabLayout.setupWithViewPager(mViewPager, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mFab.isShown()) {
                    fabVisibilityChangedListener.position = position;
                    mFab.hide(fabVisibilityChangedListener);
                } else {
                    changeFabState(position);
                    mFab.show();
                }
            }
        });


    }

    private class FabVisibilityChangedListener extends FloatingActionButton.OnVisibilityChangedListener {

        private int position;

        @Override
        public void onHidden(FloatingActionButton fab) {
            changeFabState(position);
            fab.show();
        }
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
            mNavView.inflateHeaderView(R.layout.nav_header_main);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this,
                            mDrawerLayout,
                            mToolbar,
                            R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    /**
     * 初始化 Icons
     */
    private void initIcon() {
        if (SpUtil.getInstance().getIconType() == 0) {
            SpUtil.getInstance().putInt("未知", R.mipmap.none);
            SpUtil.getInstance().putInt("晴", R.mipmap.type_one_sunny);
            SpUtil.getInstance().putInt("阴", R.mipmap.type_one_cloudy);
            SpUtil.getInstance().putInt("多云", R.mipmap.type_one_cloudy);
            SpUtil.getInstance().putInt("少云", R.mipmap.type_one_cloudy);
            SpUtil.getInstance().putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
            SpUtil.getInstance().putInt("小雨", R.mipmap.type_one_light_rain);
            SpUtil.getInstance().putInt("中雨", R.mipmap.type_one_light_rain);
            SpUtil.getInstance().putInt("大雨", R.mipmap.type_one_heavy_rain);
            SpUtil.getInstance().putInt("阵雨", R.mipmap.type_one_thunderstorm);
            SpUtil.getInstance().putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
            SpUtil.getInstance().putInt("霾", R.mipmap.type_one_fog);
            SpUtil.getInstance().putInt("雾", R.mipmap.type_one_fog);
        } else {
            SpUtil.getInstance().putInt("未知", R.mipmap.none);
            SpUtil.getInstance().putInt("晴", R.mipmap.type_two_sunny);
            SpUtil.getInstance().putInt("阴", R.mipmap.type_two_cloudy);
            SpUtil.getInstance().putInt("多云", R.mipmap.type_two_cloudy);
            SpUtil.getInstance().putInt("少云", R.mipmap.type_two_cloudy);
            SpUtil.getInstance().putInt("晴间多云", R.mipmap.type_two_cloudytosunny);
            SpUtil.getInstance().putInt("小雨", R.mipmap.type_two_light_rain);
            SpUtil.getInstance().putInt("中雨", R.mipmap.type_two_rain);
            SpUtil.getInstance().putInt("大雨", R.mipmap.type_two_rain);
            SpUtil.getInstance().putInt("阵雨", R.mipmap.type_two_rain);
            SpUtil.getInstance().putInt("雷阵雨", R.mipmap.type_two_thunderstorm);
            SpUtil.getInstance().putInt("霾", R.mipmap.type_two_haze);
            SpUtil.getInstance().putInt("雾", R.mipmap.type_two_fog);
            SpUtil.getInstance().putInt("雨夹雪", R.mipmap.type_two_snowrain);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        RxDrawer.close(mDrawerLayout)
                .doOnNext(o -> {
                    switch (item.getItemId()) {
                        case R.id.nav_set:
                            SettingActivity.launch(MainActivity.this);
                            break;
                        case R.id.nav_about:
                            AboutActivity.launch(MainActivity.this);
                            break;
                        case R.id.nav_city:
                            mViewPager.setCurrentItem(0);//需要回到MainFragment
                            showPicker(false);
                            break;
                        case R.id.nav_multi_cities:
                            mViewPager.setCurrentItem(1);
                            break;
                    }
                })
                .subscribe();
        return false;
    }

    private void changeFabState(int position) {
        if (position == 1) {
            mFab.setImageResource(R.drawable.ic_add_24dp);
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)));
            mFab.setOnClickListener(v -> {
//                Intent intent = new Intent(MainActivity.this, ChoiceCityActivity.class);
//                intent.putExtra(C.MULTI_CHECK, true);
//                CircularAnimUtil.startActivity(MainActivity.this, intent, mFab, R.color.colorPrimary);
                if (SpUtil.getInstance().getMultiCityTips()) {
                    showTips();
                } else {
                    showPicker(true);
                }

            });
        } else {
            mFab.setImageResource(R.drawable.ic_favorite);
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorAccent)));
            mFab.setOnClickListener(v -> showShareDialog());
        }
    }

    private void showShareDialog() {
        // wait to do
    }

    private void showTips() {
        new AlertDialog.Builder(this)
                .setTitle("多城市管理模式")
                .setMessage("您现在是多城市管理模式,直接点击即可新增城市.如果暂时不需要添加,\n" +
                        "因为 api 次数限制的影响,多城市列表最多三个城市.(๑′ᴗ‵๑)")
                .setPositiveButton("明白", (dialog, which) -> {
                    if (dialog != null) dialog.dismiss();
                    showPicker(true);
                })
                .setNegativeButton("不再提示", (dialog, which) -> {
                    SpUtil.getInstance().setMultiCityTips(false);
                    showPicker(true);
                })
                .setCancelable(false)
                .show();
    }


    private void showPicker(boolean isMultiCity) {
        Intent intent = new Intent(this, AddressCheckActivity.class);
        int requestCode = isMultiCity ? MULTI_CITY_REQUEST_CODE : CHANGE_CITY_REQUEST_CODE;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case MULTI_CITY_REQUEST_CODE:
                    String cityStr = Util.replaceCity(AddressCheckActivity.parse(data));
                    OrmLite.getInstance().save(new CityORM(cityStr));
                    RxBus.getDefault().post(new MultiUpdateEvent());
                    break;
                case CHANGE_CITY_REQUEST_CODE:
                    String cityStr1 = Util.replaceCity(AddressCheckActivity.parse(data));
                    SpUtil.getInstance().setCityName(cityStr1);
                    RxBus.getDefault().post(new ChangeCityEvent());
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                ToastUtil.showShort(getString(R.string.double_exit));
            } else {
                finish();
            }
        }
    }
}
