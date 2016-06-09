package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

public abstract class BaseLocalAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements LocalAdapter<VH> {

    private Context context;
    private WeakReference<CompositeRecyclerAdapter> parentAdapter;

    public BaseLocalAdapter(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public CompositeRecyclerAdapter getParentAdapter() {
        return hasParentAdapter() ? parentAdapter.get() : null;
    }

    @Override
    public void bindParentAdapter(@Nullable CompositeRecyclerAdapter adapter) {
        if (hasParentAdapter()) {
            throw new IllegalStateException("Adapter already has parentAdapter.");
        }
        parentAdapter = new WeakReference<>(adapter);
    }

    @Override
    public void unBindParentAdapter() {
        parentAdapter.clear();
        parentAdapter = null;
    }

    @Override
    public boolean hasParentAdapter() {
        return parentAdapter != null && parentAdapter.get() != null;
    }

    public Context getContext() {
        return context;
    }
}
