package com.github.chuross.recyclerview.sample

import android.content.Context
import android.support.annotation.ColorInt
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.chuross.recyclerview.sample.databinding.ViewTextBinding
import com.github.chuross.recyclerviewadapters.ItemAdapter
import com.github.chuross.recyclerviewadapters.databinding.BindingViewHolder

class TextItemAdapter(context: Context, @ColorInt val textColor: Int? = null) : ItemAdapter<String, BindingViewHolder<ViewTextBinding>>(context) {

    override fun getAdapterId(): Int = R.layout.view_text

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingViewHolder<ViewTextBinding> {
        val inflater = LayoutInflater.from(context)
        return BindingViewHolder(ViewTextBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewTextBinding>?, position: Int) {
        val text = get(position)
        holder?.binding?.also {
            it.text = text
            it.textColor = textColor
        }?.executePendingBindings()
    }

}