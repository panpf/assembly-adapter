package com.github.panpf.assemblyadapter.recycler.internal

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.Item

class AssemblyItemViewHolderWrapper<DATA>(val wrappedItem: Item<DATA>) :
    RecyclerView.ViewHolder(wrappedItem.itemView)