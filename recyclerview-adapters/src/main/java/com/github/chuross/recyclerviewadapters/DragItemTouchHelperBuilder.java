package com.github.chuross.recyclerviewadapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;
import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class DragItemTouchHelperBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private WeakHashMap<Object, Boolean> draggingMap = new WeakHashMap<>();
    private int dragFlags = 0;
    private OnItemMoveListener itemMoveListener;
    private OnItemTouchStateChangeListener itemTouchStateChangeListener;

    public DragItemTouchHelperBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
    }

    public DragItemTouchHelperBuilder dragFlag(int dragFlag) {
        this.dragFlags = this.dragFlags | dragFlag;
        return this;
    }

    public DragItemTouchHelperBuilder register(Class<? extends LocalAdapter> clazz) {
        checkNonNull(clazz);
        draggingMap.put(clazz, true);
        return this;
    }

    public DragItemTouchHelperBuilder register(@NonNull LocalAdapter localAdapter) {
        checkNonNull(localAdapter);
        draggingMap.put(localAdapter, true);
        return this;
    }

    public DragItemTouchHelperBuilder itemMoveListener(OnItemMoveListener itemMoveListener) {
        this.itemMoveListener = itemMoveListener;
        return this;
    }

    public DragItemTouchHelperBuilder itemTouchStateChangeListener(OnItemTouchStateChangeListener itemTouchStateChangeListener) {
        this.itemTouchStateChangeListener = itemTouchStateChangeListener;
        return this;
    }

    public ItemTouchHelper build() {
        DragItemCallback callback = new DragItemCallback();
        callback.recyclerAdapter = recyclerAdapter;
        callback.draggingMap = draggingMap;
        callback.dragFlags = dragFlags != 0 ? dragFlags : UP | DOWN;
        callback.itemMoveListener = itemMoveListener;
        callback.itemTouchStateChangeListener = itemTouchStateChangeListener;

        return new ItemTouchHelper(callback);
    }

    private static class DragItemCallback extends ItemTouchHelper.Callback {

        CompositeRecyclerAdapter recyclerAdapter;
        WeakHashMap<Object, Boolean> draggingMap;
        int dragFlags;
        OnItemMoveListener itemMoveListener;
        OnItemTouchStateChangeListener itemTouchStateChangeListener;

        private DragItemCallback() {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            if (item == null) return 0;

            if (draggingMap.containsKey(item.getLocalAdapter().getClass())
                    || draggingMap.containsKey(item.getLocalAdapter())) {
                return makeFlag(ACTION_STATE_DRAG, dragFlags);
            } else {
                return 0;
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder == null) {
                if (itemTouchStateChangeListener != null) itemTouchStateChangeListener.onStateChanged(null, actionState);
                return;
            }

            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            if (item == null) return;

            if (itemTouchStateChangeListener != null) itemTouchStateChangeListener.onStateChanged(item, actionState);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            LocalAdapterItem targetItem = recyclerAdapter.getLocalAdapterItem(target.getAdapterPosition());

            if (item == null || targetItem == null || !item.getLocalAdapter().equals(targetItem.getLocalAdapter())) return false;

            if (item.getLocalAdapter().getClass().equals(targetItem.getLocalAdapter().getClass())) {
                BaseLocalAdapter localAdapter = (BaseLocalAdapter) item.getLocalAdapter();
                localAdapter.notifyItemMoved(item.getLocalAdapterPosition(), targetItem.getLocalAdapterPosition());
                if (itemMoveListener != null) itemMoveListener.onItemMoved(item, targetItem);
                return true;
            }

            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }
}
