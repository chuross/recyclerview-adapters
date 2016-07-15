package com.github.chuross.recyclerviewadapters;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.WeakHashMap;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

/**
 * easy GridItem with padding Builder for CompositeRecyclerAdapter
 */
public class PaddingItemDecorationBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<LocalAdapter, Boolean> paddingMap = new WeakHashMap<>();

    public PaddingItemDecorationBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
    }

    public PaddingItemDecorationBuilder register(@NonNull LocalAdapter localAdapter) {
        checkNonNull(localAdapter);
        paddingMap.put(localAdapter, true);
        return this;
    }

    public RecyclerView.ItemDecoration build(final int padding, final int maxSpanSize) {
        return new RecyclerView.ItemDecoration() {

            private boolean usePadding(int position) {
                LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(position);
                return item != null && paddingMap.containsKey(item.getLocalAdapter());
            }

            @Override
            public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int position = params.getViewAdapterPosition();
                final int spanSize;
                final int index;

                if (!(params instanceof GridLayoutManager.LayoutParams)) {
                    return;
                }

                final GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
                spanSize = gridParams.getSpanSize();
                index = gridParams.getSpanIndex();

                if (spanSize < 1 || index < 0) {
                    return;
                }

                if (!usePadding(position)) {
                    return;
                }

                if (spanSize == maxSpanSize) {
                    outRect.left = padding;
                    outRect.right = padding;
                } else {
                    if (index == 0) {
                        outRect.left = padding;
                    }
                    if (index + spanSize == maxSpanSize) {
                        outRect.right = padding;
                    }
                    if (outRect.left == 0) {
                        outRect.left = padding / 2;
                    }
                    if (outRect.right == 0) {
                        outRect.right = padding / 2;
                    }
                }

                outRect.bottom = padding;
            }
        };
    }

}
