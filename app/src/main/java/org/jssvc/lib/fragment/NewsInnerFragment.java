package org.jssvc.lib.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.WebActivity;
import org.jssvc.lib.adapter.ArticleListAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ArticleBean;
import org.jssvc.lib.data.HttpUrlParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by lygdh on 2016/9/26.
 */

public class NewsInnerFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.mListView)
    ListView mDataLv;

    private ArticleListAdapter mAdapter;

    public static final String ARGS_CHANNEL = "args_channel_id";
    private int channelId;

    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    List<ArticleBean> articleBeanList = new ArrayList<>();

    Handler handler = new Handler();//模拟数据异步请求

    public static NewsInnerFragment newInstance(int id) {
        NewsInnerFragment fragment = new NewsInnerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CHANNEL, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelId = getArguments().getInt(ARGS_CHANNEL);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_expand_inner;
    }

    @Override
    protected void initView() {
        mRefreshLayout.setDelegate(this);

        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);

        mAdapter = new ArticleListAdapter(getActivity());
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);


        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.colorAccent);
        moocStyleRefreshViewHolder.setOriginalImage(R.drawable.ic_launcher_dark);
//        moocStyleRefreshViewHolder.setLoadMoreBackgroundColorRes(R.color.custom_imoocstyle);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
//        moocStyleRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.custom_imoocstyle);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
//        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(getActivity()), true);

        mDataLv.setAdapter(mAdapter);
    }

    @Override
    protected void onFirstUserVisible() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;

        handler.postDelayed(new setDataHandler(), 1000);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了");
            return;
        }
        handler.postDelayed(new setNewDataHandler(), 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 4) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }
        handler.postDelayed(new setMoreDataHandler(), 1000);

//        showLoadingDialog();
//        mEngine.loadMoreData(mMorePageNumber).enqueue(new Callback<List<ArticleBean>>() {
//            @Override
//            public void onResponse(Call<List<ArticleBean>> call, final Response<List<ArticleBean>> response) {
//                // 测试数据放在七牛云上的比较快，这里加载完数据后模拟延时查看动画效果
//                ThreadUtil.runInUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshLayout.endLoadingMore();
//                        dismissLoadingDialog();
//                        mAdapter.addMoreData(response.body());
//                    }
//                }, MainActivity.LOADING_DURATION);
//            }
//
//            @Override
//            public void onFailure(Call<List<ArticleBean>> call, Throwable t) {
//                mRefreshLayout.endLoadingMore();
//            }
//        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        showToast("点击了条目 " + mAdapter.getItem(position).getId());
        Intent intentReadBack = new Intent(context, WebActivity.class);
        intentReadBack.putExtra("url", HttpUrlParams.URL_ARTICLE + "new_2.html");
        intentReadBack.putExtra("title", "校园新闻");
        startActivity(intentReadBack);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showToast("长按了" + mAdapter.getItem(position).getId());
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).getId());
            return true;
        }
        return false;
    }

    // =============
    class setDataHandler implements Runnable {
        public void run() {
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/201611795214266.jpg", "我校师生喜获2016年全国职业院校物联网创新创意大赛一等奖", "这里是内容", "电子信息工程学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016114162125730.jpg", "《江苏省丝绸织绣产品功能检测试验基地建设》项目汇报暨校内验收会在教育与人文学院举行", "这里是内容", "教育与人文学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016113144040425.jpg", "阿里巴巴集团“百城千校、百万英才”跨境电商人才培养项目落户我校商学院", "这里是内容", "商学院", "2016-11-09"));

            mRefreshLayout.endRefreshing();
            mAdapter.setData(articleBeanList);
        }
    }

    class setNewDataHandler implements Runnable {
        public void run() {
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/201611795214266.jpg", "我校师生喜获2016年全国职业院校物联网创新创意大赛一等奖", "这里是内容", "电子信息工程学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016114162125730.jpg", "《江苏省丝绸织绣产品功能检测试验基地建设》项目汇报暨校内验收会在教育与人文学院举行", "这里是内容", "教育与人文学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016113144040425.jpg", "阿里巴巴集团“百城千校、百万英才”跨境电商人才培养项目落户我校商学院", "这里是内容", "商学院", "2016-11-09"));

            mRefreshLayout.endRefreshing();
            mAdapter.addNewData(articleBeanList);
        }
    }

    class setMoreDataHandler implements Runnable {
        public void run() {
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/201611795214266.jpg", "我校师生喜获2016年全国职业院校物联网创新创意大赛一等奖", "这里是内容", "电子信息工程学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016114162125730.jpg", "《江苏省丝绸织绣产品功能检测试验基地建设》项目汇报暨校内验收会在教育与人文学院举行", "这里是内容", "教育与人文学院", "2016-11-09"));
            articleBeanList.add(new ArticleBean("1", "1", "http://www.jssvc.edu.cn/UploadFiles/2016113144040425.jpg", "阿里巴巴集团“百城千校、百万英才”跨境电商人才培养项目落户我校商学院", "这里是内容", "商学院", "2016-11-09"));

            mRefreshLayout.endLoadingMore();
            mAdapter.addMoreData(articleBeanList);
        }
    }
}
