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

public class BookDetailQtyFragment extends BaseFragment {
    @BindView(R.id.inLibRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_book_detail_qty;
    }

    @Override
    protected void initView() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        BookAccessAdapter bookAccessAdapter = new BookAccessAdapter(mRecyclerView);
        mRecyclerView.setAdapter(bookAccessAdapter);
        bookAccessAdapter.setData(accessList);
    }
}
