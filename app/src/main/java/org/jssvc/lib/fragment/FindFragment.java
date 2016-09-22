package org.jssvc.lib.fragment;


import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

/**
 * 探索&发现
 */
public class FindFragment extends BaseFragment {

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {

    }

}
