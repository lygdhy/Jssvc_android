package org.jssvc.lib.ui.book.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.bean.BookSearchBean;

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
