package com.jerrellmardis.materialanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class FabTransitionCallback extends TransitionCallback {

    private final FloatingActionButton fab;
    private final ViewGroup.LayoutParams originalParams;
    private final View revealContainer;
    private final View revealTextView;

    public FabTransitionCallback(FloatingActionButton fab, View revealContainer, View revealTextView) {
        this.fab = fab;
        this.revealContainer = revealContainer;
        this.revealTextView = revealTextView;
        originalParams = fab.getLayoutParams();
    }

    // ../FabTransitionCallback
    @Override
    public void onTransitionEnd(Transition transition) {
        animateRevealContainer(R.color.colorAccent);

        // reset the FAB's position and hide it
        fab.setLayoutParams(originalParams);
        fab.setVisibility(View.GONE);
    }

    private void animateRevealContainer(int color) {
        // find the center of the reveal container
        int cx = (revealContainer.getLeft() + revealContainer.getRight()) / 2;
        int cy = (revealContainer.getTop() + revealContainer.getBottom()) / 2;

        // determine the radius of the reveal
        float radius = (float) Math.hypot(revealContainer.getWidth(), revealContainer.getHeight());

        revealContainer.setBackgroundColor(ContextCompat.getColor(revealContainer.getContext(), color));

        // create and start the circular reveal animation
        Animator anim = ViewAnimationUtils.createCircularReveal(revealContainer, cx, cy, 0, radius);
        anim.setDuration(revealContainer.getResources().getInteger(android.R.integer.config_longAnimTime));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // show the revealTextView
                revealTextView
                        .animate().scaleX(1).scaleY(1).setInterpolator(new BounceInterpolator()).setDuration(500).start();
            }
        });
        anim.start();
    }
}
