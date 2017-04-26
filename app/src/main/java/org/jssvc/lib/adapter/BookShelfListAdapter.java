package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookShelfListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 书架图书列表
 */

public class BookShelfListAdapter extends RecyclerView.Adapter<BookShelfListAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<BookShelfListBean> menuList = new ArrayList<>();

  public BookShelfListAdapter(Context context, List<BookShelfListBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_shelf, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    vh.tvRemove.setOnClickListener(this);
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    BookShelfListBean item = menuList.get(position);
    holder.tvTitle.setText(item.getName());
    holder.tvAuthor.setText("责任者：" + item.getAuthor());
    holder.tvCode.setText("索书号：" + item.getCode());
    holder.tvPublisher.setText("出版社：" + item.getPublisher());

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.tvRemove.setTag(item);
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvRemove;
    TextView tvCode, tvTitle, tvAuthor, tvPublisher;

    public ViewHolder(View view) {
      super(view);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
      tvCode = (TextView) view.findViewById(R.id.tvCode);
      tvPublisher = (TextView) view.findViewById(R.id.tvPublisher);
      tvRemove = (TextView) view.findViewById(R.id.tvRemove);
    }
  }

  private IMyViewHolderClicks mOnItemClickListener = null;

  public static interface IMyViewHolderClicks {
    void onItemClick(View view, BookShelfListBean item);

    void onRemoveClick(View view, BookShelfListBean item);
  }

  public void setOnItemClickListener(IMyViewHolderClicks listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      if (v.getId() == R.id.tvRemove) {
        mOnItemClickListener.onRemoveClick(v, (BookShelfListBean) v.getTag());
      } else {
        //注意这里使用getTag方法获取数据
        mOnItemClickListener.onItemClick(v, (BookShelfListBean) v.getTag());
      }
    }
  }
}
