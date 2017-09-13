package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.utils.ImageLoader;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/13
 *     desc   : 菜单适配器
 *     version: 1.0
 * </pre>
 */
public class MenuAdapter extends BGARecyclerViewAdapter<MenuBean> {
  public MenuAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.item_home_menu);
  }

  @Override protected void fillData(BGAViewHolderHelper helper, int position, MenuBean model) {
    helper.getTextView(R.id.title).setText(model.getTitle() + "");
    ImageView imageView = helper.getImageView(R.id.icon);

    if (!TextUtils.isEmpty(model.getIcon_url())) {
      ImageLoader.with(mContext, imageView, model.getIcon_url());
    } else {
      if (model.getResourceId() == 0) {
        ImageLoader.with(mContext, imageView, R.drawable.ic_launcher);
      } else {
        ImageLoader.with(mContext, imageView, model.getResourceId());
      }
    }
  }
}
