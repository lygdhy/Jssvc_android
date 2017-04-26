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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/14.
 */

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<BookSearchBean> menuList = new ArrayList<>();

  public BookSearchAdapter(Context context, List<BookSearchBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_search, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    BookSearchBean item = menuList.get(position);
    holder.tvNo.setText(item.getNo());
    holder.tvTitle.setText(item.getTitle());
    holder.tvCode.setText("分类号：" + item.getCode() + "");
    holder.tvAuthor.setText("责任者：" + item.getAuthor() + "");
    holder.tvPublisher.setText("出版社：" + Html.fromHtml(item.getPublisher()));
    holder.tvType.setText("图书类型：" + item.getType() + "");

    holder.tvRemain.setText(item.getCopy_Remain() + "");
    holder.tvTotal.setText(" / " + item.getCopy_Total() + "");

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvCode, tvNo, tvTitle, tvAuthor, tvPublisher, tvType;
    TextView tvRemain, tvTotal;

    public ViewHolder(View view) {
      super(view);
      tvNo = (TextView) view.findViewById(R.id.tvNo);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      tvCode = (TextView) view.findViewById(R.id.tvCode);
      tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
      tvPublisher = (TextView) view.findViewById(R.id.tvPublisher);
      tvType = (TextView) view.findViewById(R.id.tvType);
      tvRemain = (TextView) view.findViewById(R.id.tvRemain);
      tvTotal = (TextView) view.findViewById(R.id.tvTotal);
    }
  }

  private OnRecyclerViewItemClickListener mOnItemClickListener = null;

  public static interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, BookSearchBean item);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      //注意这里使用getTag方法获取数据
      mOnItemClickListener.onItemClick(v, (BookSearchBean) v.getTag());
    }
  }
}
