package com.github.chuross.recyclerviewadapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import java.util.WeakHashMap;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

/**
 * easy SpanSizeLookup Builder for CompositeRecyclerAdapter
 */
public class SpanSizeLookupBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Integer> spanSizeMapping = new WeakHashMap<>();

    public SpanSizeLookupBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
    }

    public <CLASS extends Class<? extends LocalAdapter<?>>> SpanSizeLookupBuilder register(@NonNull CLASS adapterClass, int spanSize) {
        checkNonNull(adapterClass);
        spanSizeMapping.put(adapterClass, spanSize);
        return this;
    }

    public SpanSizeLookupBuilder register(@NonNull LocalAdapter localAdapter, int spanSize) {
        checkNonNull(localAdapter);
        spanSizeMapping.put(localAdapter, spanSize);
        return this;
    }

    public GridSpanSizeLookup build() {
        return new GridSpanSizeLookup(recyclerAdapter, spanSizeMapping);
    }

    public static class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        CompositeRecyclerAdapter recyclerAdapter;
        WeakHashMap<Object, Integer> spanSizeMapping;

        private GridSpanSizeLookup(@NonNull CompositeRecyclerAdapter recyclerAdapter, @NonNull WeakHashMap<Object, Integer> spanSizeMapping) {
            this.recyclerAdapter = recyclerAdapter;
            this.spanSizeMapping = spanSizeMapping;
        }

        @Override
        public int getSpanSize(int position) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(position);
            if (item == null) return 1;

            final Integer spanSize = get(item.getLocalAdapter());
            if (spanSize == null) return 1;
            return spanSize;
        }

        @Nullable
        private Integer get(LocalAdapter localAdapter) {
            if (localAdapter == null) return null;

            if (spanSizeMapping.containsKey(localAdapter)) {
                return spanSizeMapping.get(localAdapter);
            }

            if (spanSizeMapping.containsKey(localAdapter.getClass())) {
                return spanSizeMapping.get(localAdapter.getClass());
            }

            return null;
        }
    }
}
