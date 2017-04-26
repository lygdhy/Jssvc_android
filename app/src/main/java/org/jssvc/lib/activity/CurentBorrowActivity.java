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

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookReadingAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookReadingBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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

    if (AccountPref.isLogon(context)) {
      loadBookList();
    } else {
      startActivity(new Intent(context, LoginActivity.class));
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
    showProgressDialog();
    OkGo.post(HttpUrlParams.URL_LIB_CURRENT_BORROW).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(String s, Call call, Response response) {
        dissmissProgressDialog();
        // s 即为所需要的结果
        parseHtml(s);
      }

      @Override public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        dissmissProgressDialog();
        dealNetError(e);
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
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    recyclerView.setHasFixedSize(true);
    //创建并设置Adapter
    bookReadingAdapter = new BookReadingAdapter(context, bookList);
    recyclerView.setAdapter(bookReadingAdapter);

    bookReadingAdapter.setOnItemClickListener(new BookReadingAdapter.IMyViewHolderClicks() {
      @Override public void onItemClick(View view, BookReadingBean item) {
        if (!TextUtils.isEmpty(item.getDetialUrl())) {
          Intent intent = new Intent(context, BookDetailsActivity.class);
          intent.putExtra("title", item.getBookName());
          intent.putExtra("url", item.getDetialUrl());
          startActivity(intent);
        }
      }

      @Override public void onXujieClick(View view, BookReadingBean item) {
        // 续借
        showProgressDialog("申请中...");

        // 续借事件统计
        Map<String, String> map = new HashMap<>();
        map.put("userName", AccountPref.getLogonAccoundNumber(context));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        map.put("optDate", df.format(new Date()));
        map.put("bookBarCode", item.getBarCode());
        map.put("bookTitle", item.getBookName());
        MobclickAgent.onEvent(context, "book_renew", map);

        OkGo.get(HttpUrlParams.URL_LIB_RENEW_BORROW)
            .tag(this)
            .params("bar_code", item.getBarCode())
            .params("time", String.valueOf(new Date().getTime()))
            .execute(new StringCallback() {
              @Override public void onSuccess(String s, Call call, Response response) {
                dissmissProgressDialog();
                // s 即为所需要的结果
                showResultDialog(s);
              }

              @Override public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dissmissProgressDialog();
                dealNetError(e);
              }
            });
      }
    });
  }

  // 续借结果
  private void showResultDialog(String result) {
    CustomDialog.Builder builder = new CustomDialog.Builder(context).setTitle("续借结果")
        .setMessage(Html.fromHtml(result) + "")
        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
          public void onClick(final DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    builder.create().show();
  }
}
