package org.jssvc.lib.ui.about.fragment;

import android.webkit.WebView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

import butterknife.BindView;

/**
 * 图书馆黄页
 */
public class LibYellowPagesFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lib_yellow_pages;
    }

    @Override
    protected void initView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/lib_yellow_pages.html");
    }
}
