package com.mktech.smarthome.module.home;

import android.content.Context;

import com.mktech.smarthome.event.HomeMessageEvent;
import com.mktech.smarthome.model.bean.HomeAdBean;
import com.mktech.smarthome.model.bean.HomeCategoryBean;
import com.mktech.smarthome.model.bean.HomeMultipleItemBean;
import com.mktech.smarthome.model.HomeModel;
import com.mktech.smarthome.module.local.DownloadVideoManager;
import com.mktech.smarthome.module.video.detail.AddTaskResult;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomePresenter {

    private Context mContext;
    private HomeModel mModel;
    private IHomeView mView;

    public HomePresenter(Context context, IHomeView view) {
        mView = view;
        mContext = context;
        mModel = new HomeModel();
        EventBus.getDefault().register(this);
    }

    public void destroyPresenter() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeMessageEvent(HomeMessageEvent<HomeMultipleItemBean> event) {
        HomeMessageEvent.EVENT_TYPE eventType = event.getEventType();
        if (eventType == HomeMessageEvent.EVENT_TYPE.TYPE_FIRST_LOAD_DATA) {
            mView.hideLoading();
            mView.showFirstLoadData(event.getData());
        }
    }

    // 异步请求网络数据
    public void getFirstLoadData() {
        mView.showLoading();
        mModel.getFirstLoadData();
    }

    // 测试用的同步接口
    public List<HomeCategoryBean> getCategoryData() {
        return mModel.getCategoryData();
    }

    // 测试用的同步接口
    public List<HomeAdBean> getViewPagerData() {
        return mModel.getViewPagerData();
    }

    // 请求换一换的数据，同步接口，实际情况：第一次加载就已加载了所有后续需要换一换的数据
    public List<HomeMultipleItemBean> getReplaceRecommendData() {
        // 根据RecommendID去获取对应的推荐数据
        int id = mView.getRecommendID();
        KLog.i("RecommendID:" + id);
        return mModel.getReplaceRecommendData();
    }

    // 测试用，默认增加2个下载任务
    public AddTaskResult addTask() {
        return DownloadVideoManager.getInstance().addOneTask();
    }
}
