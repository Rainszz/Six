package com.jay.six.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jay.six.R;
import com.jay.six.bean.ResultNews.ResBodyBean.PageBean.News;
import com.jay.six.common.BaseActivity;
import com.jay.six.common.ServerConfig;
import com.jay.six.utils.ShareUtils;
import com.jay.six.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jayli on 2017/5/6 0006.
 */

public class NewsDetailActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;
    private String url = "";
    private News news;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        ButterKnife.bind(this);

        setTitle(getString(R.string.newsDetail));
        setRightText(getString(R.string.share));
        setEvent();
        initListstenr();
    }

    private void initListstenr() {
        setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.getImageurls().size() > 0){
                    ShareUtils.showShare(news.getTitle(),news.getDesc(),news.getLink(),news.getImageurls().get(0).getUrl());
                }else{
                    ShareUtils.showShare(news.getTitle(),news.getDesc(),news.getLink(), ServerConfig.DEFAULT_IMG);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setEvent() {
        news = (News) getIntent().getExtras().get("newsDetail");
        url = news.getLink();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        if (url != null && !"".equals(url)) {
            webView.loadUrl(url);
        } else {
            ToastUtils.shortToast(this, "页面地址错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
