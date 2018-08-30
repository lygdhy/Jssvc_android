package org.jssvc.lib.ui.home.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.ui.home.adapter.MenuHubAdapter;
import org.jssvc.lib.ui.home.bean.MenuBean;
import org.jssvc.lib.ui.home.bean.MenuHubBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 实验室
 *     version: 1.0
 * </pre>
 */
public class LabFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    MenuHubAdapter mAdapter;

    public static LabFragment newInstance() {
        return new LabFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lab;
    }

    @Override
    protected void initView() {

        initRefreshView();

        getDiscussList();
    }

    private void initRefreshView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MenuHubAdapter(mRecycler);
        mRecycler.setAdapter(mAdapter);
    }

    private void getDiscussList() {
        List<MenuHubBean> hubList = new ArrayList<>();

        MenuHubBean hub0 = new MenuHubBean();
        hub0.setTitle("社会公益");
        List<MenuBean> mlist0 = new ArrayList<>();
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535616798&ver=1&signature=NH*NGcRPISR7WbiTU*qT-izKgP3dtpYs1v50U9z6Kc-DunZBaLMinoA5a5FBgB-FWvPQO4eUjR*LK3KSQR08EQ=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "团委", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=ZyoAhpuuh3AcuuaG2W-tD9qbU2*kZI-SUeIFeISuEL5-66WNlJvOc2esFSp5Gj9M*saUxgJ*cVgxJzGxO2C2Mw=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "平安苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535619062&ver=1&signature=fOSKPwh7VoJKvupSnFkK8ozLIXR8GGDvruzlPdue5V5C9fVhfhH1pHiUxz5jrPAzQTt936wqR6*79bA6UL-jxw=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "图书馆", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618786&ver=1&signature=vlEkS182HQllq8Z3Mqv0TUXBWyQdYkmOX27KRGBuJWc3kvd4ppVyhwl7I9RK*2xrsJPbewAO2qE140LH0M1zsA=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "计算机团学", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618158&ver=1&signature=O*9QG*chU8D7A5CCilYIzswrsbXnUh42cdhz76KhfC-qJqF4ocIGgfFmgYaPfUTvCPfviw3LseOvZjq0EWWeMQ=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "艺术学院", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618354&ver=1&signature=-z5cjCwaNC0MVWNs6fQviBaWDf0h99RzIBjOE6Tu2PlHh4866ZRHJS7hZ*OFIGXd32LW50A9yZw-mcegtdsP7g=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "国际交流", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618786&ver=1&signature=pgaEkKNBr62YTD6feq2hZFmwuIbsa9*gQQJYLZsBo2co4OZVoXm3lxGekq0IRopCiuVFpaD5M3KPY9OGLihRbw=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "微商院", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=tCLJOe2PEQHMNMPXaLvlpPVa0Hc7GR1R0dB9K2xdtGjvXaoUh9v-RJZdbzoo6XUMUXVrR4Zacu8AX*wfG7yn2A=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "苏职大资助", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=7KGZVGeNfGRGpbcU*UqZFKF9UDImWcf2fHxIRomvalm2*53UIeWqucS982wr*mO5p7udQItiqXZLY1P6PmkYqA=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "SVU苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=4lRjEHHKT9hkxTldTlAeVW3dEK0WRljLa4z1YzGZli3ztaCJfI7Fbm1sSjCEaVpS*TFx4ibSB1oU67LTSRbbhA=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "电子实训中心", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=iWzt6A90IucANLczsNI3u1BMyw8C*bTWs8vg3*5B9wbdB144XTbKf4coUR8-TM3*RNM7NVuVewVv5SHJry3nSQ=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "遇见苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618988&ver=1&signature=tkxvDmZKoIVJSTmNGs3VTj8P2xcD*DLVIRtF*t5yje-vVpabjvr*S3CCJPaFUO*BPNE2XYxAlxYnMRTdCBWQqw=="));
//        mlist0.add(new MenuBean(Constants.CODE_WEB, "外院WE圈", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618988&ver=1&signature=mFFjNRXU4Iy-dlO7rme62PQGNCBbJ23kQtpE9lyOth6wC7-hOYbY71ev8kMDYwED0QnkTdL8*xyaSl99Hg5qeA=="));
        mlist0.add(new MenuBean(Constants.CODE_WEB, "计算机青协", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=BQ2**FJxX065v3sJAfWAyIc-*nYbrBKOfJPTsq8ZE5jlAkir1*0og9-pDTjYo2*NPNQf79lZLdqqbTh3xIBvgw=="));
        mlist0.add(new MenuBean(Constants.CODE_WEB, "小红帽义工", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535619978&ver=1&signature=iDcLBN0IbDEYlBRwAJV2wavMdUbDhvH07GTCTqbscpPzjAOCGPPWfD3ba2MRk0SGjQeHuMav1gtxzFMvykdkFQ=="));
        hub0.setMenulist(mlist0);
        hubList.add(hub0);

        MenuHubBean hub2 = new MenuHubBean();
        hub2.setTitle("我的图书馆");
        List<MenuBean> mlist2 = new ArrayList<>();
        mlist2.add(new MenuBean(Constants.LIB_RESUME, "我的证件", R.drawable.icon_menu_idcard));
        mlist2.add(new MenuBean(Constants.LIB_READ_HIS, "借阅历史", R.drawable.icon_menu_book_his));
        mlist2.add(new MenuBean(Constants.LIB_READ_ING, "图书续借", R.drawable.icon_menu_borrowed));
        mlist2.add(new MenuBean(Constants.LIB_SEARCH_BOOK, "图书搜索", R.drawable.icon_menu_book_search));
        mlist2.add(new MenuBean(Constants.LIB_BOOK_SHELF, "我的书架", R.drawable.icon_menu_bookrack));
        mlist2.add(new MenuBean(Constants.LIB_HELP, "帮助指南", R.drawable.icon_menu_help));
        hub2.setMenulist(mlist2);
        hubList.add(hub2);

        MenuHubBean hub1 = new MenuHubBean();
        hub1.setTitle("我的校园");
        List<MenuBean> mlist1 = new ArrayList<>();
        mlist1.add(new MenuBean(Constants.MENU_NEWS, "新闻资讯", R.drawable.icon_menu_new));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535616798&ver=1&signature=NH*NGcRPISR7WbiTU*qT-izKgP3dtpYs1v50U9z6Kc-DunZBaLMinoA5a5FBgB-FWvPQO4eUjR*LK3KSQR08EQ=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "团委", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=ZyoAhpuuh3AcuuaG2W-tD9qbU2*kZI-SUeIFeISuEL5-66WNlJvOc2esFSp5Gj9M*saUxgJ*cVgxJzGxO2C2Mw=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "平安苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535619062&ver=1&signature=fOSKPwh7VoJKvupSnFkK8ozLIXR8GGDvruzlPdue5V5C9fVhfhH1pHiUxz5jrPAzQTt936wqR6*79bA6UL-jxw=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "遇见苏职大", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618988&ver=1&signature=tkxvDmZKoIVJSTmNGs3VTj8P2xcD*DLVIRtF*t5yje-vVpabjvr*S3CCJPaFUO*BPNE2XYxAlxYnMRTdCBWQqw=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "苏职大资助", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=7KGZVGeNfGRGpbcU*UqZFKF9UDImWcf2fHxIRomvalm2*53UIeWqucS982wr*mO5p7udQItiqXZLY1P6PmkYqA=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "计算机团学", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618158&ver=1&signature=O*9QG*chU8D7A5CCilYIzswrsbXnUh42cdhz76KhfC-qJqF4ocIGgfFmgYaPfUTvCPfviw3LseOvZjq0EWWeMQ=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "艺术学院", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618354&ver=1&signature=-z5cjCwaNC0MVWNs6fQviBaWDf0h99RzIBjOE6Tu2PlHh4866ZRHJS7hZ*OFIGXd32LW50A9yZw-mcegtdsP7g=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "国际交流", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618786&ver=1&signature=pgaEkKNBr62YTD6feq2hZFmwuIbsa9*gQQJYLZsBo2co4OZVoXm3lxGekq0IRopCiuVFpaD5M3KPY9OGLihRbw=="));
        mlist1.add(new MenuBean(Constants.CODE_WEB, "微商院", R.drawable.icon_menu_new, "https://mp.weixin.qq.com/profile?src=3&timestamp=1535618833&ver=1&signature=tCLJOe2PEQHMNMPXaLvlpPVa0Hc7GR1R0dB9K2xdtGjvXaoUh9v-RJZdbzoo6XUMUXVrR4Zacu8AX*wfG7yn2A=="));
        hub1.setMenulist(mlist1);
        hubList.add(hub1);

//        MenuHubBean hub3 = new MenuHubBean();
//        hub3.setTitle("关于未来");
//        List<MenuBean> mlist3 = new ArrayList<>();
//        mlist3.add(new MenuBean(Constants.LIB_BOOK_REVIEW, "我的书评", R.drawable.icon_menu_appraise));
//        mlist3.add(new MenuBean(Constants.LIB_BOOK_RECOMMEND, "图书荐购", R.drawable.icon_menu_book_recommend));
//        hub3.setMenulist(mlist3);
//        hubList.add(hub3);

        MenuHubBean hub4 = new MenuHubBean();
        hub4.setTitle("联系我们");
        List<MenuBean> mlist4 = new ArrayList<>();
        mlist4.add(new MenuBean(Constants.LIB_ABOUT, "@图书馆", R.drawable.icon_menu_about_lib));
        mlist4.add(new MenuBean(Constants.MENU_ABOUT, "关于我们", R.drawable.icon_menu_about_us));
        mlist4.add(new MenuBean(Constants.MENU_FEEDBACK, "意见反馈", R.drawable.icon_menu_feedback));
        mlist4.add(new MenuBean(Constants.MENU_WAITER, "客服小智", R.drawable.icon_menu_waiter));
        hub4.setMenulist(mlist4);
        hubList.add(hub4);

        mAdapter.setData(hubList);
    }
}
