package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.chuross.recyclerviewadapters.internal.LocalAdapterDataObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class CompositeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocalAdapter<?>> localAdapters = new ArrayList<>();
    private Map<Integer, LocalAdapter<?>> localAdapterMapping = new HashMap<>();

    @Override
    public final int getItemViewType(final int position) {
        final LocalAdapterItem item = getLocalAdapterItem(position);

        if (item == null) throw new IllegalStateException("LocalAdapterItem is not found.");

        return item.getLocalAdapter().getAdapterId();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        for (LocalAdapter<?> localAdapter : localAdapters) {
            localAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int adapterId) {
        return localAdapterMapping.get(adapterId).onCreateViewHolder(parent, adapterId);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        LocalAdapterItem localAdapterItem = getLocalAdapterItem(holder.getAdapterPosition());

        if (localAdapterItem == null) return;

        localAdapterItem.getLocalAdapter().onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        LocalAdapterItem localAdapterItem = getLocalAdapterItem(holder.getAdapterPosition());

        if (localAdapterItem == null) super.onFailedToRecycleView(holder);

        localAdapterItem.getLocalAdapter().onFailedToRecycleView(holder);
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LocalAdapterItem localAdapterItem = getLocalAdapterItem(holder.getAdapterPosition());

        if (localAdapterItem == null) return;

        localAdapterItem.getLocalAdapter().onViewAttachedToWindow(holder);
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final LocalAdapterItem localAdapterItem = getLocalAdapterItem(position);

        if (localAdapterItem == null) return;

        localAdapterItem.getLocalAdapter().onBindViewHolder(holder, localAdapterItem.getLocalAdapterPosition());
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        LocalAdapterItem localAdapterItem = getLocalAdapterItem(holder.getAdapterPosition());

        if (localAdapterItem == null) return;

        localAdapterItem.getLocalAdapter().onViewDetachedFromWindow(holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        for (LocalAdapter<?> localAdapter : localAdapters) {
            localAdapter.onDetachedFromRecyclerView(recyclerView);
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public int positionOf(LocalAdapter<?> targetLocalAdapter) {
        int offset = 0;
        for (LocalAdapter<?> localAdapter : localAdapters) {
            if (localAdapter.equals(targetLocalAdapter)) return offset;

            offset += localAdapter.getItemCount();
        }
        return offset;
    }

    @Override
    public int getItemCount() {
        return getTotalCount();
    }

    private int getTotalCount() {
        int size = 0;
        for (LocalAdapter localAdapter : localAdapters) {
            size += localAdapter.getItemCount();
        }
        return size;
    }

    public int getLocalAdapterCount() {
        return localAdapters.size();
    }

    @Nullable
    public LocalAdapterItem getLocalAdapterItem(final int position) {
        int offset = 0;
        for (LocalAdapter localAdapter : localAdapters) {
            if (position < (offset + localAdapter.getItemCount())) {
                int localAdapterPosition = position - offset;
                return new LocalAdapterItem(localAdapterPosition, localAdapter);
            }
            offset += localAdapter.getItemCount();
        }
        return null;
    }

    public void add(@NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.add(localAdapter);
        localAdapterMapping.put(localAdapter.getAdapterId(), localAdapter);
        localAdapter.bindParentAdapter(this, new LocalAdapterDataObserver(this, localAdapter));
        notifyDataSetChanged();
    }

    public void add(int index, @NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.add(index, localAdapter);
        localAdapterMapping.put(localAdapter.getAdapterId(), localAdapter);
        localAdapter.bindParentAdapter(this, new LocalAdapterDataObserver(this, localAdapter));
        notifyDataSetChanged();
    }

    public void addAll(@NonNull LocalAdapter<?>... localAdapters) {
        addAll(Arrays.asList(localAdapters));
    }

    public void addAll(@NonNull Collection<LocalAdapter<?>> localAdapters) {
        checkNonNull(localAdapters);
        this.localAdapters.addAll(localAdapters);
        for (LocalAdapter localAdapter : localAdapters) {
            localAdapterMapping.put(localAdapter.getAdapterId(), localAdapter);
            localAdapter.bindParentAdapter(this, new LocalAdapterDataObserver(this, localAdapter));
        }
        notifyDataSetChanged();
    }

    public void remove(@NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.remove(localAdapter);
        localAdapterMapping.remove(localAdapter.getAdapterId());
        localAdapter.unBindParentAdapter();
        notifyDataSetChanged();
    }

    public void clear() {
        for (LocalAdapter<?> localAdapter : localAdapters) {
            localAdapter.unBindParentAdapter();
        }
        localAdapters.clear();
        localAdapterMapping.clear();
        notifyDataSetChanged();
    }
}