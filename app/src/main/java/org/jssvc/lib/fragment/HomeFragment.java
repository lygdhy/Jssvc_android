package org.jssvc.lib.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.BookSearchActivity;
import org.jssvc.lib.activity.MainActivity;
import org.jssvc.lib.adapter.ArticleAdapter;
import org.jssvc.lib.adapter.MenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.ImageLoader;
import org.jssvc.lib.utils.PingUtil;
import org.jssvc.lib.view.DividerItemDecoration;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 首页
 *     version: 1.0
 * </pre>
 */
public class HomeFragment extends BaseFragment implements BGAOnRVItemClickListener {
  @BindView(R.id.top_def_layout) RelativeLayout topDefLayout;
  @BindView(R.id.nested_scroll_view) NestedScrollView mNestedScrollView;
  @BindView(R.id.menu_recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.convenientBanner) ConvenientBanner mBanner;

  @BindView(R.id.new_recyclerView) RecyclerView articleRecyclerView;
  ArticleAdapter articleAdapter;
  List<ArticleListBean> articleList = new ArrayList<>();

  MenuAdapter menuAdapter;
  List<MenuBean> menuList = new ArrayList<>();

  @Override protected int getContentViewId() {
    return R.layout.fragment_home;
  }

  @Override protected void initView() {

    initAdapter();

    initMenu();// 加载菜单

    getAdsList();// 获取Banner

    getArticleList();// 获取文章列表

    // 天气API
    // http://op.juhe.cn/onebox/weather/query?cityname=%E8%8B%8F%E5%B7%9E%E5%B8%82&key=220448b1902d02eea42160d3e06f87ff

    // 滑动监听
    mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
          int oldScrollY) {
        int difHight = mBanner.getHeight() - topDefLayout.getHeight();
        if (scrollY == 0) {
          topDefLayout.setBackgroundResource(R.drawable.bg_home_captain);
          topDefLayout.setAlpha(1f);
        } else {
          float alpha = (float) scrollY / difHight;
          topDefLayout.setBackgroundResource(R.color.color_ui_head);
          topDefLayout.setAlpha(alpha);
        }
      }
    });
  }

  private void initAdapter() {
    // 首页菜单
    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
    mRecyclerView.setNestedScrollingEnabled(false);
    menuAdapter = new MenuAdapter(mRecyclerView);
    menuAdapter.setOnRVItemClickListener(this);
    mRecyclerView.setAdapter(menuAdapter);

    // 首页新闻Adapter
    articleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    articleRecyclerView.setNestedScrollingEnabled(false);
    articleRecyclerView.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    articleAdapter = new ArticleAdapter(articleRecyclerView);
    articleAdapter.setOnRVItemClickListener(this);
    articleRecyclerView.setAdapter(articleAdapter);
  }

  // 初始化菜单
  private void initMenu() {
    menuList.clear();
    menuList.add(new MenuBean(Constants.LIB_HELP, "帮助指南", R.drawable.icon_menu_help));
    menuList.add(new MenuBean(Constants.LIB_READ_ING, "当前借阅", R.drawable.icon_menu_borrowed));
    menuList.add(new MenuBean(Constants.MENU_NEWS, "新闻资讯", R.drawable.icon_menu_new));
    menuList.add(new MenuBean(Constants.MENU_ABOUT, "关于我们", R.drawable.icon_menu_about_us));

    menuAdapter.setData(menuList);
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    if (parent.getId() == R.id.new_recyclerView) {
      showToast(articleAdapter.getData().get(position).getTitle());
    }
    if (parent.getId() == R.id.menu_recyclerView) {
      MenuBean item = menuAdapter.getItem(position);
      MainActivity parentActivity = (MainActivity) getActivity();
      parentActivity.openActivityByMenu(item);
    }
  }

  @OnClick({
      R.id.tip_layout, R.id.edt_search, R.id.tv_model_title
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tip_layout:
        showToast("天气预报");
        break;
      case R.id.edt_search:
        startActivity(new Intent(mContext, BookSearchActivity.class));// 图书搜索
        break;
      case R.id.tv_model_title:
        // ping 测试
        int result = PingUtil.getAvgRTT("http://www.baidu.com", 3, 200);
        showToast("PING测试：AvgRTT = " + result);
        break;
    }
  }

  // 获取文件列表
  private void getArticleList() {
    OkGo.<String>get(HttpUrlParams.GET_ARTICLE_LIST).tag(this)
        .params("page", "1")
        .params("pagesize", "10")
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            articleList.clear();
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                List<ArticleListBean> tempList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                  ArticleListBean item = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),
                      ArticleListBean.class);
                  tempList.add(item);
                }
                articleList.addAll(tempList);
                articleAdapter.setData(articleList);
              } else {
                showToast(jsonObject.optString("message"));
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }
        });
  }

  // 获取广告Banner
  private void getAdsList() {
    OkGo.<String>get(HttpUrlParams.GET_ADS_LIST).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(Response<String> response) {
        articleList.clear();
        try {
          JSONObject jsonObject = new JSONObject(response.body());
          if (jsonObject.optInt("code") == 200) {
            List<AdsBean> tempList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
              AdsBean item =
                  new Gson().fromJson(jsonArray.getJSONObject(i).toString(), AdsBean.class);
              tempList.add(item);
            }
            showAd(tempList);
          } else {
            showToast(jsonObject.optString("message"));
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(Response<String> response) {
        super.onError(response);
        dealNetError(response);
      }
    });
  }

  private void showAd(List<AdsBean> adsList) {
    if (adsList == null || adsList.size() == 0) {
      // String theme, String category, String banner
      adsList.add(new AdsBean("默认Banner", "0", "http://www.hydong.me/app/picture/0000.png"));
    }

    //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
    mBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
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
    // mBanner.setManualPageable(false);//设置不能手动影响
  }

  public class LocalImageHolderView implements Holder<AdsBean> {
    private ImageView imageView;

    @Override public View createView(Context context) {
      imageView = new ImageView(context);
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      return imageView;
    }

    @Override public void UpdateUI(final Context context, final int position, final AdsBean model) {
      ImageLoader.with(context, imageView, model.getBanner());
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          showToast(model.getTheme());
        }
      });
    }
  }

  @Override public void onResume() {
    super.onResume();
    mBanner.startTurning(5000);
  }

  @Override public void onPause() {
    super.onPause();
    mBanner.stopTurning();
  }
}
