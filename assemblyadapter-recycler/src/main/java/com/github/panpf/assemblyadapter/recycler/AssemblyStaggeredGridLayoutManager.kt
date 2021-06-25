package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import kotlin.reflect.KClass

class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager,
    FullSpanStaggeredGridLayoutManager {

    private val fullSpanItemFactoryList: List<KClass<out ItemFactory>>

    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory>>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    constructor(
        spanCount: Int, orientation: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory>>
    ) : super(spanCount, orientation) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    constructor(
        spanCount: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory>>
    ) : super(spanCount, VERTICAL) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    override fun setFullSpan(itemView: View, itemFactory: AssemblyItemFactory<*>) {
        val layoutParams = itemView.layoutParams
        if (layoutParams is LayoutParams && fullSpanItemFactoryList.contains(itemFactory::class)) {
            layoutParams.isFullSpan = true
        }
    }
}