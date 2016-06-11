package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
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
    private WeakHashMap<LocalAdapter, Pair<Integer, Integer>> spanSizeMapping = new WeakHashMap<>();

    public SpanSizeLookupBuilder(@NonNull Context context, @NonNull CompositeRecyclerAdapter recyclerAdapter) {
        this.context = context;
        this.recyclerAdapter = recyclerAdapter;
    }

    public SpanSizeLookupBuilder bind(@NonNull LocalAdapter localAdapter, int spanSize) {
        return bind(localAdapter, spanSize, spanSize);
    }

    public SpanSizeLookupBuilder bind(@NonNull LocalAdapter localAdapter, int portraitSpanSize, int landScapeSpanSize) {
        checkNonNull(localAdapter);
        spanSizeMapping.put(localAdapter, Pair.create(portraitSpanSize, landScapeSpanSize));
        return this;
    }

    public GridLayoutManager.SpanSizeLookup build() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(position);
                if (item == null || !spanSizeMapping.containsKey(item.getLocalAdapter())) {
                    return 1;
                }
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    return spanSizeMapping.get(item.getLocalAdapter()).first;
                } else {
                    return spanSizeMapping.get(item.getLocalAdapter()).second;
                }
            }
        };
    }
}
