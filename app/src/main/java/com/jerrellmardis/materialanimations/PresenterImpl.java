package com.jerrellmardis.materialanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jerrellmardis.materialanimations.databinding.ActivityMainBinding;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PresenterImpl implements Presenter {

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int id, int backgroundColor);
    }

    private final OnItemSelectedListener listener;
    private boolean isRevealContainerShowing;
    private int intialFabX;
    private int intialFabY;

    public PresenterImpl(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemSelected(View view, int id, int backgroundColor) {
        if (listener != null) listener.onItemSelected(view, id, backgroundColor);
    }

    @Override
    public void onFabClicked(View view) {
        final TextView revealText = (TextView) ((ViewGroup) view.getParent()).findViewById(R.id.reveal_text);
        revealText.setAlpha(1f);
        revealText.setScaleX(0f);
        revealText.setScaleY(0f);

        intialFabX = (view.getLeft() + view.getRight()) / 2;
        intialFabY = (view.getTop() + view.getBottom()) / 2;

        final FloatingActionButton fab = (FloatingActionButton) view;
        final ViewGroup.LayoutParams originalParams = fab.getLayoutParams();
        Transition transition = TransitionInflater.from(fab.getContext()).inflateTransition(R.transition.fab_transition);
        transition.addListener(new TransitionCallback() {
            @Override
            public void onTransitionEnd(Transition transition) {
                isRevealContainerShowing = true;
                final ViewGroup revealContainer = (ViewGroup) ((ViewGroup) fab.getParent()).findViewById(R.id.reveal_container);
                animateRevealColor(revealContainer, R.color.colorAccent);
                fab.setLayoutParams(originalParams);
                fab.setVisibility(View.GONE);
            }

            private void animateRevealColor(ViewGroup viewRoot, @ColorRes int color) {
                int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
                int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
                float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

                viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), color));

                Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
                anim.setDuration(viewRoot.getResources().getInteger(android.R.integer.config_longAnimTime));
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        revealText.animate().scaleX(1).scaleY(1).setInterpolator(new BounceInterpolator()).setDuration(500).start();
                    }
                });
                anim.start();
            }
        });
        TransitionManager.beginDelayedTransition((ViewGroup) fab.getParent(), transition);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
        fab.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onBackPressed(final ActivityMainBinding binding) {
        if (isRevealContainerShowing) {
            isRevealContainerShowing = false;
            float finalRadius = (float) Math.hypot(binding.revealContainer.getWidth(), binding.revealContainer.getHeight());

            binding.revealText.animate().alpha(0).scaleX(0).scaleY(0).start();

            Animator anim = ViewAnimationUtils.createCircularReveal(binding.revealContainer, intialFabX, intialFabY, finalRadius, 0);
            anim.setDuration(binding.revealContainer.getResources().getInteger(android.R.integer.config_longAnimTime));
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.revealContainer.setBackground(null);
                    binding.fab.show();
                }
            });
            anim.start();

            return true;
        }
        return false;
    }
}