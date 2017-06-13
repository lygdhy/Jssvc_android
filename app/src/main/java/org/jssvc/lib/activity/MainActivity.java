package org.jssvc.lib.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.BookSearchFragment;
import org.jssvc.lib.fragment.DiscussFragment;
import org.jssvc.lib.fragment.HomeFragment;
import org.jssvc.lib.fragment.MineFragment;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity {

  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;

  private List<Integer> tabPics;
  private List<String> tabIndicators;
  private List<Fragment> tabFragments;
  private ContentPagerAdapter contentAdapter;

  @Override protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override protected void initView() {

    initContent();

    initTab();
  }

  private void initContent() {
    //将fragment装进列表中
    tabFragments = new ArrayList<>();
    tabFragments.add(new HomeFragment());
    tabFragments.add(new BookSearchFragment());
    tabFragments.add(new DiscussFragment());
    tabFragments.add(new MineFragment());

    //将名称加载tab名字列表
    tabIndicators = new ArrayList<>();
    tabIndicators.add("首页");
    tabIndicators.add("搜书");
    tabIndicators.add("交流");
    tabIndicators.add("我的");

    tabPics = new ArrayList<>();
    tabPics.add(R.drawable.icon_selector_menu_home);
    tabPics.add(R.drawable.icon_selector_menu_search);
    tabPics.add(R.drawable.icon_selector_menu_discuss);
    tabPics.add(R.drawable.icon_selector_menu_mine);

    contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(contentAdapter);
    viewPager.setOffscreenPageLimit(4);
  }

  private void initTab() {
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    tabLayout.setupWithViewPager(viewPager);

    for (int i = 0; i < tabIndicators.size(); i++) {
      TabLayout.Tab itemTab = tabLayout.getTabAt(i);
      if (itemTab != null) {
        itemTab.setCustomView(R.layout.item_menu_tab);
        TextView itemTv = (TextView) itemTab.getCustomView().findViewById(R.id.tv_menu_item);
        itemTv.setText(tabIndicators.get(i));
        ImageView itemPic = (ImageView) itemTab.getCustomView().findViewById(R.id.iv_menu_icon);
        itemPic.setImageResource(tabPics.get(i));
      }
    }
    tabLayout.getTabAt(0).getCustomView().setSelected(true);
  }

  class ContentPagerAdapter extends FragmentPagerAdapter {

    public ContentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return tabFragments.get(position);
    }

    @Override public int getCount() {
      return tabIndicators.size();
    }

    @Override public CharSequence getPageTitle(int position) {
      return tabIndicators.get(position);
    }
  }
}
