package org.jssvc.lib.ui.home.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.home.MainActivity;
import org.jssvc.lib.ui.home.bean.MenuBean;
import org.jssvc.lib.ui.home.bean.MenuHubBean;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/13
 *     desc   : 菜单适配器
 *     version: 1.0
 * </pre>
 */
public class MenuHubAdapter extends BGARecyclerViewAdapter<MenuHubBean> {
    public MenuHubAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_home_menu_hub);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MenuHubBean model) {
        helper.getTextView(R.id.title).setText(model.getTitle() + "");

        RecyclerView mRecycler = (RecyclerView) helper.getView(R.id.recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        //mRecycler.addItemDecoration(new DividerGridItemDecoration(mContext));
        mRecycler.setFocusable(false);

        final MenuAdapter mAdapter = new MenuAdapter(mRecycler);
        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                MenuBean item = mAdapter.getData().get(position);
                MainActivity parentActivity = (MainActivity) mContext;
                parentActivity.openActivityByMenu(item);
            }
        });

        mRecycler.setAdapter(mAdapter);
        mAdapter.setData(model.getMenulist());
    }
}
