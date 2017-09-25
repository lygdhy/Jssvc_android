package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.MsgBean;
import org.jssvc.lib.utils.ImageLoader;

/**
 * Created by lygdh on 2016/11/14.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<MsgBean> menuList = new ArrayList<>();

  public MsgAdapter(Context context, List<MsgBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    MsgBean item = menuList.get(position);
    ImageLoader.with(context, holder.msgCover, item.getAvatar() + "");

    holder.tvTitle.setText(item.getTitle() + "");
    holder.tvAuthor.setText(item.getAuthor() + "");
    holder.tvDate.setText(item.getDate() + "");

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvAuthor, tvDate;
    ImageView msgCover;

    public ViewHolder(View view) {
      super(view);
      msgCover = (ImageView) view.findViewById(R.id.msgCover);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
      tvDate = (TextView) view.findViewById(R.id.tvDate);
    }
  }

  private OnRecyclerViewItemClickListener mOnItemClickListener = null;

  public static interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, MsgBean item);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      //注意这里使用getTag方法获取数据
      mOnItemClickListener.onItemClick(v, (MsgBean) v.getTag());
    }
  }
}
