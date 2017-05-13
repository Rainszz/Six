package com.jay.six.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.jay.six.R;
import com.jay.six.bean.Picture;
import com.jay.six.bean.ResultNews;
import com.jay.six.common.BaseFragment;
import com.jay.six.common.Constants;
import com.jay.six.common.ServerConfig;
import com.jay.six.common.parse.JsonMananger;
import com.jay.six.ui.activity.NewsDetailActivity;
import com.jay.six.ui.adapter.NewsRefreshAdapter;
import com.jay.six.ui.adapter.PictureAdapter;
import com.jay.six.ui.widget.recyclerview.DividerGridItemDecoration;
import com.jay.six.ui.widget.recyclerview.DividerItemDecoration;
import com.jay.six.ui.widget.recyclerview.ViewHolder;
import com.jay.six.ui.widget.recyclerview.interfaces.OnItemClickListener;
import com.jay.six.ui.widget.recyclerview.interfaces.OnLoadMoreListener;
import com.jay.six.utils.HttpException;
import com.jay.six.utils.LogUtils;
import com.jay.six.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class PicFragment extends BaseFragment {
    public static final int TYPE_REFRESH = 0x01;
    public static final int TYPE_LOADMORE = 0x02;
    private static int startNum = 0;//初始化请求从第0个数据开始
    private static int rowNum = 10;//每次请求10条数据
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    private View rootView;
    private View loadFailed;
    private PictureAdapter adapter;
    private List<Picture> datas = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recyclerview, null);
        setInflateView(rootView);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        startNum = 0;
        request(TYPE_REFRESH, startNum);
    }

    @Override
    protected void initView() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,android.R.color.holo_orange_dark,
                android.R.color.holo_purple,android.R.color.holo_red_light
        );
        //初始化adapter
        adapter = new PictureAdapter(getActivity(), null, true);

        //初始化EmptyView
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, (ViewGroup) recyclerview.getParent(), false);

        loadFailed = LayoutInflater.from(getActivity()).inflate(R.layout.layout_failed, (ViewGroup) recyclerview.getParent(), false);

        adapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        adapter.setLoadingView(R.layout.layout_loading);

        //设置加载更多触发的事件监听
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                startNum += rowNum;
                request(TYPE_LOADMORE, startNum);
            }


        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(staggeredGridLayoutManager);

        recyclerview.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startNum = 0;
                request(TYPE_REFRESH, startNum);
            }
        });

        recyclerview.setAdapter(adapter);
    }

    private void request(final int type, int startNum){

        OkHttpUtils
                .get()
                .url(ServerConfig.API.BAIDU_API_IMG)
                .addParams("col", "美女")
                .addParams("tag","小清新")
                .addParams("from", "1")
                .addParams("pn", String.valueOf(startNum))
                .addParams("rn",String.valueOf(rowNum))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        adapter.removeEmptyView();
                        adapter.setLoadFailedView(loadFailed);
                        ToastUtils.shortToast(getActivity(), "加载失败！");
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(swipeRefreshLayout != null){
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                        adapter.removeEmptyView();
                        datas = JSON.parseObject(response).getJSONArray("imgs").toJavaList(Picture.class);
                        switch (type){
                            case TYPE_REFRESH:
                                adapter.setNewData(datas);
                                break;
                            case TYPE_LOADMORE:
                                adapter.setLoadMoreData(datas);
                                break;
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
