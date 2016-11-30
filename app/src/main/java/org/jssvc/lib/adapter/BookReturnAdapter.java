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

public class BookReturnAdapter extends RecyclerView.Adapter<BookReturnAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    public List<BookReadingBean> menuList = new ArrayList<>();

    public BookReturnAdapter(Context context, List<BookReadingBean> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_return, parent, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookReadingBean item = menuList.get(position);
        holder.tvTitle.setText(item.getBookName());
        holder.tvAuthor.setText("责任者：" + item.getBookAuthor() + "");
        holder.tvCode.setText("条码号：" + item.getBarCode() + "");
        holder.tvDate.setText("借阅期：" + item.getBorrowDate() + " ~ " + item.getReturnDate());
        holder.tvPlace.setText("馆藏地：" + item.getPlace() + "");

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvTitle, tvAuthor, tvDate, tvPlace;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvCode = (TextView) view.findViewById(R.id.tvCode);
            tvPlace = (TextView) view.findViewById(R.id.tvPlace);
        }
    }


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, BookReadingBean item);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (BookReadingBean) v.getTag());
        }
    }
}
