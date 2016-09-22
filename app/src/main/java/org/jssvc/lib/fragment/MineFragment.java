package org.jssvc.lib.fragment;


import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

}
