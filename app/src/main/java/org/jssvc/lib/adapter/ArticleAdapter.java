package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.ArticleListBean;
import org.jssvc.lib.utils.ImageLoader;

/**
 * Created by lygdh on 2016/11/14.
 */

public class ArticleAdapter extends BGARecyclerViewAdapter<ArticleListBean> {
  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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

      String[] imgArr = model.getBanner().split("\\|");
      if (imgArr.length > 0) {
        ImageLoader.with(mContext, banner, imgArr[0]);
      } else {
        banner.setVisibility(View.GONE);
      }
    }

    helper.getTextView(R.id.tvTitle).setText(model.getTitle() + "");
    helper.getTextView(R.id.tvAuthor).setText(model.getAuthor() + "");

    try {
      Date date = df.parse(model.getAddtime() + "");
      SimpleDateFormat newf = new SimpleDateFormat("yyyy-MM-dd");
      helper.getTextView(R.id.tvDate).setText(newf.format(date) + "");
    } catch (ParseException e) {
      e.printStackTrace();
      helper.getTextView(R.id.tvDate).setText(model.getAddtime() + "");
    }
  }
}
