package com.github.panpf.assemblyadapter.recycler.internal

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyItem

class AssemblyItemViewHolderWrapper<DATA>(val wrappedItem: AssemblyItem<DATA>) :
    RecyclerView.ViewHolder(wrappedItem.itemView)