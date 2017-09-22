package org.jssvc.lib.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgaindicator.BGAFixedIndicator;
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

  @BindView(R.id.indicator) BGAFixedIndicator mIndicator;
  @BindView(R.id.viewPager) ViewPager mViewPager;

  private List<String> mTitles;
  private List<Fragment> mFragments;
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
    mFragments = new ArrayList<>();
    mFragments.add(new LibResumeFragment());
    mFragments.add(new LibPlanFragment());
    mFragments.add(new LibScheduleFragment());
    mFragments.add(new LibYellowPagesFragment());

    //将名称加载tab名字列表
    mTitles = new ArrayList<>();
    mTitles.add("概况");
    mTitles.add("楼层分布");
    mTitles.add("开馆时间");
    mTitles.add("联系方式");

    showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
    mViewPager.setAdapter(showTabAdapter);
    mIndicator.initData(0, mViewPager);
  }
}
