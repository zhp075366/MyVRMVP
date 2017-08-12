package com.gotech.vrplayer.module.localvideo.finished;

import android.content.Context;

import com.gotech.vrplayer.module.localvideo.DownloadVideoManager;
import com.gotech.vrplayer.utils.ExAsyncTask;
import com.lzy.okgo.model.Progress;
import com.socks.library.KLog;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class FinishedTaskPresenter {

    private Context mContext;
    private IFinishedTaskView mView;
    private DownloadVideoManager mDownloadVideoManager;
    private ExAsyncTask<Void, Void, List<Progress>> mTask;
    private ExAsyncTask.OnLoadListener mLoadDBListener;

    public FinishedTaskPresenter(Context context, IFinishedTaskView view) {
        mView = view;
        mContext = context;
        mDownloadVideoManager = DownloadVideoManager.getInstance();
        initLoadDBListener();
    }

    public void destroyPresenter() {
        if (mTask != null) {
            mTask.cancle();
        }
    }

    public void getFinishedTasks() {
        // 改为异步调用
        mTask = new ExAsyncTask<>();
        mTask.setOnLoadListener(mLoadDBListener);
        mTask.executeOnExecutor(ExAsyncTask.THREAD_POOL_CACHED);
    }

    private void initLoadDBListener() {
        mLoadDBListener = new ExAsyncTask.OnLoadListener<Void, Void, List<Progress>>() {
            @Override
            public void onStart(Object taskTag) {
                mView.showLoading();
            }

            @Override
            public void onCancel(Object taskTag) {

            }

            @Override
            public void onResult(Object taskTag, List<Progress> progresses) {
                KLog.i("onResult");
                mView.hideLoading();
                mView.showFinishedTasks(progresses);
            }

            @Override
            public void onProgress(Object taskTag, Void values) {

            }

            @Override
            public List<Progress> onWorkerThread(Object taskTag, Void... params) {
                return mDownloadVideoManager.getFinishedProgress();
            }
        };
    }
}
