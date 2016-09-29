package org.jssvc.lib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.jssvc.lib.bean.FindMenuBean;
import org.jssvc.lib.fragment.NewsInnerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * FragmentPager适配器
 */

public class NewsTabAdapter extends FragmentPagerAdapter {

    List<FindMenuBean> findMenus = new ArrayList<>();
    List<NewsInnerFragment> fragments = new ArrayList<>();

    public NewsTabAdapter(FragmentManager fm, List<FindMenuBean> findMenus, List<NewsInnerFragment> fragments) {
        super(fm);
        this.findMenus = findMenus;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.findMenus.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return findMenus.get(position).getChannelName();
    }
}

