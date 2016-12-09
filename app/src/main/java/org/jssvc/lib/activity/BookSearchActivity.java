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
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookSearchAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookSearchBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

import static org.jssvc.lib.R.id.refreshLayout;

/**
 * 图书搜索
 */
public class BookSearchActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.edtKey)
    EditText edtKey;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    @BindView(R.id.lvHistory)
    RecyclerView lvHistory;//搜索记录

    @BindView(refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//查询数据

    BookSearchAdapter bookSearchAdapter;
    List<BookSearchBean> booklists = new ArrayList<>();

    String[] searchTypes = new String[]{"题名", "责任者", "主题词", "ISBN/ISSN", "索书号", "出版社"};
    String[] searchTypesCode = new String[]{"title", "author", "keyword", "isbn", "callno", "publisher"};
    int typePos = 0;
    int searchPage = 1;
    String searchText = "";
    int maxPageSize = 1;// 最大页数，根据返回值动态计算

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_search;
    }

    @Override
    protected void initView() {
        lvHistory.setVisibility(View.GONE);//搜索记录

        initRefreshLayout();

        rlEmpty.setVisibility(View.GONE);

        tvType.setText(searchTypes[typePos]);
        edtKey.setHint("请输入" + searchTypes[typePos]);

        edtKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchText = edtKey.getText().toString().trim();
                    if (!TextUtils.isEmpty(searchText)) {
                        searchBookEngine(true);
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

    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.ui_theme);
        moocStyleRefreshViewHolder.setOriginalImage(R.drawable.icon_book_open);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
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

    // 图书检索
    private void searchBookEngine(final boolean isRefresh) {
        if (isRefresh) {
            searchPage = 1;
        } else {
            searchPage++;
        }

        showProgressDialog("检索中...");

        // 搜索事件统计
        Map<String, String> map = new HashMap<>();
        map.put("userName", AccountPref.getLogonAccoundNumber(context));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        map.put("optDate", df.format(new Date()));
        map.put("strSearchType", searchTypesCode[typePos]);
        map.put("strText", searchText);
        MobclickAgent.onEvent(context, "book_search", map);

        OkGo.post(HttpUrlParams.URL_LIB_BOOK_SEARCH)
                .tag(this)
                .params("strSearchType", searchTypesCode[typePos])
                .params("strText", searchText)
                .params("page", String.valueOf(searchPage))

                .params("sort", "CATA_DATE")
                .params("orderby", "DESC")
                .params("showmode", "list")
                .params("dept", "ALL")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        if (isRefresh) {
                            mRefreshLayout.endRefreshing();
                        } else {
                            mRefreshLayout.endLoadingMore();
                        }
                        // s 即为所需要的结果
                        parseHtml(isRefresh, s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        if (isRefresh) {
                            mRefreshLayout.endRefreshing();
                        } else {
                            mRefreshLayout.endLoadingMore();
                        }
                        dealNetError(e);
                    }
                });
    }

    // 解析网页
    private void parseHtml(boolean isRefresh, String s) {
        if (isRefresh) {
            booklists.clear();
        }

        // 获取图书数据
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

        // 获取搜索总数
        int count = HtmlParseUtils.getBookSearchListCount(s);
        int rem = count % 20;
        maxPageSize = (rem == 0) ? (count / 20) : (count / 20) + 1;
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (TextUtils.isEmpty(searchText)) {
            mRefreshLayout.endRefreshing();
        } else {
            searchBookEngine(true);
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (TextUtils.isEmpty(searchText)) {
            mRefreshLayout.endRefreshing();
        } else {
            // 判断是否还有下一页
            if (searchPage < maxPageSize) {
                searchBookEngine(false);
            } else {
                showToast("木有更多数据了");
            }
        }
        return false;
    }
}
