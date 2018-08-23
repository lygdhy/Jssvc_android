package org.jssvc.lib.ui.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by lygdh on 2016/11/9.
 */

public class ShowTabAdapter extends FragmentPagerAdapter {
  private List<Fragment> tabFragments;
  private List<String> tabIndicators;

  public ShowTabAdapter(FragmentManager fm, List<Fragment> tabFragments,
      List<String> tabIndicators) {
    super(fm);
    this.tabFragments = tabFragments;
    this.tabIndicators = tabIndicators;
  }

  @Override public Fragment getItem(int position) {
    return tabFragments.get(position);
  }

  @Override public int getCount() {
    return tabIndicators.size();
  }

  //此方法用来显示tab上的名字
  @Override public CharSequence getPageTitle(int position) {
    return tabIndicators.get(position % tabIndicators.size());
  }
}
