package org.jssvc.lib.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.DiscussAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.DiscussBean;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 图书交流
 *     version: 1.0
 * </pre>
 */
public class DiscussFragment extends BaseFragment
    implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener {

  @BindView(R.id.refresh_layout) BGARefreshLayout refreshLayout;
  @BindView(R.id.discuss_recycler) RecyclerView recyclerView;

  private int mPage = 0;
  DiscussAdapter discussAdapter;

  @Override protected int getContentViewId() {
    return R.layout.fragment_discuss;
  }

  @Override protected void initView() {
    initRefreshView();

    getDiscussList();
  }

  private void initRefreshView() {
    refreshLayout.setDelegate(this);
    refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(context, true));
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    discussAdapter = new DiscussAdapter(recyclerView);
    discussAdapter.setOnRVItemClickListener(this);
    recyclerView.setAdapter(discussAdapter);
  }

  private void getDiscussList() {
    List<DiscussBean> tempData = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      tempData.add(new DiscussBean());
    }
    if (mPage == 0) {
      discussAdapter.clear();
      discussAdapter.setData(tempData);
    } else {
      discussAdapter.addMoreData(tempData);
    }

    refreshLayout.endLoadingMore();
    refreshLayout.endRefreshing();
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    // 切换详情
    DiscussBean item = discussAdapter.getData().get(position);
  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    mPage = 0;
    getDiscussList();
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    mPage++;
    getDiscussList();
    return true;
  }
}
