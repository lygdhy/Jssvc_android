package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jssvc.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/14.
 */

public class BookSearchHisAdapter extends RecyclerView.Adapter<BookSearchHisAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<String> lists = new ArrayList<>();

  public BookSearchHisAdapter(Context context, List<String> menuList) {
    this.context = context;
    this.lists = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_book_search_his, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    String item = lists.get(position);
    holder.tvName.setText(item);

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return lists.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvName;

    public ViewHolder(View view) {
      super(view);
      tvName = (TextView) view.findViewById(R.id.tvName);
    }
  }

  private OnRecyclerViewItemClickListener mOnItemClickListener = null;

  public static interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, String item);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      //注意这里使用getTag方法获取数据
      mOnItemClickListener.onItemClick(v, (String) v.getTag());
    }
  }
}
