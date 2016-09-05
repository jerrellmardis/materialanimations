package com.jerrellmardis.materialanimations;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jerrellmardis.materialanimations.databinding.GridItemBinding;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final PresenterImpl presenter;
    private final LayoutInflater layoutInflater;

    public ItemAdapter(@NonNull Context context, PresenterImpl presenter) {
        this.presenter = presenter;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new ItemViewHolder((GridItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.binding.setPresenter(presenter);
        holder.binding.setId(position);
        holder.binding.setBackgroundColor(getBackgroundColor(holder.binding.getRoot().getContext(), position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return 22;
    }

    private int getBackgroundColor(Context context, int position) {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("colors", "array", context.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            returnColor = colors.getColor(position, Color.BLACK);
            colors.recycle();
        }

        return returnColor;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        final GridItemBinding binding;

        public ItemViewHolder(GridItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

}