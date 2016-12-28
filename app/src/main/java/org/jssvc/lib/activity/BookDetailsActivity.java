package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookAccessBean;
import org.jssvc.lib.bean.BookDetailsBean;
import org.jssvc.lib.bean.BookShelfBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.fragment.BookDetailInfoFragment;
import org.jssvc.lib.fragment.BookDetailInlibFragment;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 图书详情
 */
public class BookDetailsActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvCollect)
    ImageView tvCollect;
    @BindView(R.id.bookView)
    SimpleDraweeView bookView;
    @BindView(R.id.tvBookName)
    TextView tvBookName;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    private List<String> list_title;
    private List<Fragment> list_fragment;
    private ShowTabAdapter showTabAdapter;

    String detialUrl = "";
    List<BookShelfBean> shelfList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_details;
    }

    @Override
    protected void initView() {

        tvBookName.setText(getIntent().getStringExtra("title") + "");
        detialUrl = getIntent().getStringExtra("url");

        showProgressDialog();
        OkGo.post(detialUrl)
                .tag(this)
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

    @OnClick({R.id.tvBack, R.id.tvCollect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvCollect:
                // 添加到书架
                collectBook();
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
        showToast("弹框选择书架并添加");
    }

    // 获取书架目录
    private void getBookShelf() {
        showProgressDialog();
        OkGo.post(HttpUrlParams.URL_LIB_BOOK_SHELF)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        // s 即为所需要的结果
                        parseHtml2List(s);
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
    private void parseHtml2List(String s) {
        shelfList.clear();
        shelfList.addAll(HtmlParseUtils.getBookShelfList(s));

        if (shelfList.size() > 0) {
            add2BookShelf();
        } else {
            // 没有书架
            showToast("您需要先创建一个书架");
            startActivity(new Intent(context, BookShelfEditeActivity.class));
        }
    }

    // 解析网页
    private void parseHtml(String s) {
        // 解析图片
        String coverUrl = HtmlParseUtils.getBookCoverUrl(s);
        bookView.setImageURI(coverUrl);

        // 解析详情
        List<BookDetailsBean> detailList = new ArrayList<>();
        detailList = HtmlParseUtils.getBookDetailsList(s);
        // 解析藏书详情
        List<BookAccessBean> accessList = new ArrayList<>();
        accessList = HtmlParseUtils.getBookAccessList(s);

        // 显示
        if (detailList != null) {
            //初始化各fragment
            BookDetailInlibFragment fragmentInlib = new BookDetailInlibFragment();
            BookDetailInfoFragment fragmentInfo = new BookDetailInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("accessList", (Serializable) accessList);
            bundle.putSerializable("detailList", (Serializable) detailList);
            fragmentInlib.setArguments(bundle);
            fragmentInfo.setArguments(bundle);

            //将fragment装进列表中
            list_fragment = new ArrayList<>();
            list_fragment.add(fragmentInlib);
            list_fragment.add(fragmentInfo);

            //将名称加载tab名字列表
            list_title = new ArrayList<>();
            list_title.add("在馆状态");
            list_title.add("书目信息");

            //设置TabLayout的模式
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            //为TabLayout添加tab名称
            tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
            tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));

            showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
            viewPager.setAdapter(showTabAdapter);

            //TabLayout加载viewpager
            tabLayout.setupWithViewPager(viewPager);

        } else {
            showToast("解析失败");
        }
    }

}
