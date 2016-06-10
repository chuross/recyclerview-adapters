package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface LocalAdapter<VH extends RecyclerView.ViewHolder> {

    int getItemCount();

    /**
     * static identity
     * ex) R.layout, R.id
     */
    int getAdapterType();

    VH onCreateViewHolder(@NonNull final ViewGroup parent, int adapterType);

    void onBindViewHolder(@NonNull final VH holder, final int position);

    CompositeRecyclerAdapter getParentAdapter();

    void bindParentAdapter(@Nullable CompositeRecyclerAdapter adapter, @Nullable RecyclerView.AdapterDataObserver dataObserver);

    void unBindParentAdapter();

    boolean hasParentAdapter();
}