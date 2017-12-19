package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.umeng.analytics.MobclickAgent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookReadingAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookReadingBean;
import org.jssvc.lib.bean.ThirdAccountBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomDialog;

import static org.jssvc.lib.base.BaseApplication.libOnline;

/**
 * 借阅历史
 */
public class CurentBorrowActivity extends BaseActivity {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.rlEmpty) RelativeLayout rlEmpty;

  BookReadingAdapter bookReadingAdapter;

  @Override protected int getContentViewId() {
    return R.layout.activity_curent_borrow;
  }

  @Override protected void initView() {
    rlEmpty.setVisibility(View.GONE);

    if (libOnline) {
      loadBookList();
    } else {
      showToast("暂时无法使用");
      finish();
    }
  }

  @OnClick({ R.id.tvBack }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
    }
  }

  // 获取图书列表
  private void loadBookList() {
    OkGo.<String>post(HttpUrlParams.URL_LIB_CURRENT_BORROW).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(Response<String> response) {
        parseHtml(response.body());
      }

      @Override public void onError(Response<String> response) {
        super.onError(response);
        dealNetError(response);
      }

      @Override public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        showProgressDialog();
      }

      @Override public void onFinish() {
        super.onFinish();
        dissmissProgressDialog();
      }
    });
  }

  // 解析网页
  private void parseHtml(String s) {
    List<BookReadingBean> bookList = new ArrayList<>();
    bookList = HtmlParseUtils.getCurrentBorrowList(s);
    if (bookList.size() > 0) {
      rlEmpty.setVisibility(View.GONE);
      loadList(bookList);
    } else {
      rlEmpty.setVisibility(View.VISIBLE);
    }
  }

  private void loadList(List<BookReadingBean> bookList) {
    //创建默认的线性LayoutManager
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    recyclerView.setHasFixedSize(true);
    //创建并设置Adapter
    bookReadingAdapter = new BookReadingAdapter(mContext, bookList);
    recyclerView.setAdapter(bookReadingAdapter);

    bookReadingAdapter.setOnItemClickListener(new BookReadingAdapter.IMyViewHolderClicks() {
      @Override public void onItemClick(View view, BookReadingBean item) {
        if (!TextUtils.isEmpty(item.getDetialUrl())) {
          Intent intent = new Intent(mContext, BookDetailsActivity.class);
          intent.putExtra("title", item.getBookName());
          intent.putExtra("url", item.getDetialUrl());
          startActivity(intent);
        }
      }

      @Override public void onXujieClick(View view, BookReadingBean item) {

        String account = "";
        ThirdAccountBean libBean = DataSup.getLibThirdAccount();
        if (libBean != null) {
          account = libBean.getAccount();
        }

        // 续借事件统计
        Map<String, String> map = new HashMap<>();
        map.put("userName", account);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        map.put("optDate", df.format(new Date()));
        map.put("bookBarCode", item.getBarCode());
        map.put("bookTitle", item.getBookName());
        MobclickAgent.onEvent(mContext, "book_renew", map);

        OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_ADD).tag(this)
            .params("bar_code", item.getBarCode())
            .params("time", String.valueOf(new Date().getTime()))
            .execute(new StringCallback() {
              @Override public void onSuccess(Response<String> response) {
                showResultDialog(response.body());
              }

              @Override public void onError(Response<String> response) {
                super.onError(response);
                dealNetError(response);
              }

              @Override public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                showProgressDialog("申请中...");
              }

              @Override public void onFinish() {
                super.onFinish();
                dissmissProgressDialog();
              }
            });
      }
    });
  }

  // 续借结果
  private void showResultDialog(String result) {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext).setTitle("续借结果")
        .setMessage(Html.fromHtml(result) + "")
        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
          public void onClick(final DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    builder.create().show();
  }
}
