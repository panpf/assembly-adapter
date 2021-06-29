package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class ViewPagerItemFactory<DATA>(
    private val dataClazz: Class<DATA>,
    private val viewFactory: (context: Context, inflater: LayoutInflater, parent: ViewGroup) -> View
) : PagerItemFactory<DATA>() {

    constructor(dataClazz: Class<DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { _, inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: Class<DATA>, view: View) : this(dataClazz, { _, _, _ -> view })

    override fun match(data: Any): Boolean {
        return dataClazz.isInstance(data)
    }

    override fun createItemView(
        context: Context,
        parent: ViewGroup,
        position: Int,
        data: DATA
    ): View = viewFactory(context, LayoutInflater.from(context), parent)
}