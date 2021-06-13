package com.github.panpf.assemblyadapter.recycler.internal

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.Item

class AssemblyRecyclerItem<DATA>(private val wrappedItem: Item<DATA>) :
    RecyclerView.ViewHolder(wrappedItem.getItemView()), Item<DATA> {

    override fun dispatchBindData(position: Int, data: DATA?) {
        wrappedItem.dispatchBindData(position, data)
    }

    override fun getData(): DATA? {
        return wrappedItem.getData()
    }

    override fun getItemView(): View {
        return wrappedItem.getItemView()
    }
}