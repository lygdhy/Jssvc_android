package org.jssvc.lib.fragment;


import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

/**
 * 新闻
 */
public class NewsFragment extends BaseFragment {

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {

    }

}
