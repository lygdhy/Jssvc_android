package org.jssvc.lib.adapter;

/**
 * Created by lygdh on 2016/9/20.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.jssvc.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * FragmentPager适配器
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int[] imageResId = {R.drawable.icon_menu_home,
            R.drawable.icon_menu_news,
            R.drawable.icon_menu_find,
            R.drawable.icon_menu_mine};
    private List<Fragment> fragments = new ArrayList<>();
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_tab, null);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(imageResId[position]);
        return view;
    }
}

