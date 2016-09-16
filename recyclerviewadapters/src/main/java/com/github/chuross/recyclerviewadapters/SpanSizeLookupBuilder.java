package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Pair;

import java.util.WeakHashMap;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

/**
 * easy SpanSizeLookup Builder for CompositeRecyclerAdapter
 */
public class SpanSizeLookupBuilder {

    private Context context;
    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Pair<Integer, Integer>> spanSizeMapping = new WeakHashMap<>();

    public SpanSizeLookupBuilder(@NonNull Context context, @NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(context);
        checkNonNull(recyclerAdapter);
        this.context = context;
        this.recyclerAdapter = recyclerAdapter;
    }


    public <CLASS extends Class<? extends LocalAdapter<?>>> SpanSizeLookupBuilder bind(@NonNull CLASS adapterClass, int spanSize) {
        return bind(adapterClass, spanSize, spanSize);
    }

    public <CLASS extends Class<? extends LocalAdapter<?>>> SpanSizeLookupBuilder bind(@NonNull CLASS adapterClass, int portraitSpanSize, int landScapeSpanSize) {
        checkNonNull(adapterClass);
        spanSizeMapping.put(adapterClass, Pair.create(portraitSpanSize, landScapeSpanSize));
        return this;
    }

    public SpanSizeLookupBuilder bind(@NonNull LocalAdapter localAdapter, int spanSize) {
        return bind(localAdapter, spanSize, spanSize);
    }

    public SpanSizeLookupBuilder bind(@NonNull LocalAdapter localAdapter, int portraitSpanSize, int landScapeSpanSize) {
        checkNonNull(localAdapter);
        spanSizeMapping.put(localAdapter, Pair.create(portraitSpanSize, landScapeSpanSize));
        return this;
    }

    public GridSpanSizeLookup build() {
        return new GridSpanSizeLookup(context, recyclerAdapter, spanSizeMapping);
    }

    public static class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private Context context;
        private CompositeRecyclerAdapter recyclerAdapter;
        private WeakHashMap<Object, Pair<Integer, Integer>> spanSizeMapping;

        private GridSpanSizeLookup(@NonNull Context context, @NonNull CompositeRecyclerAdapter recyclerAdapter, @NonNull WeakHashMap<Object, Pair<Integer, Integer>> spanSizeMapping) {
            this.context = context;
            this.recyclerAdapter = recyclerAdapter;
            this.spanSizeMapping = spanSizeMapping;
        }

        @Override
        public int getSpanSize(int position) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(position);
            if (item == null) return 1;

            final Pair<Integer, Integer> spanSizePair = get(item.getLocalAdapter());
            if (spanSizePair == null) return 1;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                return spanSizePair.first;
            } else {
                return spanSizePair.second;
            }
        }

        @Nullable
        private Pair<Integer, Integer> get(LocalAdapter localAdapter) {
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
