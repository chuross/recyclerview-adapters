package com.github.chuross.recyclerviewadapters;


import android.support.annotation.Nullable;

public interface OnItemTouchStateChangeListener {

    void onStateChanged(@Nullable LocalAdapterItem item, int actionState);
}
