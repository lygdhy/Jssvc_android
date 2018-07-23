package org.jssvc.lib.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    @BindView(R.id.tv_close)
    TextView tvColse;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tbsContent)
    WebView tbsContent;

    private String urlStr = "";
    private String titleStr = "";

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);// 防止网页中的视频闪烁
        tvColse.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            titleStr = bundle.getString("title");
            urlStr = bundle.getString("url");
        }
        tvTitle.setText(TextUtils.isEmpty(titleStr) ? "加载中..." : titleStr);
        tbsContent.loadUrl(urlStr);

        WebSettings webSettings = tbsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        tbsContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

                tvTitle.setText(webView.getTitle());

                if (tbsContent.canGoBack()) {
                    tvColse.setVisibility(View.VISIBLE);
                } else {
                    tvColse.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (tbsContent.canGoBack()) {
                    tbsContent.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.tv_close:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && tbsContent.canGoBack()) {
            tbsContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
