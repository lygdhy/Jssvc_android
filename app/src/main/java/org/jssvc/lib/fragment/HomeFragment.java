package org.jssvc.lib.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.SearchBookListActivity;
import org.jssvc.lib.activity.WebActivity;
import org.jssvc.lib.adapter.HomeMenuAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    HomeMenuAdapter homeMenuAdapter;
    @BindView(R.id.tvSearchType)
    TextView tvSearchType;
    @BindView(R.id.edtSearchText)
    EditText edtSearchText;
    @BindView(R.id.tvSearchCommit)
    TextView tvSearchCommit;
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;

    private List<String> mDatas;
    int searchTypePos = 0;
    String[] searchTypes = new String[]{"书名", "作者", "索书号", "主题词"};

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        // 广告和菜单等分屏幕
        frameLayout.getLayoutParams().height = (ScreenUtils.getScreenHeight(context) / 2);

        mDatas = new ArrayList<String>();
        mDatas.add("开心");
        mDatas.add("快乐");
        mDatas.add("温暖");
        mDatas.add("幸福");

        //创建默认的线性LayoutManager
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        homeMenuAdapter = new HomeMenuAdapter(context, mDatas);
        mRecyclerView.setAdapter(homeMenuAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<AdsBean> adsList = new ArrayList<>();
        adsList.add(new AdsBean("1", "1", "欢度中秋", HttpUrlParams.URL_AD + "bg_addemo.png", "http://baike.baidu.com/view/2568.htm"));
        adsList.add(new AdsBean("2", "1", "图书馆", HttpUrlParams.URL_AD + "bg_addemo2.png", "http://baike.baidu.com/view/5476774.htm"));

        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, adsList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
        // convenientBanner.setManualPageable(false);//设置不能手动影响

        tvSearchCommit.setVisibility(View.GONE);
        edtSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    tvSearchCommit.setVisibility(View.GONE);
                } else {
                    tvSearchCommit.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tvSearchType, R.id.tvSearchCommit, R.id.searchLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSearchType:
                // 选择搜书类型
                showSearchTypeDialog();
                break;
            case R.id.tvSearchCommit:
                // 搜索按鈕
                String value = edtSearchText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    Intent intent = new Intent(context, SearchBookListActivity.class);
                    intent.putExtra("searchType", tvSearchType.getText().toString().trim());
                    intent.putExtra("searchValue", value);
                    startActivity(intent);
                }
                break;
            case R.id.searchLayout:
                // 搜索跟布局，不错任何操作，仅阻止点击事件传递到广告页面上
                break;
        }
    }

    // 选择搜索类型
    private void showSearchTypeDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("请选择")
                .setSingleChoiceItems(searchTypes, searchTypePos,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                searchTypePos = which;
                                tvSearchType.setText(searchTypes[searchTypePos]);
                                edtSearchText.setHint("请输入" + searchTypes[searchTypePos]);
                                dialog.dismiss();
                            }
                        }
                ).show();
    }

    public class LocalImageHolderView implements Holder<AdsBean> {
        private SimpleDraweeView simpleDraweeView;

        @Override
        public View createView(Context context) {
            simpleDraweeView = new SimpleDraweeView(context);
            simpleDraweeView.setScaleType(SimpleDraweeView.ScaleType.CENTER_CROP);
            return simpleDraweeView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final AdsBean adsBean) {
            simpleDraweeView.setImageURI(adsBean.getPic());
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", adsBean.getUrl());
                    intent.putExtra("title", adsBean.getTitle());
                    startActivity(intent);
                }
            });
        }
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }
}
