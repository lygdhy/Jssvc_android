package org.jssvc.lib.adapter;

import android.support.v7.widget.RecyclerView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import org.jssvc.lib.R;
import org.jssvc.lib.bean.KVBean;

/**
 * Created by lygdh on 2016/11/14.
 */

public class KeyHisAdapter extends BGARecyclerViewAdapter<KVBean> {

  public KeyHisAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.item_book_search_his);
  }

  @Override protected void fillData(BGAViewHolderHelper helper, int position, KVBean model) {
    helper.getTextView(R.id.tv_name).setText(model.getValue() + "");
  }
}
