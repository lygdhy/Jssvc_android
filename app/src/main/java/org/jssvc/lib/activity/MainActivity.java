package org.jssvc.lib.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.data.AccountPref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity {

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
    return R.layout.activity_main;
  }

  @Override protected void initView() {
    tvPoint.setVisibility(View.GONE);

    showAd();
  }

  @OnClick({
      R.id.btnBookShelf, R.id.btnCurentBorrow, R.id.btnHistoryBorrow, R.id.btnBookSearch,
      R.id.btnMsg, R.id.btnHelp, R.id.btnVideo, R.id.btnSetting
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btnBookShelf:
        // 我的书架
        if (AccountPref.isLogon(context)) {
          startActivity(new Intent(context, BookShelfActivity.class));
        } else {
          startActivity(new Intent(context, LoginActivity.class));
        }
        break;
      case R.id.btnCurentBorrow:
        // 当前借阅 / 催还续借
        if (AccountPref.isLogon(context)) {
          startActivity(new Intent(context, CurentBorrowActivity.class));
        } else {
          startActivity(new Intent(context, LoginActivity.class));
        }
        break;
      case R.id.btnHistoryBorrow:
        // 借阅历史
        if (AccountPref.isLogon(context)) {
          startActivity(new Intent(context, HistoryBorrowActivity.class));
        } else {
          startActivity(new Intent(context, LoginActivity.class));
        }
        break;
      case R.id.btnBookSearch:
        // 图书搜索
        startActivity(new Intent(context, BookSearchActivity.class));
        break;
      case R.id.btnMsg:
        // 新闻通知
        startActivity(new Intent(context, MsgActivity.class));
        break;
      case R.id.btnHelp:
        // 帮助指南
        startActivity(new Intent(context, HelpActivity.class));
        break;
      case R.id.btnVideo:
        // 图书馆简介
        startActivity(new Intent(context, VideoActivity.class));
        break;
      case R.id.btnSetting:
        // 设置
        startActivity(new Intent(context, SettingActivity.class));
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
    private SimpleDraweeView simpleDraweeView;

    @Override public View createView(Context context) {
      simpleDraweeView = new SimpleDraweeView(context);
      simpleDraweeView.setScaleType(SimpleDraweeView.ScaleType.CENTER_CROP);
      return simpleDraweeView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, final AdsBean adsBean) {
      simpleDraweeView.setImageURI(adsBean.getPic());
      simpleDraweeView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          //                    Intent intent = new Intent(context, WebActivity.class);
          //                    intent.putExtra("url", adsBean.getUrl());
          //                    intent.putExtra("title", adsBean.getTitle());
          //                    startActivity(intent);
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
}
