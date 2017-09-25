package org.jssvc.lib.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ArticleAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static org.jssvc.lib.R.id.recyclerView;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ArticleListFragment extends BaseFragment
    implements BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.refresh_layout) BGARefreshLayout mRefreshLayout;

  ArticleAdapter mAdapter;
  List<ArticleListBean> articleList = new ArrayList<>();

  private int mPageNum = 1;// 页码
  private int mPageSize = 20;// 页大小
  private String channel_id = "";// 频道ID

  @Override protected int getContentViewId() {
    return R.layout.fragment_article_list;
  }

  @Override protected void initView() {
    channel_id = getArguments().getString("channel_id");
    initRefreshView();
    getArticleList();
  }

  private void initRefreshView() {
    mRefreshLayout.setDelegate(this);
    mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));

    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    mAdapter = new ArticleAdapter(mRecyclerView);
    mAdapter.setOnRVItemClickListener(this);
    mRecyclerView.setAdapter(mAdapter);
  }

  // 获取文件列表
  private void getArticleList() {
    OkGo.<String>get(HttpUrlParams.GET_ARTICLE_LIST).tag(this)
        .params("channel_id", channel_id)
        .params("page", String.valueOf(mPageNum))
        .params("pagesize", String.valueOf(mPageSize))
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            if (mPageNum == 1) articleList.clear();
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
                mAdapter.setData(articleList);
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

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
          }

          @Override public void onFinish() {
            super.onFinish();
            mRefreshLayout.endLoadingMore();
            mRefreshLayout.endRefreshing();
          }
        });
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    String originalUrl = mAdapter.getData().get(position).getOriginal();
    if (!TextUtils.isEmpty(originalUrl)) {
      Intent intent = new Intent();
      intent.setAction("android.intent.action.VIEW");
      intent.setData(Uri.parse(originalUrl));
      startActivity(intent);
    }
  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    mPageNum = 1;
    getArticleList();
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    mPageNum++;
    getArticleList();
    return true;
  }
}
