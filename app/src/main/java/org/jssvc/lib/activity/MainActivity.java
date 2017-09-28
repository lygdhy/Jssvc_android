package org.jssvc.lib.activity;

import android.Manifest;
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
import org.jssvc.lib.fragment.HomeFragment;
import org.jssvc.lib.fragment.LabFragment;
import org.jssvc.lib.fragment.MineFragment;
import org.jssvc.lib.fragment.ReadingHubFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
  private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

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

    // 请求打开摄像头和SD卡
    requestCodeQrcodePermissions();
  }

  private void initContent() {
    //将fragment装进列表中
    tabFragments = new ArrayList<>();
    tabFragments.add(new HomeFragment());
    tabFragments.add(new ReadingHubFragment());
    tabFragments.add(new LabFragment());//DiscussFragment()
    tabFragments.add(new MineFragment());

    //将名称加载tab名字列表
    tabIndicators = new ArrayList<>();
    tabIndicators.add("首页");
    tabIndicators.add("资讯");
    tabIndicators.add("发现");
    tabIndicators.add("我的");

    tabPics = new ArrayList<>();
    tabPics.add(R.drawable.icon_selector_menu_home);
    tabPics.add(R.drawable.icon_selector_menu_msg);
    tabPics.add(R.drawable.icon_selector_menu_block);
    tabPics.add(R.drawable.icon_selector_menu_mine);

    contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(contentAdapter);
    viewPager.setOffscreenPageLimit(4);
  }

  public void turnPage(int page) {
    viewPager.setCurrentItem(page);
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

  /**
   * 是否支持滑动返回
   */
  protected boolean supportSlideBack() {
    return false;
  }

  // =============================权限 =============================
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
  private void requestCodeQrcodePermissions() {
    String[] perms = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    if (EasyPermissions.hasPermissions(this, perms)) {
      // Already have permission, do the thing
      // ...
    } else {
      // Do not have permissions, request them now
      EasyPermissions.requestPermissions(this, "为保障APP正常工作，请授权APP使用拍照和SD卡读写功能！",
          REQUEST_CODE_QRCODE_PERMISSIONS, perms);
    }
  }

  @Override public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  @Override public void onPermissionsDenied(int requestCode, List<String> perms) {
    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
      new AppSettingsDialog.Builder(this).build().show();
    }
  }
}
