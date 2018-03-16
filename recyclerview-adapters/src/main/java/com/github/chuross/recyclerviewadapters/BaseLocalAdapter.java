package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

public abstract class BaseLocalAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements LocalAdapter<VH> {

    private Context context;
    private WeakReference<CompositeRecyclerAdapter> parentAdapter;
    private boolean visible = true;
    private boolean isAttached = false;

    public BaseLocalAdapter(@NonNull Context context) {
        this.context = context;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setVisible(boolean visible) {
        setVisible(visible, false);
    }

    public void setVisible(boolean visible, boolean animated) {
        if (!animated) {
            this.visible = visible;
            notifyDataSetChanged();
            return;
        }

        if (visible) {
            this.visible = visible;
            notifyItemRangeInserted(0, getItemCount());
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            this.visible = visible;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        isAttached = true;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        isAttached = false;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * @return 0 or R.layout
     */
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean hasStableItemViewType() {
        try {
            return getItemViewType(0) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CompositeRecyclerAdapter getParentAdapter() {
        return hasParentAdapter() ? parentAdapter.get() : null;
    }

    @Override
    public void bindParentAdapter(@Nullable CompositeRecyclerAdapter adapter, @Nullable RecyclerView.AdapterDataObserver dataObserver) {
        if (hasParentAdapter()) {
            throw new IllegalStateException("Adapter already has parentAdapter.");
        }
        parentAdapter = new WeakReference<>(adapter);
        registerAdapterDataObserver(dataObserver);
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