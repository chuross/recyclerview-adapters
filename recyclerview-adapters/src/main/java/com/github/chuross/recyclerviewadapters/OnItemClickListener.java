package com.github.chuross.recyclerviewadapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnItemClickListener<T> {

    void onItemClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item);
}