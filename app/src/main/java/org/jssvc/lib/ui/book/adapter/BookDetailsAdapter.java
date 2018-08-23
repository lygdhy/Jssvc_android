package org.jssvc.lib.ui.book.adapter;

import android.support.v7.widget.RecyclerView;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.bean.BookDetailsBean;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by lygdh on 2016/11/17.
 */

public class BookDetailsAdapter extends BGARecyclerViewAdapter<BookDetailsBean> {

    public BookDetailsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_book_details);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, BookDetailsBean model) {
        helper.getTextView(R.id.tv_key).setText(model.getKey());
        helper.getTextView(R.id.tv_value).setText(model.getValue());
    }
}
