package org.jssvc.lib.fragment;


import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.HttpUrlParams;

import butterknife.BindView;

/**
 * 探索&发现
 */
public class ExpandFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    public static ExpandFragment newInstance() {
        return new ExpandFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_expand;
    }

    @Override
    protected void initView() {

        String url = HttpUrlParams.URL_EXPAND;

        loadWeb(url);

    }

    // 加载页面
    private void loadWeb(String url) {

        webView.loadUrl(url);
        //添加javaScript支持
        webView.getSettings().setJavaScriptEnabled(true);
        //取消滚动条
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //触摸焦点起作用
        webView.requestFocus();
        // 防黑屏
        webView.setBackgroundColor(Color.parseColor("#00000000"));
        webView.setBackgroundResource(R.color.white);

        // 内嵌打开
        webView.setWebViewClient(new WebViewClient() {
            // 截取网页名称
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!TextUtils.isEmpty(view.getTitle())) {
                    tvTitle.setText(view.getTitle() + "");
                } else {
                    tvTitle.setText(getString(R.string.module_exp));
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 支持JsAlert
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        // 禁止系统的复制粘贴
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        // 刷新页面
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                webView.loadUrl(webView.getUrl());
            }
        });
        swipeContainer.setColorSchemeColors(ContextCompat.getColor(context, R.color.google_green),
                ContextCompat.getColor(context, R.color.google_red),
                ContextCompat.getColor(context, R.color.google_yellow),
                ContextCompat.getColor(context, R.color.google_blue));

        //设置进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //隐藏进度条
                    swipeContainer.setRefreshing(false);
                } else {
                    if (!swipeContainer.isRefreshing())
                        swipeContainer.setRefreshing(true);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onFirstUserVisible() {

    }
}
