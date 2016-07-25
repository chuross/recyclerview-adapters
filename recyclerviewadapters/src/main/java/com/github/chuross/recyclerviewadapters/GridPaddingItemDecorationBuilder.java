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
public class GridPaddingItemDecorationBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Boolean> paddingMap = new WeakHashMap<>();
    private int padding;
    private int maxSpanSize;
    private PaddingType paddingType;

    public GridPaddingItemDecorationBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter, int padding, int maxSpanSize) {
        this(recyclerAdapter, padding, maxSpanSize, PaddingType.BOTH);
    }

    public GridPaddingItemDecorationBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter, int padding, int maxSpanSize, PaddingType paddingType) {
        this.recyclerAdapter = recyclerAdapter;
        this.padding = padding;
        this.maxSpanSize = maxSpanSize;
        this.paddingType = paddingType;
    }

    public <CLASS extends Class<? extends LocalAdapter>> GridPaddingItemDecorationBuilder put(@NonNull CLASS adapterClass) {
        checkNonNull(adapterClass);
        paddingMap.put(adapterClass, true);
        return this;
    }

    public GridPaddingItemDecorationBuilder put(@NonNull LocalAdapter localAdapter) {
        checkNonNull(localAdapter);
        paddingMap.put(localAdapter, true);
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new GridPaddingItemDecoration(recyclerAdapter, padding, maxSpanSize, paddingMap, paddingType);
    }

    public enum PaddingType {
        BOTH, VERTICAL, HORIZONTAL
    }

    public static class GridPaddingItemDecoration extends RecyclerView.ItemDecoration {

        private CompositeRecyclerAdapter recyclerAdapter;
        private WeakHashMap<Object, Boolean> paddingMap;
        private int padding;
        private int maxSpanSize;
        private PaddingType paddingType;

        private GridPaddingItemDecoration(@NonNull CompositeRecyclerAdapter recyclerAdapter, int padding, int maxSpanSize, WeakHashMap<Object, Boolean> paddingMap, PaddingType paddingType) {
            this.recyclerAdapter = recyclerAdapter;
            this.padding = padding;
            this.maxSpanSize = maxSpanSize;
            this.paddingMap = paddingMap;
            this.paddingType = paddingType;
        }

        @Override
        public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            if (!(params instanceof GridLayoutManager.LayoutParams)) return;

            final GridParams gridParams = GridParams.create(view);
            if (gridParams == null) return;

            final int position = gridParams.getPosition();
            final int spanSize = gridParams.getSpanSize();
            final int index = gridParams.getSpanIndex();

            if (spanSize < 1 || index < 0) return;
            if (!usePadding(position)) return;

            boolean useVerticalPadding = paddingType.equals(PaddingType.BOTH) || paddingType.equals(PaddingType.VERTICAL);

            outRect.top = useVerticalPadding ? padding / 2 : 0;
            outRect.bottom = useVerticalPadding ? padding / 2 : 0;
            outRect.left = getPaddingLeft(index, spanSize);
            outRect.right = getPaddingRight(index, spanSize);
        }

        private boolean usePadding(int position) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(position);
            if (item == null) return false;

            return paddingMap.containsKey(item.getLocalAdapter()) || paddingMap.containsKey(item.getLocalAdapter().getClass());
        }

        private int getPaddingLeft(int index, int spanSize) {
            if (!paddingType.equals(PaddingType.BOTH) && !paddingType.equals(PaddingType.HORIZONTAL)) {
                return 0;
            }
            return index == 0 || spanSize == maxSpanSize ? padding : padding / 2;
        }

        private int getPaddingRight(int index, int spanSize) {
            if (!paddingType.equals(PaddingType.BOTH) && !paddingType.equals(PaddingType.HORIZONTAL)) {
                return 0;
            }
            return index + spanSize == maxSpanSize ? padding : padding / 2;
        }
    }

    private static class GridParams {

        private int position;
        private int spanIndex;
        private int spanSize;

        public static GridParams create(View view) {
            return view.getLayoutParams() instanceof GridLayoutManager.LayoutParams ? new GridParams((GridLayoutManager.LayoutParams) view.getLayoutParams()) : null;
        }

        public GridParams(GridLayoutManager.LayoutParams params) {
            position = params.getViewAdapterPosition();
            spanIndex = params.getSpanIndex();
            spanSize = params.getSpanSize();
        }

        public int getPosition() {
            return position;
        }

        public int getSpanIndex() {
            return spanIndex;
        }

        public int getSpanSize() {
            return spanSize;
        }
    }
}
