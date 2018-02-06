package com.yueyue.seeweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by HugoXie on 16/7/9.
 * <p>
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public abstract class BaseFragment extends RxFragment {

    protected boolean mIsCreateView = false;

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    protected abstract void lazyLoad();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsCreateView) {
            lazyLoad();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("current Fragment:" + this.getClass().getSimpleName(), "--onCreate--");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }

    protected void safeSetTitle(String title) {
        ActionBar appBarLayout = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("current Fragment:" + this.getClass().getSimpleName(), "--onDestroy--");
    }
}
