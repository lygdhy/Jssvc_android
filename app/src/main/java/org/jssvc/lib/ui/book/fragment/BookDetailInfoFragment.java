package org.jssvc.lib.ui.book.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.adapter.BookDetailsAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.ui.book.bean.BookDetailsBean;
import org.jssvc.lib.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

/**
 * 加载详情
 * Created by lygdh on 2016/11/21.
 */

public class BookDetailInfoFragment extends BaseFragment {
    @BindView(R.id.detailsRecyclerView)
    RecyclerView mRecyclerView;

    BookDetailsAdapter bookDetailsAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_book_detail_info;
    }

    @Override
    protected void initView() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setNestedScrollingEnabled(false);
        //创建并设置Adapter
        bookDetailsAdapter = new BookDetailsAdapter(mRecyclerView);
        mRecyclerView.setAdapter(bookDetailsAdapter);
        bookDetailsAdapter.setData(detailsList);
    }
}
