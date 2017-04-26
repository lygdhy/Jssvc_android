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
import org.jssvc.lib.adapter.BookAccessAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.BookAccessBean;

/**
 * 加载详情
 * Created by lygdh on 2016/11/21.
 */

public class BookDetailInlibFragment extends BaseFragment {
  @BindView(R.id.inLibRecyclerView) RecyclerView inLibRecyclerView;

  BookAccessAdapter bookAccessAdapter;

  @Override protected int getContentViewId() {
    return R.layout.pager_book_detail_inlib;
  }

  @Override protected void initView() {
    Bundle bundle = getArguments();//从activity传过来的Bundle
    if (bundle != null) {
      List<BookAccessBean> accessList = new ArrayList<>();
      accessList.addAll(
          (Collection<? extends BookAccessBean>) bundle.getSerializable("accessList"));
      loadAccessList(accessList);
    }
  }

  // 加载藏书列表
  private void loadAccessList(List<BookAccessBean> accessList) {
    //创建默认的线性LayoutManager
    inLibRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    inLibRecyclerView.setHasFixedSize(true);
    //解决滑动冲突
    inLibRecyclerView.setNestedScrollingEnabled(false);
    //创建并设置Adapter
    bookAccessAdapter = new BookAccessAdapter(context, accessList);
    inLibRecyclerView.setAdapter(bookAccessAdapter);

    bookAccessAdapter.setOnItemClickListener(
        new BookAccessAdapter.OnRecyclerViewItemClickListener() {
          @Override public void onItemClick(View view, BookAccessBean item) {
          }
        });
  }
}
