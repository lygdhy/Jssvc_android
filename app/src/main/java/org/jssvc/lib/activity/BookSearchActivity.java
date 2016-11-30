package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookSearchAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookSearchBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 图书搜索
 */
public class BookSearchActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.edtKey)
    EditText edtKey;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    String[] searchTypes = new String[]{"题名", "责任者", "主题词", "ISBN/ISSN", "索书号", "出版社"};
    String[] searchTypesCode = new String[]{"title", "author", "keyword", "isbn", "callno", "publisher"};
    int typePos = 0;
    int searchPage = 1;

    BookSearchAdapter bookSearchAdapter;
    List<BookSearchBean> booklists = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_search;
    }

    @Override
    protected void initView() {
        rlEmpty.setVisibility(View.GONE);

        tvType.setText(searchTypes[typePos]);
        edtKey.setHint("请输入" + searchTypes[typePos]);

        edtKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String key = edtKey.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        doSearch(true, key);
                    }
                    return true;
                }
                return false;
            }
        });

        //创建默认的线性LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        bookSearchAdapter = new BookSearchAdapter(context, booklists);
        recyclerView.setAdapter(bookSearchAdapter);

        bookSearchAdapter.setOnItemClickListener(new BookSearchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, BookSearchBean item) {
                if (!TextUtils.isEmpty(item.getDetialUrl())) {
                    Intent intent = new Intent(context, BookDetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("url", item.getDetialUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.tvBack, R.id.tvType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvType:
                showTypeDialog();
                break;
        }
    }

    // 发起搜索
    private void doSearch(final boolean isRefresh, String keyword) {
        if (isRefresh) {
            searchPage = 1;
        } else {
            searchPage++;
        }
        OkGo.post(HttpUrlParams.URL_LIB_BOOK_SEARCH)
                .tag(this)
                .params("strSearchType", searchTypesCode[typePos])
                .params("strText", keyword)
                .params("sort", "CATA_DATE")
                .params("orderby", "DESC")
                .params("showmode", "list")

                .params("dept", "ALL")
                .params("displaypg", "20")
                .params("page", String.valueOf(searchPage))

                .params("historyCount", "1")
                .params("doctype", "ALL")
                .params("match_flag", "forward")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // s 即为所需要的结果
                        parseHtml(isRefresh, s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("onError -> HttpUrlParams.BASE_LIB_URL");
                    }
                });
    }

    // 解析网页
    private void parseHtml(boolean isRefresh, String s) {
        if (isRefresh) {
            booklists.clear();
        }

        List<BookSearchBean> list = new ArrayList<>();
        list.addAll(HtmlParseUtils.getBookSearchList(s));

        if (list.size() > 0) {
            booklists.addAll(list);
            bookSearchAdapter.notifyDataSetChanged();
        } else {
            showToast("没有获取到新数据");
        }

        if (booklists.size() > 0) {
            rlEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    // 方式选择
    private void showTypeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("请选择搜索方式")
                .setSingleChoiceItems(searchTypes, typePos,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                typePos = which;
                                tvType.setText(searchTypes[typePos]);
                                edtKey.setHint("请输入" + searchTypes[typePos]);
                                dialog.dismiss();
                            }
                        }
                ).show();
    }
}
