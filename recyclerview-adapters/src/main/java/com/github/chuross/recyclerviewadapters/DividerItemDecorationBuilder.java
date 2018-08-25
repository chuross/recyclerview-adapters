package com.github.chuross.recyclerviewadapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils;

import java.util.WeakHashMap;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class DividerItemDecorationBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Boolean> decorationMap = new WeakHashMap<>();
    private int dividerHeight = 0;
    private int dividerColor = Color.BLACK;

    public DividerItemDecorationBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
    }

    public <CLASS extends Class<? extends LocalAdapter>> DividerItemDecorationBuilder register(@NonNull CLASS adapterClass) {
        checkNonNull(adapterClass);
        decorationMap.put(adapterClass, true);
        return this;
    }

    public DividerItemDecorationBuilder register(@NonNull LocalAdapter localAdapter) {
        checkNonNull(localAdapter);
        decorationMap.put(localAdapter, true);
        return this;
    }

    public DividerItemDecorationBuilder dividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }

    public DividerItemDecorationBuilder dividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
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

        private DividerItemDecoration() {
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

                if (!isRegistered(adapterItem)) {
                    continue;
                }

                final int top = view.getBottom() + params.bottomMargin;
                final int bottom = top + (dividerHeight / 2);

                canvas.drawLine(left, bottom, right, bottom, dividerPaint);
            }
        }

        private boolean isRegistered(LocalAdapterItem item) {
            LocalAdapter localAdapter = RecyclerAdaptersUtils.getLocalAdapter(item);

            return decorationMap.containsKey(localAdapter)
                    || decorationMap.containsKey(localAdapter.getClass());
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
