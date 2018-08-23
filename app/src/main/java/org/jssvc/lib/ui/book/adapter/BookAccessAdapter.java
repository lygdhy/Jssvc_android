package org.jssvc.lib.ui.book.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.bean.BookAccessBean;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by lygdh on 2016/11/17.
 */

public class BookAccessAdapter extends BGARecyclerViewAdapter<BookAccessBean> {

    public BookAccessAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_book_access);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, BookAccessBean model) {
        helper.getTextView(R.id.tv_tp_code).setText("索书号：" + model.getTpCode());
        helper.getTextView(R.id.tv_bar_code).setText("条码号：" + model.getBarCode());
        helper.getTextView(R.id.tv_place).setText("馆藏地：" + model.getPlace());
        helper.getTextView(R.id.tv_cell).setText("年卷期：" + model.getCell());

        helper.getTextView(R.id.tv_state).setText(model.getState());
        if (model.getState().equals("可借")) {
            helper.getView(R.id.v_state).setBackgroundColor(Color.parseColor("#009551"));
            helper.getTextView(R.id.tv_state).setTextColor(Color.parseColor("#009551"));
        } else {
            helper.getView(R.id.v_state).setBackgroundColor(Color.parseColor("#f66039"));
            helper.getTextView(R.id.tv_state).setTextColor(Color.parseColor("#f66039"));
        }
    }
}

