package org.jssvc.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.QuestionBean;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * QA 右侧列表
 */

public class QrightAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private List<QuestionBean> dishes;
  private LayoutInflater mInflater;

  public QrightAdapter(Context context, List<QuestionBean> dishes) {
    mInflater = LayoutInflater.from(context);
    this.dishes = dishes;
  }

  @Override public int getCount() {
    return dishes.size();
  }

  @Override public Object getItem(int position) {
    return dishes.get(position);
  }

  @Override public long getItemId(int i) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = mInflater.inflate(R.layout.item_question_right, parent, false);
      holder.text = (TextView) convertView.findViewById(R.id.text);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.text.setText(dishes.get(position).getTitle());

    return convertView;
  }

  static class ViewHolder {
    TextView text;
  }

  // ============================
  class HeaderViewHolder {
    // header
    TextView text;
  }

  @Override public View getHeaderView(int position, View convertView, ViewGroup parent) {
    HeaderViewHolder holder;

    if (convertView == null) {
      holder = new HeaderViewHolder();
      convertView = mInflater.inflate(R.layout.item_question_header, parent, false);
      holder.text = (TextView) convertView.findViewById(R.id.tv_header_title);
      convertView.setTag(holder);
    } else {
      holder = (HeaderViewHolder) convertView.getTag();
    }

    holder.text.setText(dishes.get(position).getCategory() + "");

    return convertView;
  }

  @Override public long getHeaderId(int position) {
    // 获取一个类别的id
    return dishes.get(position).id;
  }
}
