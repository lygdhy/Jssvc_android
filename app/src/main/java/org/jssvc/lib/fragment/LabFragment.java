package org.jssvc.lib.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

import java.util.ArrayList;
import java.util.List;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.MenuHubAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.bean.MenuHubBean;
import org.jssvc.lib.data.Constants;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 实验室
 *     version: 1.0
 * </pre>
 */
public class LabFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    MenuHubAdapter mAdapter;

    public static LabFragment newInstance() {
        return new LabFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lab;
    }

    @Override
    protected void initView() {

        initRefreshView();

        getDiscussList();
    }

    private void initRefreshView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MenuHubAdapter(mRecycler);
        mRecycler.setAdapter(mAdapter);
    }

    private void getDiscussList() {
        List<MenuHubBean> hubList = new ArrayList<>();

        MenuHubBean hub1 = new MenuHubBean();
        hub1.setTitle("我的实验室");
        List<MenuBean> mlist1 = new ArrayList<>();
        mlist1.add(new MenuBean(Constants.MENU_NEWS, "新闻资讯", R.drawable.icon_menu_new));
        hub1.setMenulist(mlist1);
        hubList.add(hub1);

        MenuHubBean hub2 = new MenuHubBean();
        hub2.setTitle("我的图书馆");
        List<MenuBean> mlist2 = new ArrayList<>();
        mlist2.add(new MenuBean(Constants.LIB_RESUME, "我的证件", R.drawable.icon_menu_idcard));
        mlist2.add(new MenuBean(Constants.LIB_READ_HIS, "借阅历史", R.drawable.icon_menu_book_his));
        mlist2.add(new MenuBean(Constants.LIB_READ_ING, "图书续借", R.drawable.icon_menu_borrowed));
        mlist2.add(new MenuBean(Constants.LIB_SEARCH_BOOK, "图书搜索", R.drawable.icon_menu_book_search));
        mlist2.add(new MenuBean(Constants.LIB_BOOK_SHELF, "我的书架", R.drawable.icon_menu_bookrack));
        mlist2.add(new MenuBean(Constants.LIB_HELP, "帮助指南", R.drawable.icon_menu_help));
        hub2.setMenulist(mlist2);
        hubList.add(hub2);

        MenuHubBean hub3 = new MenuHubBean();
        hub3.setTitle("关于未来");
        List<MenuBean> mlist3 = new ArrayList<>();
        mlist3.add(new MenuBean(Constants.LIB_BOOK_REVIEW, "我的书评", R.drawable.icon_menu_appraise));
        mlist3.add(new MenuBean(Constants.LIB_BOOK_RECOMMEND, "图书荐购", R.drawable.icon_menu_book_recommend));
        hub3.setMenulist(mlist3);
        hubList.add(hub3);

        MenuHubBean hub4 = new MenuHubBean();
        hub4.setTitle("联系我们");
        List<MenuBean> mlist4 = new ArrayList<>();
        mlist4.add(new MenuBean(Constants.LIB_ABOUT, "关于Lib", R.drawable.icon_menu_about_lib));
        mlist4.add(new MenuBean(Constants.MENU_ABOUT, "关于我们", R.drawable.icon_menu_about_us));
        mlist4.add(new MenuBean(Constants.MENU_FEEDBACK, "意见反馈", R.drawable.icon_menu_feedback));
        mlist4.add(new MenuBean(Constants.MENU_WAITER, "客服小智", R.drawable.icon_menu_waiter));
        hub4.setMenulist(mlist4);
        hubList.add(hub4);

        mAdapter.setData(hubList);
    }
}
