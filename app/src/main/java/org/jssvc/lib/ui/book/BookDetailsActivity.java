package org.jssvc.lib.ui.book;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.jssvc.lib.R;
import org.jssvc.lib.ui.book.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.ui.home.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.ui.book.bean.BookAccessBean;
import org.jssvc.lib.ui.book.bean.BookDetailsBean;
import org.jssvc.lib.ui.book.bean.BookShelfBean;
import org.jssvc.lib.ui.account.bean.ListSelecterBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.ui.book.fragment.BookDetailInfoFragment;
import org.jssvc.lib.ui.book.fragment.BookDetailQtyFragment;
import org.jssvc.lib.ui.login.LoginActivity;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.utils.ImageLoader;
import org.jssvc.lib.view.CustomDialog;
import org.jssvc.lib.view.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static org.jssvc.lib.base.BaseApplication.libOnline;

/**
 * 图书详情
 */
public class BookDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_book_cover)
    ImageView ivBookCover;
    @BindView(R.id.tv_book_title)
    TextView tvBookTitle;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ShowTabAdapter showTabAdapter;

    String marc_no = "";
    String detialUrl = "";
    List<BookShelfBean> shelfList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_details;
    }

    @Override
    protected void initView() {

        tvBookTitle.setText(getIntent().getStringExtra("title") + "");
        detialUrl = getIntent().getStringExtra("url");

        String[] array = detialUrl.split("marc_no=");
        if (array.length == 2) {
            marc_no = array[1] + "";
        } else {
            marc_no = "";
        }

        OkGo.<String>post(detialUrl).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dissmissProgressDialog();
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

    @OnClick({R.id.tv_back, R.id.tv_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_collect:
                if (DataSup.hasUserLogin()) {
                    if (libOnline) {
                        collectBook();// 添加到书架
                    } else {
                        showToast("暂时无法使用");
                    }
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
        }
    }

    // 添加图书
    private void collectBook() {
        if (shelfList.size() > 0) {
            add2BookShelf();
        } else {
            // 获取当前可用书架列表
            getBookShelf();
        }
    }

    // 添加图书到书架
    private void add2BookShelf() {
        // 已经去到了shelfList
        if (shelfList.size() == 1) {
            add2Shelf(shelfList.get(0).getId());
        } else {
            // 显示书架列表
            List<ListSelecterBean> dataList = new ArrayList<>();
            for (int i = 0; i < shelfList.size(); i++) {
                dataList.add(new ListSelecterBean(R.drawable.icon_book_collect, shelfList.get(i).getId(),
                        shelfList.get(i).getName(), ""));
            }
            bookShelifListDialog("请选择书架", dataList);
        }
    }

    // 书架列表
    private void bookShelifListDialog(String dTitle, List<ListSelecterBean> dataList) {
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
        tvDialogTitle.setText(dTitle);
        tvDialogSubTitle.setVisibility(View.GONE);

        DialogListSelecterAdapter selecterAdapter;
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        selecterAdapter = new DialogListSelecterAdapter(mContext, dataList);
        recyclerView.setAdapter(selecterAdapter);

        selecterAdapter.setOnItemClickListener(new DialogListSelecterAdapter.IMyViewHolderClicks() {
            @Override
            public void onItemClick(View view, ListSelecterBean item) {
                add2Shelf(item.getId());
                dlg.dismiss();
            }
        });
    }

    // 调用服务添加图书
    private void add2Shelf(String classid) {
        OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_ADD).tag(this)
                .params("classid", classid)
                .params("marc_no", marc_no)
                .params("time", System.currentTimeMillis())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        showAlertDialog(response.body());
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

    // 获取书架目录
    private void getBookShelf() {
        OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_SHELF).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                parseHtml2List(response.body());
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
    private void parseHtml2List(String s) {
        shelfList.clear();
        shelfList.addAll(HtmlParseUtils.getBookShelfList(s));

        if (shelfList.size() > 0) {
            add2BookShelf();
        } else {
            // 没有书架
            showToast("您需要先创建一个书架");
            startActivity(new Intent(mContext, BookShelfEditeActivity.class));
        }
    }

    // 解析网页
    private void parseHtml(String s) {
        // 解析图片
        String coverUrl = HtmlParseUtils.getBookCoverUrl(s);
        if (!TextUtils.isEmpty(coverUrl)) ImageLoader.with(mContext, ivBookCover, coverUrl);

        // 解析详情
        List<BookDetailsBean> detailList = new ArrayList<>();
        detailList = HtmlParseUtils.getBookDetailsList(s);
        // 解析藏书详情
        List<BookAccessBean> accessList = new ArrayList<>();
        accessList = HtmlParseUtils.getBookAccessList(s);

        // 显示
        if (detailList != null) {
            //初始化各fragment
            BookDetailQtyFragment fragmentInlib = new BookDetailQtyFragment();
            BookDetailInfoFragment fragmentInfo = new BookDetailInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("accessList", (Serializable) accessList);
            bundle.putSerializable("detailList", (Serializable) detailList);
            fragmentInlib.setArguments(bundle);
            fragmentInfo.setArguments(bundle);

            //将fragment装进列表中
            mFragments = new ArrayList<>();
            mFragments.add(fragmentInlib);
            mFragments.add(fragmentInfo);

            //将名称加载tab名字列表
            mTitles = new ArrayList<>();
            mTitles.add("在馆状态");
            mTitles.add("书目信息");

            //设置TabLayout的模式
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            //为TabLayout添加tab名称
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(0)));
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(1)));

            showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
            mViewPager.setAdapter(showTabAdapter);

            //TabLayout加载viewpager
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            showToast("解析失败");
        }
    }

    // 添加结果显示
    public void showAlertDialog(final String str) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage(Html.fromHtml(str) + "");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
