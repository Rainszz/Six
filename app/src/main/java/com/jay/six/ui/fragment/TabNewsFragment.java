package com.jay.six.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.jay.six.R;
import com.jay.six.bean.News;
import com.jay.six.bean.ResultNews;
import com.jay.six.common.BaseFragment;
import com.jay.six.common.Constants;
import com.jay.six.common.ServerConfig;
import com.jay.six.common.parse.JsonMananger;
import com.jay.six.ui.adapter.NewsRefreshAdapter;
import com.jay.six.ui.widget.recyclerview.ViewHolder;
import com.jay.six.ui.widget.recyclerview.interfaces.OnItemClickListener;
import com.jay.six.ui.widget.recyclerview.interfaces.OnLoadMoreListener;
import com.jay.six.utils.HttpException;
import com.jay.six.utils.LogUtils;
import com.jay.six.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public class TabNewsFragment extends BaseFragment {
    public static final int TYPE_REFRESH = 0x01;
    public static final int TYPE_LOADMORE = 0x02;
    View rootView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ResultNews resultNews;
    private List<News> datas = new ArrayList<>();

    private static int page = 0;//初始化请求第一页数据

    private boolean isFailed = true;

    private NewsRefreshAdapter newsRefreshAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recyclerview, null);
        setInflateView(rootView);
        unbinder = ButterKnife.bind(this, rootView);
        page = 1;
        return rootView;
    }


    @Override
    protected void initData() {
        request(TYPE_REFRESH, 1);
//        getAsynHttp();
    }

    @Override
    protected void initView() {
        //初始化adapter
        newsRefreshAdapter = new NewsRefreshAdapter(getActivity(), null, true);

        //初始化EmptyView
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, (ViewGroup) recyclerview.getParent(), false);
        newsRefreshAdapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        newsRefreshAdapter.setLoadingView(R.layout.layout_loading);
        //设置加载更多触发的事件监听
        newsRefreshAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                request(TYPE_LOADMORE, page++);
            }


        });
        //设置item点击事件监听
        newsRefreshAdapter.setOnItemClickListener(new OnItemClickListener<News>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, News data, int position) {
                ToastUtils.shortToast(getActivity(), data.getTitle());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setAdapter(newsRefreshAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request(TYPE_REFRESH, 1);
            }
        });
    }

    private void request(final int type, int page){

        OkHttpUtils
                .get()
                .url(ServerConfig.getUrl(getStringArgument("type")))
                .addParams("key", Constants.TIANXING_API_KEY)
                .addParams("num", Constants.PAGE_NUM)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.shortToast(getActivity(), "加载失败！");
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        newsRefreshAdapter.removeEmptyView();
                        LogUtils.d(response);
                        try {
                            resultNews = JsonMananger.jsonToBean(response, ResultNews.class);
                            switch (type){
                                case TYPE_REFRESH:
                                    newsRefreshAdapter.setNewData(resultNews.getNewslist());
                                    break;
                                case TYPE_LOADMORE:
                                    newsRefreshAdapter.setLoadMoreData(resultNews.getNewslist());
                                    break;
                            }
                        } catch (HttpException e) {
                            LogUtils.e(e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void initListener() {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
