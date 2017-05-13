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
import com.jay.six.bean.ResultJoke;
import com.jay.six.bean.ResultJoke.ResultBean.Joke;
import com.jay.six.common.BaseFragment;
import com.jay.six.common.Constants;
import com.jay.six.common.ServerConfig;
import com.jay.six.common.parse.JsonMananger;
import com.jay.six.ui.adapter.JokeAdapter;
import com.jay.six.ui.widget.recyclerview.DividerItemDecoration;
import com.jay.six.ui.widget.recyclerview.ViewHolder;
import com.jay.six.ui.widget.recyclerview.interfaces.OnItemClickListener;
import com.jay.six.ui.widget.recyclerview.interfaces.OnLoadMoreListener;
import com.jay.six.utils.HttpException;
import com.jay.six.utils.LogUtils;
import com.jay.six.utils.ShareUtils;
import com.jay.six.utils.TimeUtils;
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

public class JokeFragment extends BaseFragment {
    public static final int TYPE_REFRESH = 0x01;
    public static final int TYPE_LOADMORE = 0x02;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    private ResultJoke resultJoke;
    private View rootView;
    private static int page = 1;//初始化请求第一页数据
    private JokeAdapter jokeAdapter;
    private List<Joke> datas = new ArrayList<>();
    private View loadFailed;

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
                android.R.color.holo_blue_bright, android.R.color.holo_blue_dark,
                android.R.color.holo_green_light, android.R.color.holo_green_dark,
                android.R.color.holo_orange_light, android.R.color.holo_orange_dark,
                android.R.color.holo_purple, android.R.color.holo_red_light
        );
        //初始化adapter
        jokeAdapter = new JokeAdapter(getActivity(), null, true);
        //初始化EmptyView
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, (ViewGroup) recyclerview.getParent(), false);

        loadFailed = LayoutInflater.from(getActivity()).inflate(R.layout.layout_failed, (ViewGroup) recyclerview.getParent(), false);

        jokeAdapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        jokeAdapter.setLoadingView(R.layout.layout_loading);

        //设置加载更多触发的事件监听
        jokeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                LogUtils.d("加载更多");
                request(TYPE_LOADMORE, page++);
            }


        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setAdapter(jokeAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                request(TYPE_REFRESH, page);
            }
        });
    }

    private void request(final int type, int page) {

        OkHttpUtils
                .get()
                .url(ServerConfig.API.JUHE_JOKE_QUERY)
                .addParams("sort","desc")
                .addParams("page",String.valueOf(page))
                .addParams("pagesize", Constants.PAGE_SIZE)
                .addParams("time", TimeUtils.getTime())
                .addParams("key",Constants.JUHE_JOKE_KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        jokeAdapter.removeEmptyView();
                        jokeAdapter.setLoadFailedView(loadFailed);
                        ToastUtils.shortToast(getActivity(), "加载失败！");
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (swipeRefreshLayout != null) {
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                        jokeAdapter.removeEmptyView();
                        try {
                            resultJoke = JsonMananger.jsonToBean(response, ResultJoke.class);
                            switch (type) {
                                case TYPE_REFRESH:
                                    jokeAdapter.setNewData(resultJoke.getResult().getData());
                                    break;
                                case TYPE_LOADMORE:
                                    jokeAdapter.setLoadMoreData(resultJoke.getResult().getData());
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
