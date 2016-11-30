package org.jssvc.lib.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.CategoryBean;

/**
 * QA 左侧列表
 */
public class QleftAdapter extends BaseAdapter {

    private List<CategoryBean> categoryBeanList;
    private LayoutInflater mInflater;
    private Context context;

    public QleftAdapter(Context context, List<CategoryBean> categoryBeanList) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.categoryBeanList = categoryBeanList;
    }

    @Override
    public int getCount() {
        return categoryBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_question_left, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.item_name);
            holder.view = (View) convertView.findViewById(R.id.view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (categoryBeanList.get(position).isSelected) {
            holder.text.setBackgroundColor(Color.WHITE);
            holder.view.setVisibility(View.VISIBLE);
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.ui_theme));
        } else {
            holder.text.setBackgroundColor(ContextCompat.getColor(context, R.color.ui_split));
            holder.view.setVisibility(View.INVISIBLE);
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.ui_text_body));
        }

        holder.text.setText(categoryBeanList.get(position).name);

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        View view;
    }

}
