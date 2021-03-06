package com.mktech.smarthome.module.video.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseActivity;
import com.mktech.smarthome.model.bean.HomePictureBean;
import com.mktech.smarthome.utils.DensityUtil;
import com.mktech.smarthome.widget.CommonLoadMoreView;
import com.mktech.smarthome.widget.SpecialLineDivider;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/12
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements IVideoDetailView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private int nPage = 0;
    private Context mContext;
    private VideoDetailAdapter mAdapter;

    @Override
    protected void onDestroy() {
        mPresenter.destroyPresenter();
        super.onDestroy();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        mContext = this;
        initRecyclerView();
    }

    @Override
    protected void initPresenterData() {
        mPresenter = new VideoDetailPresenter(this, this);
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
    public void showFirstLoadData(List<HomePictureBean> data) {
        addDetailInfoHeader();
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
    public void showNetError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void onLoadMoreRequested() {
        KLog.i("onLoadMoreRequested...");
        mPresenter.loadMore();
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mContext, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mContext, 10f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        SpecialLineDivider itemDecoration = new SpecialLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(true);
        CommonLoadMoreView loadMoreView = new CommonLoadMoreView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new VideoDetailAdapter();
        // 设置预加载
        mAdapter.setPreLoadNumber(8);
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setItemClickListener(final List<HomePictureBean> data) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list保持一致
                KLog.i("onItemClick position=" + position + " " + data.get(position).getMainDescribe());
                startActivity(new Intent(mContext, VideoDetailActivity.class));
                finish();
            }
        });
    }

    private void addDetailInfoHeader() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.layout_video_detail_header, (ViewGroup)mRecyclerView.getParent(), false);
        ImageView share = (ImageView)view.findViewById(R.id.image_view_btn_share);
        ImageView download = (ImageView)view.findViewById(R.id.image_view_btn_download);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addTask();
            }
        });
        mAdapter.addHeaderView(view);
    }
}
