package com.github.chuross.recyclerviewadapters;


import com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils;

import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
        callback.dragFlags = dragFlags != 0 ? dragFlags : ItemTouchHelper.UP | ItemTouchHelper.DOWN;
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

            if (isRegistered(item)) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags);
            } else {
                return 0;
            }
        }

        private boolean isRegistered(LocalAdapterItem item) {
            LocalAdapter localAdapter = RecyclerAdaptersUtils.getLocalAdapter(item);

            return draggingMap.containsKey(localAdapter.getClass())
                    || draggingMap.containsKey(localAdapter);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder == null) {
                if (itemTouchStateChangeListener != null)
                    itemTouchStateChangeListener.onStateChanged(null, actionState);
                return;
            }

            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            if (item == null) return;

            if (itemTouchStateChangeListener != null)
                itemTouchStateChangeListener.onStateChanged(item, actionState);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            LocalAdapterItem targetItem = recyclerAdapter.getLocalAdapterItem(target.getAdapterPosition());

            if (item == null || targetItem == null) return false;

            LocalAdapter localAdapter = RecyclerAdaptersUtils.getLocalAdapter(item);
            LocalAdapter targetLocalAdapter = RecyclerAdaptersUtils.getLocalAdapter(targetItem);

            if (!localAdapter.equals(targetLocalAdapter)) return false;

            ((RecyclerView.Adapter) item.getLocalAdapter()).notifyItemMoved(item.getLocalAdapterPosition(), targetItem.getLocalAdapterPosition());
            if (itemMoveListener != null) itemMoveListener.onItemMoved(item, targetItem);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }
}
