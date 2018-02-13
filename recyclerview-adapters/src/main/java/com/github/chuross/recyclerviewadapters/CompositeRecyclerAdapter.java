package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    private Map<Integer, LocalAdapter<?>> unstableAdapterMapping = new HashMap<>();
    private RecyclerView recyclerView;
    private View.OnAttachStateChangeListener recyclerViewAttachStateChangeListener;

    @Override
    public final int getItemViewType(final int position) {
        final LocalAdapterItem item = getLocalAdapterItem(position);

        if (item == null) throw new IllegalStateException("LocalAdapterItem is not found.");

        final LocalAdapter<?> localAdapter = item.getLocalAdapter();

        if (localAdapter.hasStableItemViewType()) {
            return localAdapter.getAdapterId();
        }

        final int itemViewType = localAdapter.getItemViewType(item.getLocalAdapterPosition());
        unstableAdapterMapping.put(itemViewType, localAdapter);
        return itemViewType;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerViewAttachStateChangeListener = new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                clear();
                recyclerView.removeOnAttachStateChangeListener(recyclerViewAttachStateChangeListener);
            }
        };
        recyclerView.addOnAttachStateChangeListener(recyclerViewAttachStateChangeListener);
        for (LocalAdapter<?> localAdapter : localAdapters) {
            localAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int itemViewType) {
        if (localAdapterMapping.containsKey(itemViewType)) {
            return localAdapterMapping.get(itemViewType).onCreateViewHolder(parent, itemViewType);
        }
        return unstableAdapterMapping.get(itemViewType).onCreateViewHolder(parent, itemViewType);
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
        this.recyclerView = null;
    }

    private void cleanUnstableAdapterMapping(@NonNull LocalAdapter<?> targetLocalAdapter) {
        for (Map.Entry<Integer, LocalAdapter<?>> entry : unstableAdapterMapping.entrySet()) {
            if (entry.getValue().equals(targetLocalAdapter)) {
                unstableAdapterMapping.remove(entry.getKey());
            }
        }
    }

    public RecyclerView getAttachedRecyclerView() {
        return recyclerView;
    }

    public int positionOf(LocalAdapter<?> targetLocalAdapter) {
        int offset = 0;
        for (LocalAdapter<?> localAdapter : localAdapters) {
            if (!localAdapter.isVisible()) continue;

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
            if (!localAdapter.isVisible()) continue;

            size += localAdapter.getItemCount();
        }
        return size;
    }

    public int getLocalAdapterCount() {
        return localAdapters.size();
    }

    @Nullable
    public LocalAdapterItem getLocalAdapterItem(final int position) {
        if (position < 0) return null;

        int offset = 0;
        for (LocalAdapter localAdapter : localAdapters) {
            if (!localAdapter.isVisible()) continue;

            if (position < (offset + localAdapter.getItemCount())) {
                int localAdapterPosition = position - offset;
                return new LocalAdapterItem(localAdapterPosition, localAdapter);
            }
            offset += localAdapter.getItemCount();
        }
        return null;
    }

    public void add(@NonNull LocalAdapter<?> localAdapter) {
        addAll(localAdapter);
    }

    public void add(int index, @NonNull LocalAdapter<?> localAdapter) {
        addAll(index, localAdapter);
    }

    public void addAll(@NonNull Collection<LocalAdapter<?>> localAdapters) {
        checkNonNull(localAdapters);
        addAll(this.localAdapters.size(), localAdapters.toArray(new LocalAdapter[localAdapters.size()]));
    }

    public void addAll(@NonNull LocalAdapter<?>... localAdapters) {
        checkNonNull(localAdapters);
        addAll(this.localAdapters.size(), localAdapters);
    }

    public void addAll(int index, @NonNull LocalAdapter<?>... localAdapters) {
        checkNonNull(localAdapters);

        if (localAdapters.length == 0) return;

        if (localAdapters.length > 1) {
            this.localAdapters.addAll(index, Arrays.asList(localAdapters));
        } else {
            this.localAdapters.add(index, localAdapters[0]);
        }

        for (LocalAdapter localAdapter : localAdapters) {
            if (localAdapter.hasStableItemViewType()) localAdapterMapping.put(localAdapter.getAdapterId(), localAdapter);
            localAdapter.bindParentAdapter(this, new LocalAdapterDataObserver(this, localAdapter));
            if (recyclerView != null) localAdapter.onAttachedToRecyclerView(recyclerView);
        }
        notifyDataSetChanged();
    }

    public void remove(@NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.remove(localAdapter);
        localAdapterMapping.remove(localAdapter.getAdapterId());
        cleanUnstableAdapterMapping(localAdapter);
        localAdapter.unBindParentAdapter();
        if (recyclerView != null) localAdapter.onDetachedFromRecyclerView(recyclerView);
        notifyDataSetChanged();
    }

    public void clear() {
        for (LocalAdapter<?> localAdapter : localAdapters) {
            localAdapter.unBindParentAdapter();
            if (recyclerView != null) localAdapter.onDetachedFromRecyclerView(recyclerView);
        }
        localAdapters.clear();
        localAdapterMapping.clear();
        unstableAdapterMapping.clear();
        notifyDataSetChanged();
    }
}