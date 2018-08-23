package org.jssvc.lib.ui.book;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.adapter.BookSearchAdapter;
import org.jssvc.lib.ui.book.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.adapter.KeyHisAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.ui.book.bean.BookSearchBean;
import org.jssvc.lib.ui.general.bean.KVBean;
import org.jssvc.lib.ui.account.bean.ListSelecterBean;
import org.jssvc.lib.ui.account.bean.ThirdAccountBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 图书搜索
 */
public class BookSearchActivity extends BaseActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener,
        BGAOnRVItemLongClickListener {

    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.edt_input)
    EditText edtInput;

    @BindView(R.id.refresh_layout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//查询数据

    @BindView(R.id.his_recycler)
    RecyclerView hisRecycler;//搜索历史

    // 搜索结果
    BookSearchAdapter bookSearchAdapter;

    // 搜索历史
    KeyHisAdapter hisAdapter;

    List<ListSelecterBean> searchTypeList = new ArrayList<>();
    ListSelecterBean currentSearchType;

    int searchPage = 1;
    String searchText = "";
    int maxPageSize = 1;// 最大页数，根据返回值动态计算

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_search;
    }

    @Override
    protected void initView() {

        initRefreshLayout();

        // 初始化搜索类型
        initSearchType();

        edtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            searchText = edtInput.getText().toString().trim();
                            if (!TextUtils.isEmpty(searchText)) {
                                searchBookEngine(true);
                            }

                            // 保存搜索记录
                            DataSup.insertSearchHis(currentSearchType.getId(), searchText);
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mRefreshLayout.setVisibility(View.GONE);
                    hisRecycler.setVisibility(View.VISIBLE);

                    hisAdapter.setData(DataSup.getSearchHisList());
                }
            }
        });

        // ============================================
        //创建默认的线性LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        bookSearchAdapter = new BookSearchAdapter(recyclerView);
        recyclerView.setAdapter(bookSearchAdapter);
        bookSearchAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (!TextUtils.isEmpty(bookSearchAdapter.getData().get(position).getDetialUrl())) {
                    Intent intent = new Intent(mContext, BookDetailsActivity.class);
                    intent.putExtra("title", bookSearchAdapter.getData().get(position).getTitle());
                    intent.putExtra("url", bookSearchAdapter.getData().get(position).getDetialUrl());
                    startActivity(intent);
                }
            }
        });

        // =================================================
        // 加载历史
        hisRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
        hisRecycler.setItemAnimator(new DefaultItemAnimator());
        hisAdapter = new KeyHisAdapter(hisRecycler);
        hisAdapter.setOnRVItemClickListener(this);
        hisAdapter.setOnRVItemLongClickListener(this);
        hisRecycler.setAdapter(hisAdapter);
        hisAdapter.setData(DataSup.getSearchHisList());

        mRefreshLayout.setVisibility(View.GONE);
        hisRecycler.setVisibility(View.VISIBLE);
    }

    // 初始化搜索类型
    private void initSearchType() {
        searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "title", "题名", ""));//1275
        searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "keyword", "主题", ""));//216
        searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "author", "作者", ""));//96
        //searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "isbn", "ISBN/ISSN", ""));
        //searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "callno", "索书号", ""));
        //searchTypeList.add(new ListSelecterBean(R.drawable.icon_type, "publisher", "出版社", ""));

        // 默认选择第一个项目
        currentSearchType = searchTypeList.get(0);
        tvCategory.setText(currentSearchType.getTitle());
        edtInput.setHint("请输入" + currentSearchType.getTitle());
    }

    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder =
                new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.color_ui_theme);
        moocStyleRefreshViewHolder.setOriginalImage(R.drawable.icon_book_open);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                KeyboardUtils.hideSoftInput(this);
                finish();
                break;
            case R.id.tv_category:
                // 类型选择
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

        String account = "";
        ThirdAccountBean libBean = DataSup.getLibThirdAccount();
        if (libBean != null) {
            account = libBean.getAccount();
        }

        // 搜索事件统计
        Map<String, String> map = new HashMap<>();
        map.put("userName", account);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        map.put("optDate", df.format(new Date()));
        map.put("strSearchType", currentSearchType.getId());
        map.put("strText", searchText);
        MobclickAgent.onEvent(mContext, "book_search", map);

        OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_SEARCH).tag(this)
                .params("strSearchType", currentSearchType.getId())
                .params("strText", searchText)
                .params("page", String.valueOf(searchPage))

                .params("sort", "CATA_DATE")
                .params("orderby", "DESC")
                .params("showmode", "list")
                .params("dept", "ALL")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (mRefreshLayout != null) {
                            if (isRefresh) {
                                mRefreshLayout.endRefreshing();
                            } else {
                                mRefreshLayout.endLoadingMore();
                            }
                            parseHtml(isRefresh, response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mRefreshLayout != null) {
                            if (isRefresh) {
                                mRefreshLayout.endRefreshing();
                            } else {
                                mRefreshLayout.endLoadingMore();
                            }
                            dealNetError(response);
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("检索中...");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                    }
                });
    }

    // 解析网页
    private void parseHtml(boolean isRefresh, String s) {
        mRefreshLayout.setVisibility(View.VISIBLE);
        hisRecycler.setVisibility(View.GONE);

        // 获取图书数据
        List<BookSearchBean> templist = new ArrayList<>();
        templist.addAll(HtmlParseUtils.getBookSearchList(s));

        if (isRefresh) {
            bookSearchAdapter.setData(templist);
        } else {
            bookSearchAdapter.addMoreData(templist);
        }

        if (templist.size() == 0) {
            showToast("没有获取到新数据");
        }

        // 获取搜索总数
        int count = HtmlParseUtils.getBookSearchListCount(s);
        int rem = count % 20;
        maxPageSize = (rem == 0) ? (count / 20) : (count / 20) + 1;
    }

    // 方式选择
    private void showTypeDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
        dlg.show();
        dlg.getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_list_select_layout);

        TextView tvDialogTitle = (TextView) window.findViewById(R.id.tvDialogTitle);
        TextView tvDialogSubTitle = (TextView) window.findViewById(R.id.tvDialogSubTitle);
        tvDialogTitle.setText("请选择搜索方式");
        tvDialogSubTitle.setVisibility(View.GONE);

        DialogListSelecterAdapter selecterAdapter;
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        selecterAdapter = new DialogListSelecterAdapter(mContext, searchTypeList);
        recyclerView.setAdapter(selecterAdapter);

        selecterAdapter.setOnItemClickListener(new DialogListSelecterAdapter.IMyViewHolderClicks() {
            @Override
            public void onItemClick(View view, ListSelecterBean item) {
                currentSearchType = item;
                tvCategory.setText(currentSearchType.getTitle());
                edtInput.setHint("请输入" + currentSearchType.getTitle());
                dlg.dismiss();
            }
        });
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

    // 点击搜索
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (parent.getId() == R.id.his_recycler) {
            KVBean item = hisAdapter.getData().get(position);

            for (int i = 0; i < searchTypeList.size(); i++) {
                if (searchTypeList.get(i).getId().equals(item.getKey())) {
                    currentSearchType = searchTypeList.get(i);
                    tvCategory.setText(currentSearchType.getTitle());
                    edtInput.setHint("请输入" + currentSearchType.getTitle());
                    break;
                }
            }

            searchText = item.getValue();
            edtInput.setText(searchText);
            if (!TextUtils.isEmpty(searchText)) {
                searchBookEngine(true);
            }
        }
    }

    // 长按删除
    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        KVBean item = hisAdapter.getData().get(position);
        DataSup.deleteSearchHis(item.getValue());
        // 删除后刷新页面
        showToast("[" + item.getValue() + "] 删除成功！");
        hisAdapter.setData(DataSup.getSearchHisList());
        return false;
    }
}
