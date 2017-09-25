package org.jssvc.lib.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.umeng.analytics.MobclickAgent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookSearchAdapter;
import org.jssvc.lib.adapter.BookSearchHisAdapter;
import org.jssvc.lib.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookSearchBean;
import org.jssvc.lib.bean.ListSelecterBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.AppPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.DividerItemDecoration;

/**
 * 图书搜索
 */
public class BookSearchActivity extends BaseActivity
    implements BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.tv_category) TextView tvCategory;
  @BindView(R.id.edt_input) EditText edtInput;
  @BindView(R.id.iv_search) ImageView ivSearch;
  @BindView(R.id.rl_empty) RelativeLayout rlEmpty;

  @BindView(R.id.his_layout) LinearLayout hisLayout;//搜索记录
  @BindView(R.id.lv_history) RecyclerView lvHistory;
  @BindView(R.id.tv_delete_his) TextView tvDeleteHis;

  @BindView(R.id.refresh_layout) BGARefreshLayout mRefreshLayout;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;//查询数据

  // 搜索记录
  List<String> hislist = new ArrayList<String>();
  BookSearchHisAdapter hislistAdapter;

  // 搜索结果
  BookSearchAdapter bookSearchAdapter;
  List<BookSearchBean> booklists = new ArrayList<>();

  List<ListSelecterBean> searchTypeList = new ArrayList<>();
  ListSelecterBean currentSearchType;

  int searchPage = 1;
  String searchText = "";
  int maxPageSize = 1;// 最大页数，根据返回值动态计算

  @Override protected int getContentViewId() {
    return R.layout.activity_book_search;
  }

  @Override protected void initView() {
    ivSearch.setVisibility(View.GONE);
    hisLayout.setVisibility(View.GONE);//搜索记录

    initRefreshLayout();

    rlEmpty.setVisibility(View.GONE);

    // 初始化搜索类型
    initSearchType();

    edtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null
            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
          searchText = edtInput.getText().toString().trim();
          if (!TextUtils.isEmpty(searchText)) {
            saveKey2Local(searchText);
            searchBookEngine(true);
          }
          return true;
        }
        return false;
      }
    });

    edtInput.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
          ivSearch.setVisibility(View.GONE);

          // 隐显
          rlEmpty.setVisibility(View.GONE);
          recyclerView.setVisibility(View.GONE);
          hisLayout.setVisibility(View.VISIBLE);
          reLoadSearchHis();
        } else {
          ivSearch.setVisibility(View.VISIBLE);
        }
      }
    });

    // ============================================
    lvHistory.setLayoutManager(new LinearLayoutManager(mContext));
    lvHistory.setHasFixedSize(true);
    hislistAdapter = new BookSearchHisAdapter(mContext, hislist);
    lvHistory.setAdapter(hislistAdapter);

    hislistAdapter.setOnItemClickListener(
        new BookSearchHisAdapter.OnRecyclerViewItemClickListener() {
          @Override public void onItemClick(View view, String item) {
            if (!TextUtils.isEmpty(item)) {
              searchText = item;
              edtInput.setText(item);
              saveKey2Local(searchText);
              searchBookEngine(true);
            }
          }
        });
    reLoadSearchHis();// 加载搜索记录

    // ============================================
    //创建默认的线性LayoutManager
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setHasFixedSize(true);
    bookSearchAdapter = new BookSearchAdapter(mContext, booklists);
    recyclerView.setAdapter(bookSearchAdapter);

    bookSearchAdapter.setOnItemClickListener(
        new BookSearchAdapter.OnRecyclerViewItemClickListener() {
          @Override public void onItemClick(View view, BookSearchBean item) {
            if (!TextUtils.isEmpty(item.getDetialUrl())) {
              Intent intent = new Intent(mContext, BookDetailsActivity.class);
              intent.putExtra("title", item.getTitle());
              intent.putExtra("url", item.getDetialUrl());
              startActivity(intent);
            }
          }
        });
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

  @OnClick({ R.id.tv_cancel, R.id.tv_category, R.id.iv_search, R.id.tv_delete_his })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_cancel:
        finish();
        break;
      case R.id.tv_category:
        // 类型选择
        showTypeDialog();
        break;
      case R.id.iv_search:
        // 搜索
        searchText = edtInput.getText().toString().trim();
        if (!TextUtils.isEmpty(searchText)) {
          saveKey2Local(searchText);
          searchBookEngine(true);
        }
        break;
      case R.id.tv_delete_his:
        // 清空历史
        AppPref.clearSearchKey(mContext);
        reLoadSearchHis();
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

    // 搜索事件统计
    Map<String, String> map = new HashMap<>();
    map.put("userName", AccountPref.getLogonAccoundNumber(mContext));
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
          @Override public void onSuccess(Response<String> response) {
            if (mRefreshLayout != null) {
              if (isRefresh) {
                mRefreshLayout.endRefreshing();
              } else {
                mRefreshLayout.endLoadingMore();
              }
              parseHtml(isRefresh, response.body());
            }
          }

          @Override public void onError(Response<String> response) {
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

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("检索中...");
            hisLayout.setVisibility(View.GONE);
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });

    //OkGo.post(HttpUrlParams.URL_LIB_BOOK_SEARCH)
    //    .tag(this)
    //    .params("strSearchType", currentSearchType.getId())
    //    .params("strText", searchText)
    //    .params("page", String.valueOf(searchPage))
    //
    //    .params("sort", "CATA_DATE")
    //    .params("orderby", "DESC")
    //    .params("showmode", "list")
    //    .params("dept", "ALL")
    //    .execute(new StringCallback() {
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        dissmissProgressDialog();
    //        if (mRefreshLayout != null) {
    //          if (isRefresh) {
    //            mRefreshLayout.endRefreshing();
    //          } else {
    //            mRefreshLayout.endLoadingMore();
    //          }
    //          // s 即为所需要的结果
    //          parseHtml(isRefresh, s);
    //        }
    //      }
    //
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dissmissProgressDialog();
    //        if (mRefreshLayout != null) {
    //          if (isRefresh) {
    //            mRefreshLayout.endRefreshing();
    //          } else {
    //            mRefreshLayout.endLoadingMore();
    //          }
    //          dealNetError(e);
    //        }
    //      }
    //    });
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
      @Override public void onItemClick(View view, ListSelecterBean item) {
        currentSearchType = item;
        tvCategory.setText(currentSearchType.getTitle());
        edtInput.setHint("请输入" + currentSearchType.getTitle());
        dlg.dismiss();
      }
    });
  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    if (TextUtils.isEmpty(searchText)) {
      mRefreshLayout.endRefreshing();
    } else {
      searchBookEngine(true);
    }
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
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

  // =================================================
  // 加载搜索记录
  private void reLoadSearchHis() {
    hislist.clear();
    String allkeys = AppPref.getSearchKey(mContext);
    String[] hisArrays = allkeys.split(",");
    for (int i = 0; i < hisArrays.length; i++) {
      if (!TextUtils.isEmpty(hisArrays[i])) hislist.add(hisArrays[i]);
    }

    if (hislist.size() > 0) {
      hisLayout.setVisibility(View.VISIBLE);
      Collections.reverse(hislist);
      hislistAdapter.notifyDataSetChanged();
    } else {
      hisLayout.setVisibility(View.GONE);
    }
  }

  // 保存关键字到本地
  private void saveKey2Local(String key) {
    if (!TextUtils.isEmpty(key)) {
      // 1、读取本地数据
      String allkeys = AppPref.getSearchKey(mContext);
      // 2、重复性判断
      int pos = 0;
      int maxlong = 5;
      boolean same = false;
      String[] hisArrays = allkeys.split(",");
      List<String> hisArr = new ArrayList<>();
      for (int i = 0; i < hisArrays.length; i++) {
        hisArr.add("" + hisArrays[i]);
      }
      for (int i = 0; i < hisArr.size(); i++) {
        if (hisArr.get(i).equals(key)) {
          pos = i;
          same = true;
        }
      }
      if (same) {
        hisArr.remove(pos);
      }
      hisArr.add(key);
      // 3、处理溢出
      if (hisArr.size() > maxlong) {
        hisArr.remove(0);
      }

      // 4、存储
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < hisArr.size(); i++) {
        sb.append(hisArr.get(i) + ",");
      }
      AppPref.saveSearchKey(mContext, sb.toString());
    }
  }
}
