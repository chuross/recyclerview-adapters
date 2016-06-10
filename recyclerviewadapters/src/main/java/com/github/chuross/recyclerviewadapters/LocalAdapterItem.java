package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;

public class LocalAdapterItem {

    private int localAdapterPosition;
    private LocalAdapter localAdapter;

    public LocalAdapterItem(int localAdapterPosition, @NonNull LocalAdapter localAdapter) {
        this.localAdapterPosition = localAdapterPosition;
        this.localAdapter = localAdapter;
    }

    public int getLocalAdapterPosition() {
        return localAdapterPosition;
    }

    @NonNull
    public LocalAdapter getLocalAdapter() {
        return localAdapter;
    }

}
