package org.jssvc.lib.ui.home.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.ui.home.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.ui.article.bean.ChannelBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.ui.article.fragment.ArticleListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 资讯信息
 *     version: 1.0
 * </pre>
 */
public class ReadingHubFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static ReadingHubFragment newInstance() {
        return new ReadingHubFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_reading_hub;
    }

    @Override
    protected void initView() {
        getChannelList();
    }

    // 获取频道类型
    private void getChannelList() {
        OkGo.<String>get(HttpUrlParams.GET_CHANNEL_LIST).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                List<ChannelBean> channelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optInt("code") == 200) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChannelBean item =
                                    new Gson().fromJson(jsonArray.getJSONObject(i).toString(), ChannelBean.class);
                            channelList.add(item);
                        }
                        loadData2UI(channelList);
                    } else {
                        showToast(jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 加载页面
    private void loadData2UI(List<ChannelBean> channelList) {
        if (channelList == null || channelList.size() == 0) {
            return;
        }
        List<String> mTitles = new ArrayList<>();
        List<Fragment> mFragments = new ArrayList<>();

        for (int i = 0; i < channelList.size(); i++) {
            mTitles.add(channelList.get(i).getPlatform());
            tabLayout.addTab(tabLayout.newTab().setText(channelList.get(i).getPlatform()));

            ArticleListFragment fragment = new ArticleListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("channel_id", channelList.get(i).getId());// category id
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        ShowTabAdapter showTabAdapter =
                new ShowTabAdapter(getChildFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(showTabAdapter);
        viewPager.setOffscreenPageLimit(mTitles.size());
        tabLayout.setupWithViewPager(viewPager);
    }
}
