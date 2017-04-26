package org.jssvc.lib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookAccessBean;

/**
 * Created by lygdh on 2016/11/17.
 */

public class BookAccessAdapter extends RecyclerView.Adapter<BookAccessAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<BookAccessBean> menuList = new ArrayList<>();

  public BookAccessAdapter(Context context, List<BookAccessBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_access, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    BookAccessBean item = menuList.get(position);

    if (item.getState().equals("可借")) {
      holder.vState.setBackgroundColor(Color.parseColor("#009551"));
      holder.tvState.setTextColor(Color.parseColor("#009551"));
    } else {
      holder.vState.setBackgroundColor(Color.parseColor("#f66039"));
      holder.tvState.setTextColor(Color.parseColor("#f66039"));
    }

    holder.tvState.setText(item.getState());
    holder.tvTpCode.setText("索书号：" + item.getTpCode());
    holder.tvBarCode.setText("条码号：" + item.getBarCode());
    holder.tvPlace.setText("馆藏地：" + item.getPlace());
    holder.tvCell.setText("年卷期：" + item.getCell());

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTpCode, tvBarCode, tvPlace, tvCell;
    TextView tvState;
    View vState;

    public ViewHolder(View view) {
      super(view);
      tvTpCode = (TextView) view.findViewById(R.id.tvTpCode);
      tvBarCode = (TextView) view.findViewById(R.id.tvBarCode);
      tvPlace = (TextView) view.findViewById(R.id.tvPlace);
      tvCell = (TextView) view.findViewById(R.id.tvCell);
      tvState = (TextView) view.findViewById(R.id.tvState);
      vState = (View) view.findViewById(R.id.vState);
    }
  }

  private BookAccessAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

  public static interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, BookAccessBean item);
  }

  public void setOnItemClickListener(BookAccessAdapter.OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      //注意这里使用getTag方法获取数据
      mOnItemClickListener.onItemClick(v, (BookAccessBean) v.getTag());
    }
  }
}

