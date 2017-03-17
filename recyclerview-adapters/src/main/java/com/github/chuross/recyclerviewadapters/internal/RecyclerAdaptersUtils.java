package com.github.chuross.recyclerviewadapters.internal;

import android.support.annotation.Nullable;

public final class RecyclerAdaptersUtils {

    public static void checkNonNull(@Nullable Object object) {
        if(object == null) {
            throw new NullPointerException("object is null.");
        }
    }
}
