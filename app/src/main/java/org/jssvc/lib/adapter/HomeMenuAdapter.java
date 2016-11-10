package org.jssvc.lib.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页书签菜单
 * Created by lygdh on 2016/9/29.
 */

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {

    Context context;
    public List<String> datas = new ArrayList<>();

    public HomeMenuAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_menu, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(datas.get(position));
        switch (position) {
            case 0:
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_menu_home));
                holder.draweeView.setImageURI("http://www.hydong.me/app/adPics/bg_bookmark_demo.jpg");
                break;
            case 1:
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_menu_news));
                holder.draweeView.setImageURI("http://www.hydong.me/app/adPics/bg_bookmark_demo.jpg");
                break;
            case 2:
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_menu_find));
                holder.draweeView.setImageURI("http://www.hydong.me/app/adPics/bg_bookmark_demo.jpg");
                break;
            case 3:
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_menu_mine));
                holder.draweeView.setImageURI("http://www.hydong.me/app/adPics/bg_bookmark_demo.jpg");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        CardView cardView;
        SimpleDraweeView draweeView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            cardView = (CardView) view.findViewById(R.id.cardView);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
        }
    }
}
