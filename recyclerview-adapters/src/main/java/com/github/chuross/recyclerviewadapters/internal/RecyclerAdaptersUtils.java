package com.github.chuross.recyclerviewadapters.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.chuross.recyclerviewadapters.CompositeRecyclerAdapter;
import com.github.chuross.recyclerviewadapters.LocalAdapter;
import com.github.chuross.recyclerviewadapters.LocalAdapterItem;

public final class RecyclerAdaptersUtils {

    public static void checkNonNull(@Nullable Object object) {
        if(object == null) {
            throw new NullPointerException("object is null.");
        }
    }

    @NonNull
    public static LocalAdapter getLocalAdapter(LocalAdapterItem item) {
        LocalAdapter localAdapter = item.getLocalAdapter();
        if (localAdapter instanceof CompositeRecyclerAdapter) {
            CompositeRecyclerAdapter compositeRecyclerAdapter = (CompositeRecyclerAdapter) localAdapter;
            return getLocalAdapter(compositeRecyclerAdapter.getLocalAdapterItem(item.getLocalAdapterPosition()));
        }
        return localAdapter;
    }
}
