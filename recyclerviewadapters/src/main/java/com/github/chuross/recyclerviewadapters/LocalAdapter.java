package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface LocalAdapter<VH extends RecyclerView.ViewHolder> {

    int getItemCount();

    int getAdapterType();

    VH onCreateViewHolder(@NonNull final ViewGroup parent, int adapterType);

    void onBindViewHolder(@NonNull final VH holder, final int position);

    CombinableRecyclerAdapter getParentAdapter();

    void bindParentAdapter(@Nullable CombinableRecyclerAdapter adapter);

    void unBindParentAdapter();

    boolean hasParentAdapter();
}