package com.github.chuross.recyclerviewadapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;
import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public class DragItemTouchHelperBuilder {

    private CompositeRecyclerAdapter recyclerAdapter;
    private List<Class<? extends LocalAdapter>> acceptedClasses = new ArrayList<>();
    private int dragFlags = 0;

    public DragItemTouchHelperBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
    }

    public DragItemTouchHelperBuilder dragFlag(int dragFlag) {
        this.dragFlags = this.dragFlags | dragFlag;
        return this;
    }

    public DragItemTouchHelperBuilder register(Class<? extends LocalAdapter> clazz) {
        acceptedClasses.add(clazz);
        return this;
    }

    public ItemTouchHelper build() {
        DragItemCallback callback = new DragItemCallback();
        callback.recyclerAdapter = recyclerAdapter;
        callback.acceptedClasses = acceptedClasses;
        callback.dragFlags = dragFlags != 0 ? dragFlags : UP | DOWN;

        return new ItemTouchHelper(callback);
    }

    private static class DragItemCallback extends ItemTouchHelper.Callback {

        CompositeRecyclerAdapter recyclerAdapter;
        List<Class<? extends LocalAdapter>> acceptedClasses;
        int dragFlags;

        private DragItemCallback() {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            if (item == null) return 0;

            if (acceptedClasses.contains(item.getLocalAdapter().getClass())) {
                return makeFlag(ACTION_STATE_DRAG, dragFlags);
            } else {
                return 0;
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            LocalAdapterItem item = recyclerAdapter.getLocalAdapterItem(viewHolder.getAdapterPosition());
            LocalAdapterItem targetItem = recyclerAdapter.getLocalAdapterItem(target.getAdapterPosition());

            if (item == null || targetItem == null || !item.getLocalAdapter().equals(targetItem.getLocalAdapter())) return false;

            if (item.getLocalAdapter().getClass().equals(targetItem.getLocalAdapter().getClass())) {
                BaseLocalAdapter localAdapter = (BaseLocalAdapter) item.getLocalAdapter();
                localAdapter.notifyItemMoved(item.getLocalAdapterPosition(), targetItem.getLocalAdapterPosition());
                return true;
            }

            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }
}
