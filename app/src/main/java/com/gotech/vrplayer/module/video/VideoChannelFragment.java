package com.gotech.vrplayer.module.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.model.bean.HomePictureBean;
import com.gotech.vrplayer.model.bean.VideoChannelBean;
import com.gotech.vrplayer.module.video.detail.VideoDetailActivity;
import com.gotech.vrplayer.utils.DensityUtil;
import com.gotech.vrplayer.widget.CommonLoadMoreView;
import com.gotech.vrplayer.widget.SpecialLineDivider;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoChannelFragment extends BaseFragment<VideoChannelPresenter> implements IVideoChannelView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private int nPage = 0;
    private Context mContext;
    private VideoChannelAdapter mAdapter;

    // 当前fragment view是否已经初始化
    private boolean mIsInit;
    // 当前fragment view是否可见
    private boolean mIsVisible;
    // 第一次加载数据是否成功
    private boolean mHasFirstLoaded;

    private VideoChannelBean mChannel;

    public static VideoChannelFragment newInstance(VideoChannelBean channel) {
        Bundle args = new Bundle();
        args.putParcelable("ARGS", channel);
        VideoChannelFragment fragment = new VideoChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannel = getArguments().getParcelable("ARGS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        mIsInit = true;
        lazyLoad();
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            lazyLoad();
        } else {
            mIsVisible = false;
        }
    }

    private void lazyLoad() {
        if (!mIsInit || !mIsVisible || mHasFirstLoaded) {
            return;
        }
        mPresenter.getFirstLoadData();
    }

    @Override
    public void showLoading() {
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingProgress.setVisibility(View.GONE);
    }

    @Override
    public int getLoadPageNum() {
        return nPage;
    }

    @Override
    public String getChannelCode() {
        return mChannel.getTitleCode();
    }

    @Override
    public void showNetError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void showFirstLoadData(List<HomePictureBean> data) {
        mHasFirstLoaded = true;
        setItemClickListener(data);
        mAdapter.setNewData(data);
        nPage++;
    }

    @Override
    public void showLoadMoreData(List<HomePictureBean> data) {
        mAdapter.loadMoreComplete();
        if (data == null) {
            mAdapter.loadMoreEnd();
            return;
        }
        mAdapter.addData(data);
        nPage++;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_video_channel;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new VideoChannelPresenter(mContext, this);
    }

    @Override
    public void onLoadMoreRequested() {
        KLog.i("onLoadMoreRequested...");
        mPresenter.loadMore();
    }

    private void setItemClickListener(final List<HomePictureBean> data) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list保持一致
                KLog.i("onItemClick position=" + position + " " + data.get(position).getMainDescribe());
                startActivity(new Intent(mContext, VideoDetailActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mContext, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mContext, 10f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        SpecialLineDivider itemDecoration = new SpecialLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(true);
        CommonLoadMoreView loadMoreView = new CommonLoadMoreView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new VideoChannelAdapter();
        // 设置预加载
        mAdapter.setPreLoadNumber(8);
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
