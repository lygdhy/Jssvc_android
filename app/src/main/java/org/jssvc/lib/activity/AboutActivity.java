package org.jssvc.lib.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.AdsBean;
import org.jssvc.lib.fragment.TabAFragment;
import org.jssvc.lib.fragment.TabBFragment;
import org.jssvc.lib.fragment.TabCFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private TabAFragment tabAFragment;
    private TabBFragment tabBFragment;
    private TabCFragment tabCFragment;

    private List<String> list_title;
    private List<Fragment> list_fragment;

    private ShowTabAdapter showTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        //初始化各fragment
        tabAFragment = new TabAFragment();
        tabBFragment = new TabBFragment();
        tabCFragment = new TabCFragment();

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(tabAFragment);
        list_fragment.add(tabBFragment);
        list_fragment.add(tabCFragment);

        //将名称加载tab名字列表
        list_title = new ArrayList<>();
        list_title.add("产品详情");
        list_title.add("费用说明");
        list_title.add("预定须知");

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));

        showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(showTabAdapter);

        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);

        // =====================================================================================================================
        List<AdsBean> adsList = new ArrayList<>();
        adsList.add(new AdsBean("1", "1", "demo1", "http://e.51toc.net/themes/upload/item_images/518_20161013010023_62.png", ""));
        adsList.add(new AdsBean("2", "1", "demo2", "http://e.51toc.net/themes/upload/item_images/558_20161028103126_52.jpg", ""));

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

    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
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
