package com.yueyue.seeweather.modules.setting.ui;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bugtags.library.Bugtags;
import com.hugo.watcher.Watcher;
import com.yueyue.seeweather.R;
import com.yueyue.seeweather.base.BaseApplication;
import com.yueyue.seeweather.common.C;
import com.yueyue.seeweather.common.utils.FileSizeUtil;
import com.yueyue.seeweather.common.utils.FileUtil;
import com.yueyue.seeweather.common.utils.RxUtil;
import com.yueyue.seeweather.common.utils.SpUtil;
import com.yueyue.seeweather.common.utils.ToastUtil;
import com.yueyue.seeweather.component.ImageLoader;
import com.yueyue.seeweather.component.RxBus;
import com.yueyue.seeweather.modules.main.domain.ChangeCityEvent;
import com.yueyue.seeweather.modules.main.ui.MainActivity;
import com.yueyue.seeweather.modules.service.AutoUpdateService;

import java.io.File;
import java.util.Locale;

import io.reactivex.Observable;

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private static String TAG = SettingFragment.class.getSimpleName();

    public static final String APP_FEEDBACK = "app_feedback";//应用反馈

    private SpUtil mSpUtil;
    private Preference mChangeIcons;
    private Preference mChangeUpdate;
    private Preference mClearCache;
    private CheckBoxPreference mNotificationType;
    private CheckBoxPreference mAnimationOnOff;
    private CheckBoxPreference mMultiCityTipsOnOff;
    private CheckBoxPreference mWatcherSwitch;

    private EditTextPreference mAppFeedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSpUtil = SpUtil.getInstance();

        mChangeIcons = findPreference(SpUtil.CHANGE_ICONS);
        mChangeUpdate = findPreference(SpUtil.AUTO_UPDATE);
        mClearCache = findPreference(SpUtil.CLEAR_CACHE);

        mAnimationOnOff = (CheckBoxPreference) findPreference(SpUtil.ANIM_START);
        mMultiCityTipsOnOff = (CheckBoxPreference) findPreference(SpUtil.MULTI_CITY_TIPS);
        mNotificationType = (CheckBoxPreference) findPreference(SpUtil.NOTIFICATION_MODEL);
        mWatcherSwitch = (CheckBoxPreference) findPreference(SpUtil.WATCHER);


        mAppFeedback = (EditTextPreference) findPreference(APP_FEEDBACK);

        mNotificationType.setChecked(
                SpUtil.getInstance().getNotificationModel() == Notification.FLAG_ONGOING_EVENT);
        mAnimationOnOff.setChecked(SpUtil.getInstance().getMainAnim());
        mMultiCityTipsOnOff.setChecked(SpUtil.getInstance().getMultiCityTips());
        mWatcherSwitch.setChecked(SpUtil.getInstance().getWatcherSwitch());
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(getContext())) {
            mWatcherSwitch.setEnabled(false);
        }
        mChangeIcons.setSummary(getResources().getStringArray(R.array.icons)[mSpUtil.getIconType()]);

        mChangeUpdate.setSummary(
                mSpUtil.getAutoUpdate() == 0 ? "禁止刷新" : "每" + mSpUtil.getAutoUpdate() + "小时更新");
        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(C.NET_CACHE));

        mChangeIcons.setOnPreferenceClickListener(this);
        mChangeUpdate.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
        mNotificationType.setOnPreferenceChangeListener(this);
        mAnimationOnOff.setOnPreferenceChangeListener(this);
        mMultiCityTipsOnOff.setOnPreferenceChangeListener(this);
        mWatcherSwitch.setOnPreferenceChangeListener(this);
        mAppFeedback.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mChangeIcons == preference) {
            showIconDialog();
        } else if (mClearCache == preference) {
            ImageLoader.clear(getActivity());
            Observable.just(FileUtil.delete(new File(C.NET_CACHE)))
                    .filter(aBoolean -> aBoolean)
                    .compose(RxUtil.io())
                    .doOnNext(aBoolean -> {
                        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(C.NET_CACHE));
                        Snackbar.make(getView(), "缓存已清除", Snackbar.LENGTH_SHORT).show();
                    })
                    .subscribe();
        } else if (mChangeUpdate == preference) {
            showUpdateDialog();
        } else if (mWatcherSwitch == preference) {
            if (mWatcherSwitch.isChecked()) {
                Watcher.getInstance().start(BaseApplication.getAppContext());
            }
        }
        return true;
    }

    private void showIconDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_icon, (ViewGroup) getActivity().findViewById(R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        LinearLayout layoutTypeOne = (LinearLayout) dialogLayout.findViewById(R.id.layout_one);
        layoutTypeOne.setClickable(true);
        RadioButton radioTypeOne = (RadioButton) dialogLayout.findViewById(R.id.radio_one);
        LinearLayout layoutTypeTwo = (LinearLayout) dialogLayout.findViewById(R.id.layout_two);
        layoutTypeTwo.setClickable(true);
        RadioButton radioTypeTwo = (RadioButton) dialogLayout.findViewById(R.id.radio_two);
        TextView done = (TextView) dialogLayout.findViewById(R.id.done);

        radioTypeOne.setClickable(false);
        radioTypeTwo.setClickable(false);

        alertDialog.show();

        switch (mSpUtil.getIconType()) {
            case 0:
                radioTypeOne.setChecked(true);
                radioTypeTwo.setChecked(false);
                break;
            case 1:
                radioTypeOne.setChecked(false);
                radioTypeTwo.setChecked(true);
                break;
        }

        layoutTypeOne.setOnClickListener(v -> {
            radioTypeOne.setChecked(true);
            radioTypeTwo.setChecked(false);
        });

        layoutTypeTwo.setOnClickListener(v -> {
            radioTypeOne.setChecked(false);
            radioTypeTwo.setChecked(true);
        });

        done.setOnClickListener(v -> {
            mSpUtil.setIconType(radioTypeOne.isChecked() ? 0 : 1);
            String[] iconsText = getResources().getStringArray(R.array.icons);
            mChangeIcons.setSummary(radioTypeOne.isChecked() ? iconsText[0] :
                    iconsText[1]);
            alertDialog.dismiss();
            Snackbar.make(getView(), "切换成功,重启应用生效",
                    Snackbar.LENGTH_INDEFINITE).setAction("重启",
                    v1 -> {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        RxBus.getDefault().post(new ChangeCityEvent());
                    }).show();
        });
    }

    private void showUpdateDialog() {
        //将 SeekBar 放入 Dialog 的方案 http://stackoverflow.com/questions/7184104/how-do-i-put-a-seek-bar-in-an-alert-dialog
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_update, (ViewGroup) getActivity().findViewById(
                R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        final SeekBar mSeekBar = (SeekBar) dialogLayout.findViewById(R.id.time_seekbar);
        final TextView tvShowHour = (TextView) dialogLayout.findViewById(R.id.tv_showhour);
        TextView tvDone = (TextView) dialogLayout.findViewById(R.id.done);

        mSeekBar.setMax(24);
        mSeekBar.setProgress(mSpUtil.getAutoUpdate());
        tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
        alertDialog.show();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvDone.setOnClickListener(v -> {
            mSpUtil.setAutoUpdate(mSeekBar.getProgress());
            mChangeUpdate.setSummary(
                    mSpUtil.getAutoUpdate() == 0 ? "禁止刷新"
                            : String.format(Locale.CHINA, "每%d小时更新", mSpUtil.getAutoUpdate()));
            getActivity().startService(new Intent(getActivity(), AutoUpdateService.class));
            alertDialog.dismiss();
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mAnimationOnOff == preference) {
            SpUtil.getInstance().setMainAnim((Boolean) newValue);
        } else if (mMultiCityTipsOnOff == preference) {
            SpUtil.getInstance().setMultiCityTips((Boolean) newValue);
        } else if (mNotificationType == preference) {
            SpUtil.getInstance().setNotificationModel(
                    (boolean) newValue ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL);
        } else if (mAppFeedback == preference) {
            if (TextUtils.isEmpty((CharSequence) newValue)) return true;
            Bugtags.sendFeedback((String) newValue);
            ToastUtil.showShort(getString(R.string.thanks_for_feedback));
        }
        return true;
    }
}
