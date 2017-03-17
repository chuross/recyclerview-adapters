package com.github.chuross.recyclerviewadapters.internal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.github.chuross.recyclerviewadapters.CompositeRecyclerAdapter;
import com.github.chuross.recyclerviewadapters.LocalAdapter;

import java.lang.ref.WeakReference;

public class LocalAdapterDataObserver extends RecyclerView.AdapterDataObserver {

    private WeakReference<CompositeRecyclerAdapter> parentAdapter;
    private WeakReference<LocalAdapter<?>> localAdapter;

    public LocalAdapterDataObserver(@NonNull CompositeRecyclerAdapter parentAdapter, @NonNull LocalAdapter<?> localAdapter) {
        this.parentAdapter = new WeakReference<>(parentAdapter);
        this.localAdapter = new WeakReference<LocalAdapter<?>>(localAdapter);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();

        if (parentAdapter == null) return;

        parentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();
        LocalAdapter<?> localAdapter = this.localAdapter.get();

        if (parentAdapter == null || localAdapter == null) return;

        int adapterPosition = parentAdapter.positionOf(localAdapter);
        parentAdapter.notifyItemRangeChanged(adapterPosition + positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();
        LocalAdapter<?> localAdapter = this.localAdapter.get();

        if (parentAdapter == null || localAdapter == null) return;

        int adapterPosition = parentAdapter.positionOf(localAdapter);
        parentAdapter.notifyItemRangeChanged(adapterPosition + positionStart, itemCount, payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();
        LocalAdapter<?> localAdapter = this.localAdapter.get();

        if (parentAdapter == null || localAdapter == null) return;

        int adapterPosition = parentAdapter.positionOf(localAdapter);
        parentAdapter.notifyItemRangeInserted(adapterPosition + positionStart, itemCount);


    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);

        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();
        LocalAdapter<?> localAdapter = this.localAdapter.get();

        if (parentAdapter == null || localAdapter == null) return;

        int adapterPosition = parentAdapter.positionOf(localAdapter);
        parentAdapter.notifyItemRangeRemoved(adapterPosition + positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        CompositeRecyclerAdapter parentAdapter = this.parentAdapter.get();
        LocalAdapter<?> localAdapter = this.localAdapter.get();

        if (parentAdapter == null || localAdapter == null) return;

        int adapterPosition = parentAdapter.positionOf(localAdapter);
        parentAdapter.notifyItemMoved(adapterPosition + fromPosition, adapterPosition + toPosition);
    }
}
