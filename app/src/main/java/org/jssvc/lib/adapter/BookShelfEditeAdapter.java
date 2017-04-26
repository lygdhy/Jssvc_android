package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookShelfBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/14.
 */

public class BookShelfEditeAdapter extends RecyclerView.Adapter<BookShelfEditeAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<BookShelfBean> menuList = new ArrayList<>();

  public BookShelfEditeAdapter(Context context, List<BookShelfBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_book_shelf_edite, parent, false);
    ViewHolder vh = new ViewHolder(view);

    //将创建的View注册点击事件
    vh.ivEdit.setOnClickListener(this);
    vh.ivDelete.setOnClickListener(this);
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    BookShelfBean item = menuList.get(position);
    holder.tvTitle.setText(item.getName());

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.ivEdit.setTag(item);
    holder.ivDelete.setTag(item);
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    ImageView ivEdit, ivDelete;

    public ViewHolder(View view) {
      super(view);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
      ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
    }
  }

  private IMyViewHolderClicks mOnItemClickListener = null;

  public static interface IMyViewHolderClicks {
    void onItemClick(View view, BookShelfBean item);

    void onEditClick(View view, BookShelfBean item);

    void onDeleteClick(View view, BookShelfBean item);
  }

  public void setOnItemClickListener(IMyViewHolderClicks listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      if (v.getId() == R.id.ivEdit) {
        mOnItemClickListener.onEditClick(v, (BookShelfBean) v.getTag());
      } else if (v.getId() == R.id.ivDelete) {
        mOnItemClickListener.onDeleteClick(v, (BookShelfBean) v.getTag());
      } else {
        //注意这里使用getTag方法获取数据
        mOnItemClickListener.onItemClick(v, (BookShelfBean) v.getTag());
      }
    }
  }
}
