package com.jay.six.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.six.R;
import com.jay.six.common.BaseFragment;
import com.jay.six.ui.adapter.TabNewsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class NewsFragment extends BaseFragment {
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.vp_news)
    ViewPager vpNews;
    Unbinder unbinder;
    private View rootView;
    private List<Fragment> list_fragment = new ArrayList<>();//定义要装fragment的列表
    private List<String> list_title = new ArrayList<>();//定义要装tab名称的列表
    private TabNewsAdapter tabNewsAdapter;//定义Tab的Adapter


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, null);
        setInflateView(rootView);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title.add("社会新闻");
        list_title.add("国内新闻");
        list_title.add("国际新闻");
        list_title.add("娱乐新闻");
        list_title.add("NBA新闻");
        list_title.add("科技新闻");
        list_title.add("创业新闻");
        list_title.add("移动互联");
        list_title.add("旅游资讯");
        list_title.add("健康知识");
        list_title.add("奇闻异事");
    }

    /**
     * @param i
     * @param tabNewsFragment
     */
    private void setType(int i, TabNewsFragment tabNewsFragment) {
        switch (i) {
            case 0:
                tabNewsFragment.setArgument("type", "social");
                break;
            case 1:
                tabNewsFragment.setArgument("type", "guonei");
                break;
            case 2:
                tabNewsFragment.setArgument("type", "world");
                break;
            case 3:
                tabNewsFragment.setArgument("type", "huabian");
                break;
            case 4:
                tabNewsFragment.setArgument("type", "nba");
                break;
            case 5:
                tabNewsFragment.setArgument("type", "keji");
                break;
            case 6:
                tabNewsFragment.setArgument("type", "startup");
                break;
            case 7:
                tabNewsFragment.setArgument("type", "mobile");
                break;
            case 8:
                tabNewsFragment.setArgument("type", "travel");
                break;
            case 9:
                tabNewsFragment.setArgument("type", "health");
                break;
            case 10:
                tabNewsFragment.setArgument("type", "qiwen");
                break;
        }
    }

    @Override
    protected void initView() {
        for (int i = 0; i < list_title.size(); i++) {
            //初始化fragment
            TabNewsFragment tabNewsFragment = new TabNewsFragment();
            //为fragment传递参数：参数为新闻类型
            setType(i, tabNewsFragment);
            list_fragment.add(tabNewsFragment);
            tabTitle.addTab(tabTitle.newTab().setText(list_title.get(i)));
        }
        tabNewsAdapter = new TabNewsAdapter(getChildFragmentManager(),list_fragment,list_title);
        vpNews.setAdapter(tabNewsAdapter);
        //为TabLayout添加adapter
        tabTitle.setupWithViewPager(vpNews);
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
