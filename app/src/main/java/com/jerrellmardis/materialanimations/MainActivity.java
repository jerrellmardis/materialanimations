package com.jerrellmardis.materialanimations;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jerrellmardis.materialanimations.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PresenterImpl.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private PresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PresenterImpl(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(presenter);

        setActionBar(binding.toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.grid);
        recyclerView.setAdapter(new ItemAdapter(this, presenter));
        recyclerView.addItemDecoration(new GridMarginDecoration(getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onItemSelected(View view, int id, int backgroundColor) {
        List<Pair<View, String>> pairs = buildTransitionPairs(view);

        Intent intent = new Intent(this, DetailActivity.class);
        // pass the background color to set on the shared element view in the {@code DetailActivity}
        intent.putExtra(DetailActivity.EXTRA_BG_COLOR, backgroundColor);
        // pass the id (position of the view in the ItemAdapter) of the view to use to build the unique transitionName in the {@code DetailActivity}
        intent.putExtra(DetailActivity.EXTRA_ID, id);

        //noinspection unchecked
        ActivityCompat.startActivity(this, intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs.toArray(new Pair[pairs.size()])).toBundle());
    }

    @Override
    public void onBackPressed() {
        if (!presenter.onBackPressed(binding)) {
            super.onBackPressed();
        }
    }

    @NonNull
    private List<Pair<View, String>> buildTransitionPairs(View view) {
        List<Pair<View, String>> pairs = new ArrayList<>();

        View decor = getWindow().getDecorView();

        View statusBar = decor.findViewById(android.R.id.statusBarBackground);
        pairs.add(Pair.create(statusBar, statusBar.getTransitionName()));

        View navBar = decor.findViewById(android.R.id.navigationBarBackground);
        if (navBar != null) {
            pairs.add(Pair.create(navBar, navBar.getTransitionName()));
        }

        pairs.add(Pair.create((View) binding.toolbar, binding.toolbar.getTransitionName()));
        pairs.add(Pair.create(view, view.getTransitionName()));
        return pairs;
    }
}
