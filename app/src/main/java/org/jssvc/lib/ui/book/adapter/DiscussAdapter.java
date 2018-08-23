package org.jssvc.lib.ui.book.adapter;

import android.support.v7.widget.RecyclerView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.bean.DiscussBean;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DiscussAdapter extends BGARecyclerViewAdapter<DiscussBean> {

  public DiscussAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.item_discuss);
  }

  @Override protected void fillData(BGAViewHolderHelper helper, int position, DiscussBean model) {
    //helper.getTextView(R.id.tv_title).setText(model.getSorttitle());
    //helper.getTextView(R.id.tv_count).setText("x" + model.getQty());
  }
}
