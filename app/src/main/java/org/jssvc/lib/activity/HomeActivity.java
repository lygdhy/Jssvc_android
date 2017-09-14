package org.jssvc.lib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.utils.ImageLoader;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主程序
 */
public class HomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
  private static final int REQUEST_CODE_STORAGE = 1;

  @BindView(R.id.convenientBanner) ConvenientBanner convenientBanner;
  @BindView(R.id.btnBookShelf) LinearLayout btnBookShelf;
  @BindView(R.id.btnCurentBorrow) LinearLayout btnCurentBorrow;
  @BindView(R.id.btnHistoryBorrow) LinearLayout btnHistoryBorrow;
  @BindView(R.id.btnBookSearch) LinearLayout btnBookSearch;
  @BindView(R.id.btnHelp) LinearLayout btnHelp;
  @BindView(R.id.btnVideo) LinearLayout btnVideo;
  @BindView(R.id.btnMsg) LinearLayout btnMsg;
  @BindView(R.id.btnSetting) LinearLayout btnSetting;
  @BindView(R.id.tvPoint) TextView tvPoint;

  @Override protected int getContentViewId() {
    return R.layout.activity_home;
  }

  @Override protected void initView() {
    tvPoint.setVisibility(View.GONE);

    showAd();

    // 请求存储权限
    requestStoragePermissions();
  }

  @OnClick({
      R.id.btnBookShelf, R.id.btnCurentBorrow, R.id.btnHistoryBorrow, R.id.btnBookSearch,
      R.id.btnMsg, R.id.btnHelp, R.id.btnVideo, R.id.btnSetting
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btnBookShelf:
        // 我的书架
        if (AccountPref.isLogon(mContext)) {
          startActivity(new Intent(mContext, BookShelfActivity.class));
        } else {
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.btnCurentBorrow:
        // 当前借阅 / 催还续借
        if (AccountPref.isLogon(mContext)) {
          startActivity(new Intent(mContext, CurentBorrowActivity.class));
        } else {
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.btnHistoryBorrow:
        // 借阅历史
        if (AccountPref.isLogon(mContext)) {
          startActivity(new Intent(mContext, HistoryBorrowActivity.class));
        } else {
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.btnBookSearch:
        // 图书搜索
        startActivity(new Intent(mContext, BookSearchActivity.class));
        break;
      case R.id.btnMsg:
        // 新闻通知
        startActivity(new Intent(mContext, MsgActivity.class));
        break;
      case R.id.btnHelp:
        // 帮助指南
        startActivity(new Intent(mContext, HelpActivity.class));
        break;
      case R.id.btnVideo:
        // 图书馆简介
        startActivity(new Intent(mContext, AboutSchoolActivity.class));
        break;
      case R.id.btnSetting:
        // 设置
        startActivity(new Intent(mContext, SettingActivity.class));
        break;
    }
  }

  private void showAd() {
    List<AdsBean> adsList = new ArrayList<>();
    adsList.add(new AdsBean("1", "1", "主题一", "http://www.hydong.me/app/libapp/0001.png",
        "http://lib.jssvc.edu.cn/"));
    adsList.add(new AdsBean("2", "1", "主题二", "http://www.hydong.me/app/libapp/0002.png",
        "http://lib.jssvc.edu.cn/"));

    //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
    convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
      @Override public LocalImageHolderView createHolder() {
        return new LocalImageHolderView();
      }
    }, adsList)
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        .setPageIndicator(
            new int[] { R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused })
        //设置指示器的方向
        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    //设置翻页的效果，不需要翻页效果可用不设
    //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
    // convenientBanner.setManualPageable(false);//设置不能手动影响
  }

  public class LocalImageHolderView implements Holder<AdsBean> {
    private ImageView imageView;

    @Override public View createView(Context context) {
      imageView = new ImageView(context);
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      return imageView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, final AdsBean adsBean) {
      ImageLoader.with(context, imageView, adsBean.getPic());
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          //Intent intent = new Intent(context, WebActivity.class);
          //intent.putExtra("url", adsBean.getUrl());
          //intent.putExtra("title", adsBean.getTitle());
          //startActivity(intent);
        }
      });
    }
  }

  // 开始自动翻页
  @Override public void onResume() {
    super.onResume();
    //开始自动翻页
    convenientBanner.startTurning(5000);
  }

  // 停止自动翻页
  @Override public void onPause() {
    super.onPause();
    //停止翻页
    convenientBanner.stopTurning();
  }

  // 按两次返回键退出====================================
  private long mExitTime;

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if ((System.currentTimeMillis() - mExitTime) > 2000) {
        showToast("再按一次退出程序");
        mExitTime = System.currentTimeMillis();
      } else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  // ================================================
  // =============================权限==========================
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @Override public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  @Override public void onPermissionsDenied(int requestCode, List<String> perms) {

  }

  @AfterPermissionGranted(REQUEST_CODE_STORAGE) private void requestStoragePermissions() {
    String[] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
    if (!EasyPermissions.hasPermissions(this, perms)) {
      EasyPermissions.requestPermissions(this, "为保证应用功能完整，请务必授予存储卡读写权限!", REQUEST_CODE_STORAGE,
          perms);
    }
  }
}
