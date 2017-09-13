package org.jssvc.lib.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AboutActivity;
import org.jssvc.lib.activity.CurentBorrowActivity;
import org.jssvc.lib.activity.HelpActivity;
import org.jssvc.lib.activity.LoginActivity;
import org.jssvc.lib.activity.MainActivity;
import org.jssvc.lib.adapter.MenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.utils.ImageLoader;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 首页
 *     version: 1.0
 * </pre>
 */
public class HomeFragment extends BaseFragment implements BGAOnRVItemClickListener {
  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.convenientBanner) ConvenientBanner convenientBanner;

  MenuAdapter mAdapter;
  List<MenuBean> menuList = new ArrayList<>();

  @Override protected int getContentViewId() {
    return R.layout.fragment_home;
  }

  @Override protected void initView() {
    showAd();

    // 加载菜单
    menuList.add(new MenuBean(1, "帮助指南", R.drawable.icon_menu_a));
    menuList.add(new MenuBean(2, "催还续借", R.drawable.icon_menu_b));
    menuList.add(new MenuBean(3, "图书搜索", R.drawable.icon_menu_c));
    menuList.add(new MenuBean(4, "关于我们", R.drawable.icon_menu_d));

    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
    mAdapter = new MenuAdapter(mRecyclerView);
    mAdapter.setOnRVItemClickListener(this);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setData(menuList);
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    MenuBean item = mAdapter.getItem(position);
    // Type 0链接类  !0 其他类别
    switch (item.getType()) {
      case 1:
        // 帮助指南
        startActivity(new Intent(context, HelpActivity.class));
        break;
      case 2:
        // 当前借阅 / 催还续借
        if (AccountPref.isLogon(context)) {
          startActivity(new Intent(context, CurentBorrowActivity.class));
        } else {
          startActivity(new Intent(context, LoginActivity.class));
        }
        break;
      case 3:
        // 图书搜索
        MainActivity parentActivity = (MainActivity) getActivity();
        parentActivity.turnPage(1);
        break;
      case 4:
        // 关于
        startActivity(new Intent(context, AboutActivity.class));
        break;
    }
  }

  //@OnClick({
  //    R.id.ll_help, R.id.ll_renew, R.id.ll_search
  //}) public void onClick(View view) {
  //  switch (view.getId()) {
  //    case R.id.ll_help:
  //      // 帮助指南
  //      startActivity(new Intent(context, HelpActivity.class));
  //      break;
  //    case R.id.ll_renew:
  //      // 当前借阅 / 催还续借
  //      if (AccountPref.isLogon(context)) {
  //        startActivity(new Intent(context, CurentBorrowActivity.class));
  //      } else {
  //        startActivity(new Intent(context, LoginActivity.class));
  //      }
  //      break;
  //    case R.id.ll_search:
  //      // 图书搜索
  //      MainActivity parentActivity = (MainActivity) getActivity();
  //      parentActivity.turnPage(1);
  //      break;
  //  }
  //}

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
}
