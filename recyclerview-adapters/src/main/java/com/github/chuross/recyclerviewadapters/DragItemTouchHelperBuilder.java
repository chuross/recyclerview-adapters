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
    private int dragFlags;

    public DragItemTouchHelperBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter) {
        this(recyclerAdapter, UP | DOWN);
    }

    public DragItemTouchHelperBuilder(@NonNull CompositeRecyclerAdapter recyclerAdapter, int dragFlags) {
        checkNonNull(recyclerAdapter);
        this.recyclerAdapter = recyclerAdapter;
        this.dragFlags = dragFlags;
    }

    public DragItemTouchHelperBuilder register(Class<? extends LocalAdapter> clazz) {
        acceptedClasses.add(clazz);
        return this;
    }

    public ItemTouchHelper build() {
        return new ItemTouchHelper(new VerticalListDragCallback(recyclerAdapter, acceptedClasses, dragFlags));
    }

    private static class VerticalListDragCallback extends ItemTouchHelper.Callback {

        private CompositeRecyclerAdapter recyclerAdapter;
        private List<Class<? extends LocalAdapter>> acceptedClasses;
        private int dragFlags;

        private VerticalListDragCallback(CompositeRecyclerAdapter recyclerAdapter, List<Class<? extends LocalAdapter>> acceptedClasses, int dragFlags) {
            this.recyclerAdapter = recyclerAdapter;
            this.acceptedClasses = acceptedClasses;
            this.dragFlags = dragFlags;
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
