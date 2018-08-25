package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewItemAdapter extends ItemAdapter<ViewItem, RecyclerView.ViewHolder> {


    public ViewItemAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getAdapterId() {
        return 0;
    }

    @Override
    public boolean hasStableItemViewType() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return get(position).getAdapterId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        for (ViewItem viewItem : getAll()) {
            if (viewItem.getAdapterId() == viewType) {
                return viewItem.onCreateViewHolder(parent, viewType);
            }
        }
        throw new IllegalStateException("ViewItem not found.");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        get(position).onBindViewHolder(holder, position);
    }
}
