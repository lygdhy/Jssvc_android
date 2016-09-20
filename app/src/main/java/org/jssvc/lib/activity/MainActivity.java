package org.jssvc.lib.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.SimpleFragmentPagerAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.FindFragment;
import org.jssvc.lib.fragment.HomeFragment;
import org.jssvc.lib.fragment.MineFragment;
import org.jssvc.lib.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * APP主程序
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private int pos_cur = 0;// 默认加载页码
    private List<Fragment> fragments = new ArrayList<>();
    private SimpleFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        String url = "http://api.51toc.net/advertisement_list.php";
//        OkHttpUtils.get().url(url)
//                .addParams("type", "2")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        textView.setText(response);
//                    }
//                });


        tabLayout.setupWithViewPager(viewpager);
        fragments.add(HomeFragment.newInstance());
        fragments.add(NewsFragment.newInstance());
        fragments.add(FindFragment.newInstance());
        fragments.add(MineFragment.newInstance());
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this, fragments);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(fragments.size());

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(this);
            }
        }
        viewpager.setCurrentItem(pos_cur);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (position != pos_cur) {
            pos_cur = position;
        }
    }
}
