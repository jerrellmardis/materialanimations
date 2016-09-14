package com.jerrellmardis.materialanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.jerrellmardis.materialanimations.databinding.ActivityMainBinding;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PresenterImpl implements Presenter {

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int id, int backgroundColor);
    }

    private final OnItemSelectedListener listener;
    private int initialFabX;
    private int initialFabY;

    public PresenterImpl(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemSelected(View view, int id, int backgroundColor) {
        if (listener != null) listener.onItemSelected(view, id, backgroundColor);
    }

    @Override
    public void onFabClicked(View view) {
        ViewGroup rootView = (ViewGroup) view.getRootView();
        View revealContainer = rootView.findViewById(R.id.reveal_container);
        View revealTextView = rootView.findViewById(R.id.reveal_text);

        // set the initial state of the revealTextView
        revealTextView.setAlpha(1f);
        revealTextView.setScaleX(0f);
        revealTextView.setScaleY(0f);

        // find the center point of the FAB
        initialFabX = (view.getLeft() + view.getRight()) / 2;
        initialFabY = (view.getTop() + view.getBottom()) / 2;

        FloatingActionButton fab = (FloatingActionButton) view;

        // load the fab transition, add the FabTransitionCallback and start the transition
        Transition transition = TransitionInflater.from(view.getContext()).inflateTransition(R.transition.fab_transition);
        transition.addListener(new FabTransitionCallback(fab, revealContainer, revealTextView));

        // 1. saves the current property values of all views in the rootView
        // 2. adds an OnPreDrawListener which is triggered on the next layout pass
        TransitionManager.beginDelayedTransition(rootView, transition);

        // 3. triggers the OnPreDrawListener created in TransitionManager.beginDelayedTransition
        // 4. OnPreDrawListener retrieves the new property values
        // 5. the system calculates the before/after property values and runs the animations
        fab.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
    }

    @Override
    public boolean onBackPressed(final ActivityMainBinding binding) {
        if (View.GONE == binding.fab.getVisibility()) {
            float finalRadius = (float) Math.hypot(binding.revealContainer.getWidth(), binding.revealContainer.getHeight());

            binding.revealText.animate().alpha(0).scaleX(0).scaleY(0).start();

            Animator anim = ViewAnimationUtils.createCircularReveal(binding.revealContainer, initialFabX, initialFabY, finalRadius, 0);
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