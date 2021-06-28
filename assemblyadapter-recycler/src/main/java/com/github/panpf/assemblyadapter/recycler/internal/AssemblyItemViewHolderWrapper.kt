package com.github.panpf.assemblyadapter.recycler.internal

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory

open class AssemblyItemViewHolderWrapper<DATA>(val wrappedItem: ItemFactory.Item<DATA>) :
    RecyclerView.ViewHolder(wrappedItem.itemView)