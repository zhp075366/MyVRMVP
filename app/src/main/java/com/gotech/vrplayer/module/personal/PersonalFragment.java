package com.gotech.vrplayer.module.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.module.personal.update.AppUpdateManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalView {

    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;
    @BindView(R.id.check_update)
    RelativeLayout mCheckUpdate;
    @BindView(R.id.help)
    RelativeLayout mHelp;
    @BindView(R.id.feedback_advice)
    RelativeLayout mFeedbackAdvice;

    private Context mContext;

    public static PersonalFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showVersionName();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new PersonalPresenter(mContext, this);
    }

    @OnClick(R.id.check_update)
    public void checkUpdate() {
        AppUpdateManager.getInstance().checkUpdate(true);
    }

    private void showVersionName() {
        String appVersion = mPresenter.getVersionName();
        mTvAppVersion.setText(appVersion);
    }
}
