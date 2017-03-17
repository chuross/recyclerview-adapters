package com.github.chuross.recyclerviewadapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.WeakHashMap;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class DividerItemDecorationBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Boolean> decorationMap = new WeakHashMap<>();
    private int dividerHeight = 0;
    private int dividerColor = Color.BLACK;

    public DividerItemDecorationBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter, int dividerHeight, int dividerColor) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
        this.dividerHeight = dividerHeight;
        this.dividerColor = dividerColor;
    }

    public <CLASS extends Class<? extends LocalAdapter>> DividerItemDecorationBuilder put(@NonNull CLASS adapterClass) {
        checkNonNull(adapterClass);
        decorationMap.put(adapterClass, true);
        return this;
    }

    public DividerItemDecorationBuilder put(@NonNull LocalAdapter localAdapter) {
        checkNonNull(localAdapter);
        decorationMap.put(localAdapter, true);
        return this;
    }

    public DividerItemDecoration build() {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(dividerColor);
        paint.setStrokeWidth(dividerHeight);

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.recyclerAdapter = recyclerAdapter;
        itemDecoration.decorationMap = decorationMap;
        itemDecoration.dividerHeight = dividerHeight;
        itemDecoration.dividerPaint = paint;
        return itemDecoration;
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        CompositeRecyclerAdapter recyclerAdapter;
        WeakHashMap<Object, Boolean> decorationMap;
        int dividerHeight;
        Paint dividerPaint;

        DividerItemDecoration() {
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() + parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View view = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

                if (params == null) continue;

                final LocalAdapterItem adapterItem = recyclerAdapter.getLocalAdapterItem(params.getViewAdapterPosition());

                if (adapterItem == null) continue;

                if (!decorationMap.containsKey(adapterItem.getLocalAdapter())
                        && !decorationMap.containsKey(adapterItem.getLocalAdapter().getClass())) {
                    continue;
                }

                final int top = view.getBottom() + params.bottomMargin;
                final int bottom = top + (dividerHeight / 2);

                canvas.drawLine(left, bottom, right, bottom, dividerPaint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            if (params == null) return;

            final LocalAdapterItem adapterItem = recyclerAdapter.getLocalAdapterItem(params.getViewAdapterPosition());

            if (adapterItem == null) return;

            if (!decorationMap.containsKey(adapterItem.getLocalAdapter())
                    && !decorationMap.containsKey(adapterItem.getLocalAdapter().getClass())) {
                return;
            }

            outRect.set(0, 0, 0, dividerHeight);
        }
    }
}
