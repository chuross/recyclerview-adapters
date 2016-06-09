package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ViewItem extends BaseLocalAdapter<RecyclerView.ViewHolder> {

    private int layoutResourceId;

    public ViewItem(@NonNull ViewItem item) {
        this(item.getContext(), item.getAdapterType());
    }

    public ViewItem(@NonNull Context context, int layoutResourceId) {
        super(context);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getAdapterType() {
        return layoutResourceId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return new RecyclerView.ViewHolder(inflater.inflate(layoutResourceId, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
