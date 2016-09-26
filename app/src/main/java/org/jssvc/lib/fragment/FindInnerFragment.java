package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by lygdh on 2016/9/26.
 */

public class FindInnerFragment extends BaseFragment {
    @BindView(R.id.textView)
    TextView textView;

    public static final String ARGS_CHANNEL = "args_channel_id";
    private int channelId;

    public static FindInnerFragment newInstance(int id) {
        FindInnerFragment fragment = new FindInnerFragment();
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
        return R.layout.fragment_find_inner;
    }

    @Override
    protected void initView() {
        textView.setText("id = " + channelId + "");
    }
}
