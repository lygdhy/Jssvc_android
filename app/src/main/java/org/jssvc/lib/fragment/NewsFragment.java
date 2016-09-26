package org.jssvc.lib.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.FindTabAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.FindMenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 新闻
 */
public class NewsFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<FindMenuBean> findMenus;
    private List<FindInnerFragment> fragments;

    private int pos_cur = 0;// 默认加载页码
    private FindTabAdapter pagerAdapter;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_news;
    }

    // 初始化各控件
    @Override
    protected void initView() {

        //将fragment装进列表中
        findMenus = new ArrayList<>();
        findMenus.add(new FindMenuBean(1, "推荐"));
        findMenus.add(new FindMenuBean(2, "热门"));
        findMenus.add(new FindMenuBean(3, "哈哈"));

        fragments = new ArrayList<>();
        for (int i = 0; i < findMenus.size(); i++) {
            fragments.add(FindInnerFragment.newInstance(findMenus.get(i).getChannelId()));
        }

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new FindTabAdapter(getChildFragmentManager(), findMenus, fragments);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);//将TabLayout和ViewPager关联起来

        viewpager.setOffscreenPageLimit(findMenus.size());//懒加载

        viewpager.setCurrentItem(pos_cur);
    }

    @Override
    protected void onFirstUserVisible() {
        
    }


}
