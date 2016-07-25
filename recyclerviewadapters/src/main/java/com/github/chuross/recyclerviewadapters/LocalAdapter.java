package com.github.chuross.recyclerviewadapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface LocalAdapter<VH extends RecyclerView.ViewHolder> {

    int getItemCount();

    /**
     * static identity for CompositeRecyclerAdapter#onCreateViewHolder
     * ex) R.layout, R.id
     */
    int getAdapterId();

    int getItemViewType(int position);

    boolean hasStableItemViewType();

    void onAttachedToRecyclerView(RecyclerView recyclerView);

    VH onCreateViewHolder(final ViewGroup parent, int adapterId);

    void onViewRecycled(VH holder);

    boolean onFailedToRecycleView(VH holder);

    void onViewAttachedToWindow(VH holder);

    void onBindViewHolder(final VH holder, final int position);

    void onViewDetachedFromWindow(VH holder);

    void onDetachedFromRecyclerView(RecyclerView recyclerView);

    CompositeRecyclerAdapter getParentAdapter();

    void bindParentAdapter(@Nullable CompositeRecyclerAdapter adapter, @Nullable RecyclerView.AdapterDataObserver dataObserver);

    void unBindParentAdapter();

    boolean hasParentAdapter();
}