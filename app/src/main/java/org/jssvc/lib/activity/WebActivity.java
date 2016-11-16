package org.jssvc.lib.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通用web页面
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    String pageTitle;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra("url");
        pageTitle = getIntent().getStringExtra("title");

        if (!TextUtils.isEmpty(pageTitle)) {
            tvTitle.setText(pageTitle + "");
        }
        loadWeb(url);
    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }

    private void loadWeb(String url) {
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
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 设置标题
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (TextUtils.isEmpty(pageTitle) && !TextUtils.isEmpty(title)) {
                    tvTitle.setText(title);
                }
            }
        });
        // 禁止系统的复制粘贴
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
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
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
        webView.addJavascriptInterface(new WebAppInterface(this), "AppInterface");
    }

    public class WebAppInterface {

        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void readOriginalArticle(String url) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
