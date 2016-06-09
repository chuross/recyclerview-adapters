package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public abstract class ItemAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseLocalAdapter<VH> implements View.OnClickListener {

    private final List<T> items = new ArrayList<>();
    private OnItemClickListener<T> listener;

    public ItemAdapter(@NonNull Context context) {
        super(context);
    }

    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent);

    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = onCreateViewHolder(parent);
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if (listener == null || v == null) return;

        final RecyclerView recyclerView = ((RecyclerView) v.getParent());
        int parentAdapterPosition = recyclerView.getChildAdapterPosition(v);

        if (!hasParentAdapter()) {
            listener.onItemClicked(recyclerView.getChildViewHolder(v), parentAdapterPosition, get(parentAdapterPosition));
            return;
        }

        LocalAdapterItem localAdapterItem = getParentAdapter().getLocalAdapterItem(parentAdapterPosition);
        if (localAdapterItem == null) return;

        int localAdapterPosition = localAdapterItem.getLocalAdapterPosition();
        listener.onItemClicked(recyclerView.getChildViewHolder(v), localAdapterPosition, get(localAdapterPosition));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    public T get(int index) {
        return items.get(index);
    }

    public void add(@NonNull T item) {
        checkNonNull(item);
        items.add(item);
        notifyDataSetChanged();
    }

    public void add(int index, @NonNull T item) {
        checkNonNull(item);
        items.add(index, item);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> items) {
        checkNonNull(items);
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(@NonNull T item) {
        checkNonNull(item);
        items.remove(item);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> listener) {
        this.listener = listener;
    }
}
