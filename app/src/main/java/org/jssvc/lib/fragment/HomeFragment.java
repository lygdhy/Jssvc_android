package org.jssvc.lib.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.jssvc.lib.activity.AboutActivity;
import org.jssvc.lib.activity.BookSearchActivity;
import org.jssvc.lib.activity.CurentBorrowActivity;
import org.jssvc.lib.activity.HelpActivity;
import org.jssvc.lib.activity.LoginActivity;
import org.jssvc.lib.activity.MainActivity;
import org.jssvc.lib.adapter.ArticleAdapter;
import org.jssvc.lib.adapter.MenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.ImageLoader;
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
  @BindView(R.id.menu_recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.convenientBanner) ConvenientBanner convenientBanner;

  @BindView(R.id.new_recyclerView) RecyclerView articleRecyclerView;
  ArticleAdapter articleAdapter;
  List<ArticleListBean> articleList = new ArrayList<>();

  MenuAdapter menuAdapter;
  List<MenuBean> menuList = new ArrayList<>();

  @Override protected int getContentViewId() {
    return R.layout.fragment_home;
  }

  @Override protected void initView() {
    // 加载菜单
    menuList.clear();
    menuList.add(new MenuBean(1, "帮助指南", R.drawable.icon_menu_a));
    menuList.add(new MenuBean(2, "催还续借", R.drawable.icon_menu_b));
    menuList.add(new MenuBean(3, "新闻资讯", R.drawable.icon_menu_c));
    menuList.add(new MenuBean(4, "关于我们", R.drawable.icon_menu_d));

    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
    mRecyclerView.setNestedScrollingEnabled(false);
    menuAdapter = new MenuAdapter(mRecyclerView);
    menuAdapter.setOnRVItemClickListener(this);
    mRecyclerView.setAdapter(menuAdapter);
    menuAdapter.setData(menuList);

    articleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    articleRecyclerView.setNestedScrollingEnabled(false);
    articleRecyclerView.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    articleAdapter = new ArticleAdapter(articleRecyclerView);
    articleAdapter.setOnRVItemClickListener(this);
    articleRecyclerView.setAdapter(articleAdapter);

    getAdsList();// 获取Banner

    getArticleList();// 获取文章列表
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    if (parent.getId() == R.id.new_recyclerView) {
      showToast(articleAdapter.getData().get(position).getTitle());
    }
    if (parent.getId() == R.id.menu_recyclerView) {
      MenuBean item = menuAdapter.getItem(position);
      // Type 0链接类  !0 其他类别
      switch (item.getType()) {
        case 1:
          // 帮助指南
          startActivity(new Intent(mContext, HelpActivity.class));
          break;
        case 2:
          // 当前借阅 / 催还续借
          if (AccountPref.isLogon(mContext)) {
            startActivity(new Intent(mContext, CurentBorrowActivity.class));
          } else {
            startActivity(new Intent(mContext, LoginActivity.class));
          }
          break;
        case 3:
          // 图书搜索
          MainActivity parentActivity = (MainActivity) getActivity();
          parentActivity.turnPage(1);
          break;
        case 4:
          // 关于
          startActivity(new Intent(mContext, AboutActivity.class));
          break;
      }
    }
  }

  @OnClick({
      R.id.tip_layout, R.id.tv_search_bar
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tip_layout:
        break;
      case R.id.tv_search_bar:
        // 图书搜索
        startActivity(new Intent(mContext, BookSearchActivity.class));
        break;
    }
  }

  // 获取文件列表
  private void getArticleList() {
    OkGo.<String>get(HttpUrlParams.GET_ARTICLE_LIST).tag(this)
        .params("page", "1")
        .params("pagesize", "3")
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
    convenientBanner.startTurning(5000);
  }

  @Override public void onPause() {
    super.onPause();
    convenientBanner.stopTurning();
  }
}
