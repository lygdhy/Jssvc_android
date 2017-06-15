package org.jssvc.lib.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.LibPlanFragment;
import org.jssvc.lib.fragment.LibResumeFragment;
import org.jssvc.lib.fragment.LibScheduleFragment;
import org.jssvc.lib.fragment.LibYellowPagesFragment;

/**
 * 关于图书馆
 */
public class AboutSchoolActivity extends BaseActivity {

  @BindView(R.id.tabLayout) TabLayout tabLayout;
  @BindView(R.id.viewPager) ViewPager viewPager;

  private List<String> list_title;
  private List<Fragment> list_fragment;
  private ShowTabAdapter showTabAdapter;

  @Override protected int getContentViewId() {
    return R.layout.activity_about_school;
  }

  @Override protected void initView() {
    initContent();
  }

  @OnClick({ R.id.tv_back }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
    }
  }

  private void initContent() {
    //将fragment装进列表中
    list_fragment = new ArrayList<>();
    list_fragment.add(new LibResumeFragment());
    list_fragment.add(new LibPlanFragment());
    list_fragment.add(new LibScheduleFragment());
    list_fragment.add(new LibYellowPagesFragment());

    //将名称加载tab名字列表
    list_title = new ArrayList<>();
    list_title.add("概况");
    list_title.add("楼层分布");
    list_title.add("开馆时间");
    list_title.add("联系方式");

    //设置TabLayout的模式
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    //为TabLayout添加tab名称
    tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
    tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
    tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));
    tabLayout.addTab(tabLayout.newTab().setText(list_title.get(3)));

    showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
    viewPager.setAdapter(showTabAdapter);

    //TabLayout加载viewpager
    tabLayout.setupWithViewPager(viewPager);
    viewPager.setCurrentItem(0);
  }
}
