package com.jerrellmardis.materialanimations;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jerrellmardis.materialanimations.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_BG_COLOR = "bgColor";
    public static final String EXTRA_ID = "pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // bind the id and background color to the View
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setId(getIntent().getIntExtra(EXTRA_ID, 0));
        binding.item.setBackgroundColor(getIntent().getIntExtra(EXTRA_BG_COLOR, 0));

        // postpone the transition until the view is ready to be drawn
        postponeEnterTransition();

        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }
}
