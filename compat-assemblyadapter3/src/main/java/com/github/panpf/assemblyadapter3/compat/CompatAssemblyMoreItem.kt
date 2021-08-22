package com.github.panpf.assemblyadapter3.compat

import android.content.Context
import android.view.View
import android.view.ViewGroup

abstract class CompatAssemblyMoreItem : CompatAssemblyItem<CompatMoreState> {

    private var itemFactory: CompatAssemblyMoreItemFactory

    constructor(itemFactory: CompatAssemblyMoreItemFactory, itemLayoutId: Int, parent: ViewGroup)
            : super(itemLayoutId, parent) {
        this.itemFactory = itemFactory
    }

    constructor(
        itemFactory: CompatAssemblyMoreItemFactory,
        convertView: View
    ) : super(convertView) {
        this.itemFactory = itemFactory
    }

    protected abstract fun getErrorRetryView(): View?
    protected abstract fun showEnd()
    protected abstract fun showLoading()
    protected abstract fun showErrorRetry()

    public override fun onConfigViews(context: Context) {
        getErrorRetryView()?.setOnClickListener {
            itemFactory.startLoad()
        }
    }

    public override fun onSetData(position: Int, data: CompatMoreState?) {
        when (data) {
            CompatMoreState.END -> {
                showEnd()
            }
            CompatMoreState.ERROR -> {
                showErrorRetry()
            }
            CompatMoreState.LOADING -> {
                showLoading()
            }
            else -> {
                showLoading()
                itemFactory.startLoad()
            }
        }
    }
}