package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.*;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class CombinableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocalAdapter<?>> combinables = new ArrayList<>();
    private Map<Integer, LocalAdapter<?>> combinableMapping = new HashMap<>();

    @Override
    public int getItemViewType(final int position) {
        final Pair<Integer, LocalAdapter> info = getCombinableInfo(position);

        if (info == null) {
            throw new IllegalStateException("CombinableAdapter is not found.");
        }
        return info.second.getAdapterType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int adapterType) {
        return combinableMapping.get(adapterType).onCreateViewHolder(parent, adapterType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Pair<Integer, LocalAdapter> info = getCombinableInfo(position);

        if (info == null) {
            return;
        }

        final int combinableAdapterPosition = info.first;
        final LocalAdapter combinableAdapter = info.second;

        combinableAdapter.onBindViewHolder(holder, combinableAdapterPosition);
    }

    @Override
    public int getItemCount() {
        return getTotalCount();
    }

    @Nullable
    private Pair<Integer, LocalAdapter> getCombinableInfo(final int position) {
        int offset = 0;
        for (LocalAdapter combinable : combinables) {
            if (position < (offset + combinable.getItemCount())) {
                int combinableAdapterPosition = position - offset;
                return Pair.create(combinableAdapterPosition, combinable);
            }
            offset += combinable.getItemCount();
        }
        return null;
    }

    private int getTotalCount() {
        int size = 0;
        for (LocalAdapter combinable : combinables) {
            size += combinable.getItemCount();
        }
        return size;
    }

    public int getCombinableAdapterItemPosition(int position) {
        Pair<Integer, LocalAdapter> info = getCombinableInfo(position);
        return info != null ? info.first : -1;
    }

    public void add(@NonNull LocalAdapter<?> combinable) {
        checkNonNull(combinable);
        combinables.add(combinable);
        combinableMapping.put(combinable.getAdapterType(), combinable);
        combinable.bindParentAdapter(this);
        notifyDataSetChanged();
    }

    public void add(int index, @NonNull LocalAdapter<?> combinable) {
        checkNonNull(combinable);
        combinables.add(index, combinable);
        combinableMapping.put(combinable.getAdapterType(), combinable);
        combinable.bindParentAdapter(this);
        notifyDataSetChanged();
    }

    public void addAll(@NonNull LocalAdapter<?>... combinables) {
        addAll(Arrays.asList(combinables));
    }

    public void addAll(@NonNull Collection<LocalAdapter<?>> combinables) {
        checkNonNull(combinables);
        this.combinables.addAll(combinables);
        for (LocalAdapter combinable : combinables) {
            combinableMapping.put(combinable.getAdapterType(), combinable);
            combinable.bindParentAdapter(this);
        }
        notifyDataSetChanged();
    }

    public void remove(@NonNull LocalAdapter<?> combinable) {
        checkNonNull(combinable);
        combinables.remove(combinable);
        combinableMapping.remove(combinable.getAdapterType());
        combinable.unBindParentAdapter();
        notifyDataSetChanged();
    }
}