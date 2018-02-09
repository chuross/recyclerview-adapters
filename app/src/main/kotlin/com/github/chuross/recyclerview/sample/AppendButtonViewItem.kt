package com.github.chuross.recyclerview.sample

import android.content.Context
import android.view.View
import com.github.chuross.recyclerviewadapters.ViewItem

class AppendButtonViewItem(context: Context, clickListener: View.OnClickListener) : ViewItem(context, R.layout.item_append_button, clickListener)