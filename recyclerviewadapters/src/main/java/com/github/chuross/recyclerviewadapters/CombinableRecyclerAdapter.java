package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.*;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class CombinableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocalAdapter<?>> localAdapters = new ArrayList<>();
    private Map<Integer, LocalAdapter<?>> localAdapterMapping = new HashMap<>();

    @Override
    public int getItemViewType(final int position) {
        final Pair<Integer, LocalAdapter> info = getLocalAdapterInfo(position);

        if (info == null) {
            throw new IllegalStateException("CombinableAdapter is not found.");
        }
        return info.second.getAdapterType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int adapterType) {
        return localAdapterMapping.get(adapterType).onCreateViewHolder(parent, adapterType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Pair<Integer, LocalAdapter> info = getLocalAdapterInfo(position);

        if (info == null) {
            return;
        }

        final int localAdapterPosition = info.first;
        final LocalAdapter localAdapter = info.second;

        localAdapter.onBindViewHolder(holder, localAdapterPosition);
    }

    @Override
    public int getItemCount() {
        return getTotalCount();
    }

    @Nullable
    private Pair<Integer, LocalAdapter> getLocalAdapterInfo(final int position) {
        int offset = 0;
        for (LocalAdapter localAdapter : localAdapters) {
            if (position < (offset + localAdapter.getItemCount())) {
                int localAdapterPosition = position - offset;
                return Pair.create(localAdapterPosition, localAdapter);
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

    public int getPositionFromParent(int position) {
        Pair<Integer, LocalAdapter> info = getLocalAdapterInfo(position);
        return info != null ? info.first : -1;
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