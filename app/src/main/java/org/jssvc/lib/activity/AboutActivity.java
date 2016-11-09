package org.jssvc.lib.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.TabAFragment;
import org.jssvc.lib.fragment.TabBFragment;
import org.jssvc.lib.fragment.TabCFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private TabAFragment tabAFragment;
    private TabBFragment tabBFragment;
    private TabCFragment tabCFragment;

    private List<String> list_title;
    private List<Fragment> list_fragment;

    private ShowTabAdapter showTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        //初始化各fragment
        tabAFragment = new TabAFragment();
        tabBFragment = new TabBFragment();
        tabCFragment = new TabCFragment();

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(tabAFragment);
        list_fragment.add(tabBFragment);
        list_fragment.add(tabCFragment);

        //将名称加载tab名字列表
        list_title = new ArrayList<>();
        list_title.add("产品详情");
        list_title.add("费用说明");
        list_title.add("预定须知");

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));

        showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(showTabAdapter);

        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
