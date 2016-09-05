package com.jerrellmardis.materialanimations;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.jerrellmardis.materialanimations.databinding.ActivityMainBinding;

public interface Presenter {

    /**
     * Handles the item selected action.
     *
     * @param view the item selected.
     * @param id the id of the selected item to be used to construct the unique transition name
     * @param backgroundColor the background color of the selected item
     */
    void onItemSelected(View view, int id, int backgroundColor);

    /**
     * Handles the FAB clicked action.
     *
     * @param view the {@link FloatingActionButton}
     */
    void onFabClicked(View view);

    /**
     * Handles the back press action.
     *
     * @param binding the {@link ActivityMainBinding}
     * @return true, if the back press was handled. False, otherwise.
     */
    boolean onBackPressed(ActivityMainBinding binding);
}
