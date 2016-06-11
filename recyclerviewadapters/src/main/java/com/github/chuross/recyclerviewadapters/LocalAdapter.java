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
    int getAdapterId();

    int getItemViewType(int position);

    boolean hasStableItemViewType();

    void onAttachedToRecyclerView(RecyclerView recyclerView);

    VH onCreateViewHolder(@NonNull final ViewGroup parent, int adapterId);

    void onViewRecycled(RecyclerView.ViewHolder holder);

    boolean onFailedToRecycleView(RecyclerView.ViewHolder holder);

    void onViewAttachedToWindow(RecyclerView.ViewHolder holder);

    void onBindViewHolder(@NonNull final VH holder, final int position);

    void onViewDetachedFromWindow(RecyclerView.ViewHolder holder);

    void onDetachedFromRecyclerView(RecyclerView recyclerView);

    CompositeRecyclerAdapter getParentAdapter();

    void bindParentAdapter(@Nullable CompositeRecyclerAdapter adapter, @Nullable RecyclerView.AdapterDataObserver dataObserver);

    void unBindParentAdapter();

    boolean hasParentAdapter();
}