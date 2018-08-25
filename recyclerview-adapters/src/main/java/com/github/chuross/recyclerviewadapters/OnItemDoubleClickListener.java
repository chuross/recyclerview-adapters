package com.github.chuross.recyclerviewadapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnItemDoubleClickListener<T> {

    void onItemDoubleClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item);
}
