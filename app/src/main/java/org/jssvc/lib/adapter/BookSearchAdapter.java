package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookSearchBean;
import org.jssvc.lib.bean.DiscussBean;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by lygdh on 2016/11/14.
 */

public class BookSearchAdapter extends BGARecyclerViewAdapter<BookSearchBean> {

    public BookSearchAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_book_search);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, BookSearchBean model) {
        helper.getTextView(R.id.tv_no).setText(model.getNo());
        helper.getTextView(R.id.tv_title).setText(model.getTitle());
        helper.getTextView(R.id.tv_code).setText("分类号：" + model.getCode() + "");
        helper.getTextView(R.id.tv_author).setText("责任者：" + model.getAuthor() + "");
        helper.getTextView(R.id.tv_publisher).setText("出版社：" + Html.fromHtml(model.getPublisher()));
        helper.getTextView(R.id.tv_type).setText("图书类型：" + model.getType() + "");
        helper.getTextView(R.id.tv_remain).setText(model.getCopy_Remain() + "");
        helper.getTextView(R.id.tv_total).setText(" / " + model.getCopy_Total() + "");
    }
}
