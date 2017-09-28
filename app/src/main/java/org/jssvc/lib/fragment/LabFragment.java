package org.jssvc.lib.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.DrawingActivity;
import org.jssvc.lib.adapter.MenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.view.DividerItemDecoration;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 实验室
 *     version: 1.0
 * </pre>
 */
public class LabFragment extends BaseFragment implements BGAOnRVItemClickListener {

  @BindView(R.id.recycler) RecyclerView mRecycler;

  MenuAdapter mAdapter;

  @Override protected int getContentViewId() {
    return R.layout.fragment_lab;
  }

  @Override protected void initView() {
    initRefreshView();

    getDiscussList();
  }

  private void initRefreshView() {
    mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
    mRecycler.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    mAdapter = new MenuAdapter(mRecycler);
    mAdapter.setOnRVItemClickListener(this);
    mRecycler.setAdapter(mAdapter);
  }

  private void getDiscussList() {
    List<MenuBean> menuList = new ArrayList<>();

    menuList.add(new MenuBean(Constants.MENU_DRAW, "你画我猜", R.drawable.icon_menu_c));
    menuList.add(new MenuBean(Constants.MENU_LIB_BOOK_REVIEW, "书评", R.drawable.icon_menu_c));
    for (int i = 0; i < 8; i++) {
      menuList.add(new MenuBean(i, "菜单" + i, R.drawable.icon_menu_a));
    }
    mAdapter.setData(menuList);
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    MenuBean item = mAdapter.getData().get(position);
    switch (item.getType()) {
      case Constants.MENU_DRAW://你画我猜
        startActivity(new Intent(mContext, DrawingActivity.class));
        break;
      case Constants.MENU_LIB_BOOK_REVIEW://书评
        showToast("书评");
        break;
    }
  }
}
