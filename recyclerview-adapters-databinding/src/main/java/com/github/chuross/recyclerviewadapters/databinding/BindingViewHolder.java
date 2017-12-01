package com.github.chuross.recyclerviewadapters.databinding;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;


public class BindingViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private B binding;

    public BindingViewHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public B getBinding() {
        return binding;
    }
}
