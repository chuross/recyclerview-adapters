package com.github.chuross.recyclerviewadapters.internal;

import com.github.chuross.recyclerviewadapters.CompositeRecyclerAdapter;
import com.github.chuross.recyclerviewadapters.LocalAdapter;
import com.github.chuross.recyclerviewadapters.LocalAdapterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class RecyclerAdaptersUtils {

    public static void checkNonNull(@Nullable Object object) {
        if (object == null) {
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
