package com.jay.six.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.six.R;
import com.jay.six.bean.ResultNews;
import com.jay.six.bean.ResultNews.ResBodyBean.PageBean.News;
import com.jay.six.common.BaseFragment;
import com.jay.six.common.Constants;
import com.jay.six.common.ServerConfig;
import com.jay.six.common.parse.JsonMananger;
import com.jay.six.ui.activity.NewsDetailActivity;
import com.jay.six.ui.adapter.NewsRefreshAdapter;
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

    private static int page = 1;//初始化请求第一页数据

    private boolean isFailed = true;

    private View loadFailed ;

    private NewsRefreshAdapter newsRefreshAdapter;

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
        page = 1;
        request(TYPE_REFRESH, page);
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
        newsRefreshAdapter = new NewsRefreshAdapter(getActivity(), null, true);

        //初始化EmptyView
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, (ViewGroup) recyclerview.getParent(), false);

        loadFailed = LayoutInflater.from(getActivity()).inflate(R.layout.layout_failed, (ViewGroup) recyclerview.getParent(), false);

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


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        //设置item点击事件监听
        newsRefreshAdapter.setOnItemClickListener(new OnItemClickListener<News>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, News data, int position) {
                startActivity("newsDetail",newsRefreshAdapter.getItem(position),NewsDetailActivity.class);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                request(TYPE_REFRESH, page);
            }
        });

        recyclerview.setAdapter(newsRefreshAdapter);
    }

    private void request(final int type, int page){

        OkHttpUtils
                .get()
                .url(ServerConfig.API.SHOW_API_NEWS_QUERY)
                .addParams("showapi_appid", Constants.SHOW_API_ID)
                .addParams("showapi_sign",Constants.SHOW_API_SECRET)
                .addParams("channelId", getStringArgument("channelId"))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        newsRefreshAdapter.removeEmptyView();
                        newsRefreshAdapter.setLoadFailedView(loadFailed);
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
                        newsRefreshAdapter.removeEmptyView();
                        try {
                            resultNews = JsonMananger.jsonToBean(response, ResultNews.class);
                            switch (type){
                                case TYPE_REFRESH:
                                    newsRefreshAdapter.setNewData(resultNews.getShowapi_res_body().getPagebean().getContentlist());
                                    break;
                                case TYPE_LOADMORE:
                                    newsRefreshAdapter.setLoadMoreData(resultNews.getShowapi_res_body().getPagebean().getContentlist());
                                    break;
                            }
                        } catch (HttpException e) {
                            LogUtils.e(e.getMessage());
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
