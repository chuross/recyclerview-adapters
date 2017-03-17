package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public abstract class ItemAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseItemAdapter<T, VH> implements RecyclerView.OnItemTouchListener {

    private final List<T> items = new ArrayList<>();

    public ItemAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public T get(int index) {
        return items.get(index);
    }

    @NonNull
    protected List<T> getAll() {
        return items;
    }

    public void add(@NonNull T item) {
        checkNonNull(item);
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void add(int index, @NonNull T item) {
        checkNonNull(item);
        items.add(index, item);
        notifyItemInserted(index);
    }

    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public void addAll(Collection<T> items) {
        checkNonNull(items);
        this.items.addAll(items);
        notifyItemRangeInserted(this.items.size() - items.size(), items.size());
    }

    public void remove(@NonNull T item) {
        checkNonNull(item);
        int index = items.indexOf(item);
        if (index < 0) return;
        items.remove(item);
        notifyItemRemoved(index);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
}
