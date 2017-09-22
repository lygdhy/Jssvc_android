package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.utils.ImageLoader;

/**
 * Created by lygdh on 2016/11/14.
 */

public class ArticleAdapter extends BGARecyclerViewAdapter<ArticleListBean> {

  public ArticleAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.item_msg);
  }

  @Override
  protected void fillData(BGAViewHolderHelper helper, int position, ArticleListBean model) {
    ImageLoader.with(mContext, helper.getImageView(R.id.msgCover), model.getBanner());

    helper.getTextView(R.id.tvTitle).setText(model.getTitle() + "");
    helper.getTextView(R.id.tvAuthor).setText(model.getAuthor() + "");
    helper.getTextView(R.id.tvDate).setText(model.getAddtime() + "");
  }
}
