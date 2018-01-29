package org.jssvc.lib.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.jssvc.lib.R;

/**
 * Created by jjj on 2018/1/29.
 *
 * @description:
 */

public class TitleView extends ConstraintLayout {

    private ConstraintLayout parentView;
    private TextView leftTextView;
    private TextView titleView;

    public TitleView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.base_title_view, this);
        parentView = findViewById(R.id.cl_title_view);
        leftTextView = findViewById(R.id.tv_back);
        titleView = findViewById(R.id.tv_title);
    }

    public ConstraintLayout getParentView() {
        return parentView;
    }

    public void setTitleView(String title) {
        titleView.setText(title);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }
}
