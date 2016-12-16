package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.BookDetailsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/17.
 */

public class BookDetailsAdapter extends RecyclerView.Adapter<BookDetailsAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    public List<BookDetailsBean> menuList = new ArrayList<>();

    public BookDetailsAdapter(Context context, List<BookDetailsBean> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_details, parent, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookDetailsBean item = menuList.get(position);

//        if (item.getValue().length() > 20) {
        holder.lageLayout.setVisibility(View.VISIBLE);
        holder.smallLayout.setVisibility(View.GONE);
        holder.tvKey2.setText(item.getKey());
        holder.tvValue2.setText(item.getValue());
//        } else {
//            holder.lageLayout.setVisibility(View.GONE);
//            holder.smallLayout.setVisibility(View.VISIBLE);
//            holder.tvKey1.setText(item.getKey());
//            holder.tvValue1.setText(item.getValue());
//        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey1, tvValue1, tvKey2, tvValue2;
        LinearLayout lageLayout;
        RelativeLayout smallLayout;

        public ViewHolder(View view) {
            super(view);
            tvKey1 = (TextView) view.findViewById(R.id.tvKey1);
            tvValue1 = (TextView) view.findViewById(R.id.tvValue1);
            tvKey2 = (TextView) view.findViewById(R.id.tvKey2);
            tvValue2 = (TextView) view.findViewById(R.id.tvValue2);
            lageLayout = (LinearLayout) view.findViewById(R.id.lageLayout);
            smallLayout = (RelativeLayout) view.findViewById(R.id.smallLayout);
        }
    }


    private BookDetailsAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, BookDetailsBean item);
    }

    public void setOnItemClickListener(BookDetailsAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (BookDetailsBean) v.getTag());
        }
    }
}
