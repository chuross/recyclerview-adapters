package com.github.chuross.recyclerviewadapters;


import androidx.annotation.Nullable;

public interface OnItemTouchStateChangeListener {

    void onStateChanged(@Nullable LocalAdapterItem item, int actionState);
}
