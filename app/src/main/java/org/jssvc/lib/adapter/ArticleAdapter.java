package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout imageLayout = helper.getView(R.id.image_layout);
    ImageView cover0 = helper.getImageView(R.id.cover_0);
    ImageView cover1 = helper.getImageView(R.id.cover_1);
    ImageView cover2 = helper.getImageView(R.id.cover_2);
    ImageView cover3 = helper.getImageView(R.id.cover_3);

    imageLayout.setVisibility(View.GONE);

    if (TextUtils.isEmpty(model.getBanner())) {
      imageLayout.setVisibility(View.GONE);
      cover0.setVisibility(View.GONE);
    } else {
      String[] imgArr = model.getBanner().split("\\|");
      if (imgArr.length >= 3) {
        imageLayout.setVisibility(View.VISIBLE);
        cover0.setVisibility(View.GONE);
        cover1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        cover2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        cover3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.with(mContext, cover1, imgArr[0]);
        ImageLoader.with(mContext, cover2, imgArr[1]);
        ImageLoader.with(mContext, cover3, imgArr[2]);
      } else {
        imageLayout.setVisibility(View.GONE);
        cover0.setVisibility(View.VISIBLE);
        cover0.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.with(mContext, cover0, imgArr[0]);
      }
    }

    helper.getTextView(R.id.tv_title).setText(model.getTitle() + "");
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
