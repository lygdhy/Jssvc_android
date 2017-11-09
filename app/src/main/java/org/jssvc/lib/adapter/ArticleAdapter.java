package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.utils.ImageLoader;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by lygdh on 2016/11/14.
 */

public class ArticleAdapter extends BGARecyclerViewAdapter<ArticleListBean> {

  public ArticleAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.item_article);
  }

  @Override
  protected void fillData(BGAViewHolderHelper helper, int position, ArticleListBean model) {
    ImageView banner = helper.getImageView(R.id.msgCover);
    if (TextUtils.isEmpty(model.getBanner())) {
      banner.setVisibility(View.GONE);
    } else {
      banner.setVisibility(View.VISIBLE);
      ImageLoader.with(mContext, banner, model.getBanner());
    }

    helper.getTextView(R.id.tvTitle).setText(model.getTitle() + "");
    helper.getTextView(R.id.tvAuthor).setText(model.getAuthor() + "");
    helper.getTextView(R.id.tvDate).setText(model.getAddtime() + "");
  }
}
