package org.jssvc.lib.fragment;


import android.webkit.WebView;

import butterknife.BindView;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

/**
 * 图书馆开放时间
 */
public class LibScheduleFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lib_schedule;
    }

    @Override
    protected void initView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/lib_schedule.html");
    }
}
