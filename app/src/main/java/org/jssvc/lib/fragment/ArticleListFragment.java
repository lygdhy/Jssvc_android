package org.jssvc.lib.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ArticleAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.data.HttpUrlParams;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ArticleListFragment extends BaseFragment implements BGAOnRVItemClickListener {

  @BindView(R.id.rl_empty) RelativeLayout rlEmpty;
  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.refresh_layout) BGARefreshLayout refreshLayout;

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

    getArticleList();
  }

  // 获取文件列表
  private void getArticleList() {
    OkGo.<String>post(HttpUrlParams.GET_ARTICLE_LIST).tag(this)
        .params("channel_id", channel_id)
        .params("page", String.valueOf(mPageNum))
        .params("pagesize", String.valueOf(mPageSize))
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                  ArticleListBean item = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),
                      ArticleListBean.class);
                  articleList.add(item);
                }
                loadData2UI();
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
            showProgressDialog();
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  private void loadData2UI() {
    if (articleList == null || articleList.size() == 0) {
      rlEmpty.setVisibility(View.VISIBLE);
      refreshLayout.setVisibility(View.GONE);
    } else {
      rlEmpty.setVisibility(View.GONE);
      refreshLayout.setVisibility(View.VISIBLE);

      mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
      mAdapter = new ArticleAdapter(mRecyclerView);
      mAdapter.setOnRVItemClickListener(this);
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.setData(articleList);
    }
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    showToast(mAdapter.getData().get(position).getTitle());
    //Intent intent = new Intent(mContext, MsgBrowseActivity.class);
    //intent.putExtra("item", (Serializable) item);
    //startActivity(intent);
  }
}
