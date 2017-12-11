package org.jssvc.lib.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.MainActivity;
import org.jssvc.lib.adapter.MenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.view.DividerGridItemDecoration;

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
    mRecycler.addItemDecoration(new DividerGridItemDecoration(mContext));
    mAdapter = new MenuAdapter(mRecycler);
    mAdapter.setOnRVItemClickListener(this);
    mRecycler.setAdapter(mAdapter);
  }

  private void getDiscussList() {
    List<MenuBean> menuList = new ArrayList<>();

    menuList.add(new MenuBean(Constants.MENU_FEEDBACK, "意见反馈", R.drawable.icon_menu_feedback));
    menuList.add(new MenuBean(Constants.MENU_WAITER, "客服小智", R.drawable.icon_menu_waiter));
    menuList.add(new MenuBean(Constants.MENU_NEWS, "新闻资讯", R.drawable.icon_menu_new));
    menuList.add(new MenuBean(Constants.MENU_ABOUT, "关于我们", R.drawable.icon_menu_about_us));

    menuList.add(new MenuBean(Constants.LIB_ABOUT, "关于图书馆", R.drawable.icon_menu_about_lib));
    menuList.add(new MenuBean(Constants.LIB_HELP, "帮助指南", R.drawable.icon_menu_help));
    menuList.add(new MenuBean(Constants.LIB_SEARCH_BOOK, "图书搜索", R.drawable.icon_menu_book_search));

    menuList.add(new MenuBean(Constants.LIB_RESUME, "证件信息", R.drawable.icon_menu_idcard));
    menuList.add(new MenuBean(Constants.LIB_READ_ING, "当前借阅", R.drawable.icon_menu_borrowed));
    menuList.add(new MenuBean(Constants.LIB_READ_HIS, "借阅历史", R.drawable.icon_menu_book_his));
    menuList.add(new MenuBean(Constants.LIB_BOOK_REVIEW, "我的书评", R.drawable.icon_menu_appraise));
    menuList.add(new MenuBean(Constants.LIB_BOOK_SHELF, "我的书架", R.drawable.icon_menu_bookrack));

    mAdapter.setData(menuList);
  }

  @Override public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    MenuBean item = mAdapter.getData().get(position);
    MainActivity parentActivity = (MainActivity) getActivity();
    parentActivity.openActivityByMenu(item);
  }
}
