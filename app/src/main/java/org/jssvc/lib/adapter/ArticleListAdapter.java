package org.jssvc.lib.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.bean.ArticleBean;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by lygdh on 2016/9/26.
 */

public class ArticleListAdapter extends BGAAdapterViewAdapter<ArticleBean> {

    public ArticleListAdapter(Context context) {
        super(context, R.layout.item_article);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_normal_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_normal_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, ArticleBean model) {
        viewHolderHelper.setText(R.id.tv_item_normal_title, model.getTitle());
        viewHolderHelper.setText(R.id.tv_item_normal_detail, model.getAuthor() + "  " + model.getDate());
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)viewHolderHelper.getView(R.id.draweeView);
        simpleDraweeView.setImageURI(model.getPic());
    }
}