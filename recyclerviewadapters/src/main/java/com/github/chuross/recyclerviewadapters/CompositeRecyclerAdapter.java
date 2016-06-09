package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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
    public int getItemViewType(final int position) {
        final LocalAdapterItem item = getLocalAdapterItem(position);
        if (item == null) {
            throw new IllegalStateException("LocalAdapterItem is not found.");
        }
        return item.getLocalAdapter().getAdapterType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int adapterType) {
        return localAdapterMapping.get(adapterType).onCreateViewHolder(parent, adapterType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final LocalAdapterItem item = getLocalAdapterItem(position);
        if (item == null) {
            return;
        }
        item.getLocalAdapter().onBindViewHolder(holder, item.getLocalAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return getTotalCount();
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

    private int getTotalCount() {
        int size = 0;
        for (LocalAdapter localAdapter : localAdapters) {
            size += localAdapter.getItemCount();
        }
        return size;
    }

    public void add(@NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.add(localAdapter);
        localAdapterMapping.put(localAdapter.getAdapterType(), localAdapter);
        localAdapter.bindParentAdapter(this);
        notifyDataSetChanged();
    }

    public void add(int index, @NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.add(index, localAdapter);
        localAdapterMapping.put(localAdapter.getAdapterType(), localAdapter);
        localAdapter.bindParentAdapter(this);
        notifyDataSetChanged();
    }

    public void addAll(@NonNull LocalAdapter<?>... localAdapters) {
        addAll(Arrays.asList(localAdapters));
    }

    public void addAll(@NonNull Collection<LocalAdapter<?>> localAdapters) {
        checkNonNull(localAdapters);
        this.localAdapters.addAll(localAdapters);
        for (LocalAdapter localAdapter : localAdapters) {
            localAdapterMapping.put(localAdapter.getAdapterType(), localAdapter);
            localAdapter.bindParentAdapter(this);
        }
        notifyDataSetChanged();
    }

    public void remove(@NonNull LocalAdapter<?> localAdapter) {
        checkNonNull(localAdapter);
        localAdapters.remove(localAdapter);
        localAdapterMapping.remove(localAdapter.getAdapterType());
        localAdapter.unBindParentAdapter();
        notifyDataSetChanged();
    }
}