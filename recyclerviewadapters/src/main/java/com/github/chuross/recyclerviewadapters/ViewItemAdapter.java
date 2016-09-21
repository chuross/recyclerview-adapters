package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ViewItemAdapter extends ItemAdapter<ViewItem, RecyclerView.ViewHolder> {


    public ViewItemAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getAdapterId() {
        return 0;
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
