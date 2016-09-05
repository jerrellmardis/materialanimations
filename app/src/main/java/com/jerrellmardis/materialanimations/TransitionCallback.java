package com.jerrellmardis.materialanimations;

import android.transition.Transition;

public abstract class TransitionCallback implements Transition.TransitionListener {

    @Override
    public void onTransitionStart(Transition transition) {
        // no-op
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        // no-op
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        // no-op
    }

    @Override
    public void onTransitionPause(Transition transition) {
        // no-op
    }

    @Override
    public void onTransitionResume(Transition transition) {
        // no-op
    }
}
