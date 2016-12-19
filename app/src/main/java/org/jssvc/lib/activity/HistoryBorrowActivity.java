package org.jssvc.lib.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookReturnAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookReadingBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 图书搜索
 */
public class HistoryBorrowActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    BookReturnAdapter bookReturnAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_borrow;
    }

    @Override
    protected void initView() {
        rlEmpty.setVisibility(View.GONE);

        if (AccountPref.isLogon(context)) {
            loadBookList();
        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }

    @OnClick({R.id.tvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }

    private void loadBookList() {
        showProgressDialog();
        OkGo.post(HttpUrlParams.URL_LIB_HISTORY_BORROW)
                .tag(this)
                .params("para_string", "all")// all 显示全部; page 分页显示
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        // s 即为所需要的结果
                        parseHtml(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        dealNetError(e);
                    }
                });
    }

    // 解析网页
    private void parseHtml(String s) {
        List<BookReadingBean> bookList = new ArrayList<>();
        bookList = HtmlParseUtils.getReturnBorrowList(s);
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
        bookReturnAdapter = new BookReturnAdapter(context, bookList);
        recyclerView.setAdapter(bookReturnAdapter);

        bookReturnAdapter.setOnItemClickListener(new BookReturnAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, BookReadingBean item) {
                if (!TextUtils.isEmpty(item.getDetialUrl())) {
                    Intent intent = new Intent(context, BookDetailsActivity.class);
                    intent.putExtra("title", item.getBookName());
                    intent.putExtra("url", item.getDetialUrl());
                    startActivity(intent);
                }
            }
        });
    }
}
