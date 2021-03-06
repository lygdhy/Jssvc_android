package org.jssvc.lib.ui.book;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.adapter.BookReturnAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.ui.book.bean.BookReadingBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static org.jssvc.lib.base.BaseApplication.libOnline;

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

        if (libOnline) {
            loadBookList();
        } else {
            showToast("暂时无法使用");
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
        OkGo.<String>post(HttpUrlParams.URL_LIB_HISTORY_BORROW).tag(this)
                .params("para_string", "all")// all 显示全部; page 分页显示
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        parseHtml(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dealNetError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        bookReturnAdapter = new BookReturnAdapter(mContext, bookList);
        recyclerView.setAdapter(bookReturnAdapter);

        bookReturnAdapter.setOnItemClickListener(
                new BookReturnAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, BookReadingBean item) {
                        if (!TextUtils.isEmpty(item.getDetialUrl())) {
                            Intent intent = new Intent(mContext, BookDetailsActivity.class);
                            intent.putExtra("title", item.getBookName());
                            intent.putExtra("url", item.getDetialUrl());
                            startActivity(intent);
                        }
                    }
                });
    }
}
