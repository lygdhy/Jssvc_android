package org.jssvc.lib.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookShelfBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.fragment.BookShelfFragment;
import org.jssvc.lib.utils.HtmlParseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的书架
 */
public class BookShelfActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.ivEdite)
    ImageView ivEdite;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.dateLayout)
    LinearLayout dateLayout;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<String> list_title;
    private List<Fragment> list_fragment;

    List<BookShelfBean> shelfList = new ArrayList<>();
    private ShowTabAdapter showTabAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_shelf;
    }

    @Override
    protected void initView() {
        ivEdite.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        dateLayout.setVisibility(View.GONE);
        getBookShelf();
    }

    @OnClick({R.id.tvBack, R.id.ivEdite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.ivEdite:
                if (shelfList.size() == 0) {
                    // 添加
                } else {
                    // 编辑
                }
                break;
        }
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
        shelfList.clear();
        shelfList.addAll(HtmlParseUtils.getBookShelfList(s));

        if (shelfList.size() > 0) {
            ivEdite.setVisibility(View.VISIBLE);
            ivEdite.setImageResource(R.drawable.icon_collect_edite);
            emptyLayout.setVisibility(View.GONE);
            dateLayout.setVisibility(View.VISIBLE);

            list_title = new ArrayList<>();
            list_fragment = new ArrayList<>();
            //设置TabLayout的模式
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            for (int i = 0; i < shelfList.size(); i++) {
                //将名称加载tab名字列表
                list_title.add(shelfList.get(i).getName() + "");

                //初始化各fragment
                BookShelfFragment shelfFragment = new BookShelfFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", shelfList.get(i).getUrl());
                shelfFragment.setArguments(bundle);

                //将fragment装进列表中
                list_fragment.add(shelfFragment);

                //为TabLayout添加tab名称
                tabLayout.addTab(tabLayout.newTab().setText(list_title.get(i)));
            }

            showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
            viewPager.setAdapter(showTabAdapter);

            //TabLayout加载viewpager
            tabLayout.setupWithViewPager(viewPager);
        } else {
            // 没有书架
            ivEdite.setVisibility(View.VISIBLE);
            ivEdite.setImageResource(R.drawable.icon_collect_off);
            emptyLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.GONE);
        }

    }
}
