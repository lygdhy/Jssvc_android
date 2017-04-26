package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.ListSelecterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/14.
 */

public class DialogListSelecterAdapter
    extends RecyclerView.Adapter<DialogListSelecterAdapter.ViewHolder>
    implements View.OnClickListener {
  Context context;
  public List<ListSelecterBean> menuList = new ArrayList<>();

  public DialogListSelecterAdapter(Context context, List<ListSelecterBean> menuList) {
    this.context = context;
    this.menuList = menuList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.dialog_item_select_layout, parent, false);
    ViewHolder vh = new ViewHolder(view);
    //将创建的View注册点击事件
    view.setOnClickListener(this);
    return vh;
  }

  //将数据与界面进行绑定的操作
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    ListSelecterBean item = menuList.get(position);

    if (item.getIcon() == 0) {
      holder.imageView.setVisibility(View.GONE);
    } else {
      holder.imageView.setVisibility(View.VISIBLE);
      holder.imageView.setImageResource(item.getIcon());
    }

    holder.tvTitle.setText(item.getTitle());

    if (TextUtils.isEmpty(item.getSubtitle())) {
      holder.tvSubTitle.setVisibility(View.GONE);
    } else {
      holder.tvSubTitle.setVisibility(View.VISIBLE);
      holder.tvSubTitle.setText(item.getSubtitle());
    }

    //将数据保存在itemView的Tag中，以便点击时进行获取
    holder.itemView.setTag(item);
  }

  @Override public int getItemCount() {
    return menuList.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView tvTitle, tvSubTitle;

    public ViewHolder(View view) {
      super(view);
      imageView = (ImageView) view.findViewById(R.id.imageView);
      tvTitle = (TextView) view.findViewById(R.id.tvTitle);
      tvSubTitle = (TextView) view.findViewById(R.id.tvSubTitle);
    }
  }

  private IMyViewHolderClicks mOnItemClickListener = null;

  public static interface IMyViewHolderClicks {
    void onItemClick(View view, ListSelecterBean item);
  }

  public void setOnItemClickListener(IMyViewHolderClicks listener) {
    this.mOnItemClickListener = listener;
  }

  @Override public void onClick(View v) {
    if (mOnItemClickListener != null) {
      //注意这里使用getTag方法获取数据
      mOnItemClickListener.onItemClick(v, (ListSelecterBean) v.getTag());
    }
  }
}
