package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookDetailsAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.BookDetailsBean;
import org.jssvc.lib.view.DividerItemDecoration;

/**
 * 加载详情
 * Created by lygdh on 2016/11/21.
 */

public class BookDetailInfoFragment extends BaseFragment {
  @BindView(R.id.detailsRecyclerView) RecyclerView detailsRecyclerView;

  BookDetailsAdapter bookDetailsAdapter;

  @Override protected int getContentViewId() {
    return R.layout.fragment_book_detail_info;
  }

  @Override protected void initView() {
    Bundle bundle = getArguments();//从activity传过来的Bundle
    if (bundle != null) {
      List<BookDetailsBean> detailList = new ArrayList<>();
      detailList.addAll(
          (Collection<? extends BookDetailsBean>) bundle.getSerializable("detailList"));
      loadDetailsList(detailList);
    }
  }

  // 加载详情
  private void loadDetailsList(List<BookDetailsBean> detailsList) {
    //创建默认的线性LayoutManager
    detailsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    detailsRecyclerView.setHasFixedSize(true);
    //添加分割线divider
    detailsRecyclerView.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    //解决滑动冲突
    detailsRecyclerView.setNestedScrollingEnabled(false);
    //创建并设置Adapter
    bookDetailsAdapter = new BookDetailsAdapter(mContext, detailsList);
    detailsRecyclerView.setAdapter(bookDetailsAdapter);

    bookDetailsAdapter.setOnItemClickListener(
        new BookDetailsAdapter.OnRecyclerViewItemClickListener() {
          @Override public void onItemClick(View view, BookDetailsBean item) {
          }
        });
  }
}
