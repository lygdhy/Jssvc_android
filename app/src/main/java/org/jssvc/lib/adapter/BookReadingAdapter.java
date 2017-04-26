package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookReadingBean;

/**
 * Created by lygdh on 2016/11/14.
 */

public class BookReadingAdapter extends RecyclerView.Adapter<BookReadingAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<BookReadingBean> menuList = new ArrayList<>();

  public BookReadingAdapter(Context context, List<BookReadingBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_reading, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    vh.tvXu.setOnClickListener(this);
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    BookReadingBean item = menuList.get(position);
    holder.tvCode.setText("条码号：" + item.getBarCode() + "");
    holder.tvTitle.setText(item.getBookName());
    holder.tvDate.setText(item.getBorrowDate() + " ~ " + item.getReturnDate());
    holder.tvPlace.setText("馆藏地：" + item.getPlace() + "");

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
    holder.tvXu.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvCode, tvTitle, tvDate, tvPlace, tvXu;

    public ViewHolder(View view) {
      super(view);
      tvCode = (TextView) view.findViewById(R.id.tvCode);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      tvDate = (TextView) view.findViewById(R.id.tvDate);
      tvPlace = (TextView) view.findViewById(R.id.tvPlace);
      tvXu = (TextView) view.findViewById(R.id.tvXu);
    }
  }

  private IMyViewHolderClicks mOnItemClickListener = null;

  public static interface IMyViewHolderClicks {
    void onItemClick(View view, BookReadingBean item);

    void onXujieClick(View view, BookReadingBean item);
  }

  public void setOnItemClickListener(IMyViewHolderClicks listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      if (v.getId() == R.id.tvXu) {
        mOnItemClickListener.onXujieClick(v, (BookReadingBean) v.getTag());
      } else {
        //注意这里使用getTag方法获取数据
        mOnItemClickListener.onItemClick(v, (BookReadingBean) v.getTag());
      }
    }
  }
}
