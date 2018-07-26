package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookDetailsBean;
import org.jssvc.lib.bean.BookSearchBean;

import java.util.ArrayList;
import java.util.List;

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
