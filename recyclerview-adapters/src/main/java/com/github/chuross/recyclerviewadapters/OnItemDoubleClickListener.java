package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public interface OnItemDoubleClickListener<T> {

    void onItemDoubleClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item);
}
