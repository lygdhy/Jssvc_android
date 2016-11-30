package org.jssvc.lib.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookAccessBean;
import org.jssvc.lib.bean.BookDetailsBean;
import org.jssvc.lib.fragment.BookDetailInfoFragment;
import org.jssvc.lib.fragment.BookDetailInlibFragment;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomViewPager;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 图书详情
 */
public class BookDetailsActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvBarNo)
    TextView tvBarNo;
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_details;
    }

    @Override
    protected void initView() {
        tvBookName.setText(getIntent().getStringExtra("title") + "");
        detialUrl = getIntent().getStringExtra("url");
        OkGo.post(detialUrl)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // s 即为所需要的结果
                        parseHtml(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("onError -> HttpUrlParams.BASE_LIB_URL");
                    }
                });
    }

    @OnClick({R.id.tvBack, R.id.tvBarNo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvBarNo:
                // 显示条码
                String[] strs = detialUrl.split("=");
                if (strs.length == 2) {
                    showToast(strs[1] + "");
                }
                break;
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
