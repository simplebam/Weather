package com.yueyue.seeweather.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.litesuits.orm.db.assit.WhereBuilder;
import com.yueyue.seeweather.R;
import com.yueyue.seeweather.base.BaseFragment;
import com.yueyue.seeweather.common.C;
import com.yueyue.seeweather.common.utils.RxUtil;
import com.yueyue.seeweather.common.utils.Util;
import com.yueyue.seeweather.component.OrmLite;
import com.yueyue.seeweather.component.RetrofitSingleton;
import com.yueyue.seeweather.component.RxBus;
import com.yueyue.seeweather.modules.main.adapter.MultiCityAdapter;
import com.yueyue.seeweather.modules.main.domain.CityORM;
import com.yueyue.seeweather.modules.main.domain.MultiUpdateEvent;
import com.yueyue.seeweather.modules.main.domain.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by HugoXie on 16/7/9.
 * <p>
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class MultiCityFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty)
    LinearLayout mLayout;

    private MultiCityAdapter mAdapter;
    private List<Weather> mWeathers;

    private View view;

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getDefault()
                .toObservable(MultiUpdateEvent.class)
                .doOnNext(event -> multiLoad())
                .subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_multicity, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        multiLoad();
    }

    private void initView() {
        mWeathers = new ArrayList<>();
        mAdapter = new MultiCityAdapter(mWeathers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMultiCityClick(new MultiCityAdapter.onMultiCityClick() {
            @Override
            public void longClick(String city) {
                showTips(city);
            }

            @Override
            public void click(Weather weather) {
                DetailCityActivity.launch(getActivity(), weather);
            }
        });

        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_blue_bright
            );
            mRefreshLayout.setOnRefreshListener(() -> mRefreshLayout.postDelayed(this::multiLoad, 1000));
        }
    }


    private void multiLoad() {
        mWeathers.clear();
        Observable.create((ObservableOnSubscribe<CityORM>) emitter -> {
            try {
                for (CityORM cityORM : OrmLite.getInstance().query(CityORM.class)) {
                    emitter.onNext(cityORM);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).doOnSubscribe(subscription -> mRefreshLayout.setRefreshing(true))
                .map(city -> Util.replaceCity(city.getName()))
                .distinct()
                .flatMap(cityName -> RetrofitSingleton.getInstance().fetchWeather(cityName))
                .filter(weather -> !C.UNKNOWN_CITY.equals(weather.status))
                .take(3)
                .compose(RxUtil.fragmentLifecycle(this))
                .doOnNext(weather -> mWeathers.add(weather))
                .doOnComplete(() -> {
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    mLayout.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .doOnError(error -> {
                    mRefreshLayout.setRefreshing(false);//发生错误,这里也要取消
                    if (mAdapter.isEmpty() && mLayout != null) {
                        mLayout.setVisibility(View.VISIBLE);
                    }
                })
                .subscribe();
    }

    private void showTips(String city) {
        new AlertDialog.Builder(getActivity())
                .setMessage("是否删除该城市?")
                .setPositiveButton("删除", (dialog, which) -> {
                    OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", city));
                    multiLoad();
                    Snackbar.make(getView(), String.format(Locale.CHINA, "已经将%s删掉了 Ծ‸ Ծ", city), Snackbar.LENGTH_LONG)
                            .setAction("撤销", v -> {
                                OrmLite.getInstance().save(new CityORM(city));
                                multiLoad();
                            }).show();
                })
                .show();
    }
}
