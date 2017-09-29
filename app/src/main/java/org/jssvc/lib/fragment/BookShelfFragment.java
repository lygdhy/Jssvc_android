package org.jssvc.lib.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.BookDetailsActivity;
import org.jssvc.lib.adapter.BookShelfListAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.BookShelfListBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomDialog;

/**
 * 书架
 */
public class BookShelfFragment extends BaseFragment {

  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.emptyLayout) LinearLayout emptyLayout;

  BookShelfListAdapter bookShelfListAdapter;
  List<BookShelfListBean> bookList = new ArrayList<>();

  String baseUrl = "";
  String classid = "";// 书架ID

  public BookShelfFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_book_shelf;
  }

  @Override protected void initView() {
    emptyLayout.setVisibility(View.GONE);
    recyclerView.setVisibility(View.GONE);

    Bundle bundle = getArguments();//从activity传过来的Bundle
    if (bundle != null) {
      baseUrl = bundle.getString("url");

      String[] array = baseUrl.split("classid=");
      if (array.length == 2) {
        classid = "" + array[1];
      } else {
        classid = "";
      }
      loadBookList();
    }
  }

  private void loadBookList() {
    OkGo.<String>post(baseUrl).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(Response<String> response) {
        dissmissProgressDialog();
        parseHtml(response.body());
      }

      @Override public void onError(Response<String> response) {
        super.onError(response);
        dissmissProgressDialog();
        dealNetError(response);
      }
    });

    //OkGo.post(baseUrl).tag(this).execute(new StringCallback() {
    //  @Override public void onSuccess(String s, Call call, Response response) {
    //    // s 即为所需要的结果
    //    parseHtml(s);
    //  }
    //
    //  @Override public void onError(Call call, Response response, Exception e) {
    //    super.onError(call, response, e);
    //    dealNetError(e);
    //  }
    //});
  }

  // 解析网页
  private void parseHtml(String s) {
    bookList.clear();
    bookList = HtmlParseUtils.getBookOnShelfList(s);

    if (bookList.size() > 0) {
      emptyLayout.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      loadBookList(bookList);
    } else {
      // 书架没有图书
      emptyLayout.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }
  }

  private void loadBookList(List<BookShelfListBean> bookList) {
    //创建默认的线性LayoutManager
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    recyclerView.setHasFixedSize(true);
    //解决滑动冲突
    recyclerView.setNestedScrollingEnabled(false);
    //创建并设置Adapter
    bookShelfListAdapter = new BookShelfListAdapter(mContext, bookList);
    recyclerView.setAdapter(bookShelfListAdapter);

    bookShelfListAdapter.setOnItemClickListener(new BookShelfListAdapter.IMyViewHolderClicks() {
      @Override public void onItemClick(View view, BookShelfListBean item) {
        // 查看详情
        if (!TextUtils.isEmpty(item.getUrl())) {
          Intent intent = new Intent(mContext, BookDetailsActivity.class);
          intent.putExtra("title", item.getName());
          intent.putExtra("url", item.getUrl());
          startActivity(intent);
        }
      }

      @Override public void onRemoveClick(View view, BookShelfListBean item) {
        // 移除图书
        // classid
        String bookcode = item.getBookcode();
        if (!TextUtils.isEmpty(bookcode)) {
          deleteAlertDialog(classid, bookcode);
        }
      }
    });
  }

  // 删除书架
  public void deleteAlertDialog(final String classid, final String bookcode) {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
    builder.setTitle("提示");
    builder.setMessage("确认要从书架中删除吗?");
    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();

        removeBook(classid, bookcode);
      }
    });
    builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.create().show();
  }

  // 移除图书
  private void removeBook(String classid, String bookcode) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_DEL).tag(this)
        .params("classid", classid)
        .params("marc_no", bookcode)
        .params("time", System.currentTimeMillis())
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            showAlertDialog(response.body());
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

  // 删除结果显示
  public void showAlertDialog(final String str) {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
    builder.setTitle("提示");
    builder.setMessage(Html.fromHtml(str) + "");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();
        loadBookList();
      }
    });
    builder.create().show();
  }
}
