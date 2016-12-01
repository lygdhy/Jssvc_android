package org.jssvc.lib.view.pDialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.jssvc.lib.R;

/**
 * Created by lygdh on 2016/11/30.
 */

public class SpotsProgressView extends FrameLayout {

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    private int size = 5;
    private AnimatedView[] spots;
    private AnimatorPlayer animator;
    private int[] sportDrawableArr = new int[]{R.drawable.spot_yellow, R.drawable.spot_blue, R.drawable.spot_grey, R.drawable.spot_red, R.drawable.spot_green};

    public SpotsProgressView(Context context) {
        super(context);
        init();
    }

    public SpotsProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpotsProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        spots = new AnimatedView[size];
        int dotSize = getContext().getResources().getDimensionPixelSize(R.dimen.spot_progress_spot_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.spot_progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(getContext());
            v.setBackgroundResource(sportDrawableArr[i]);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            addView(v, dotSize, dotSize);
            spots[i] = v;
        }
        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }

    public void onStop() {
        animator.stop();
    }
}
