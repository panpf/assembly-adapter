package com.github.panpf.assemblyadapter3.compat.sample.item

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter3.compat.ktx.bindView
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyMoreItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyMoreItemFactory
import com.github.panpf.assemblyadapter3.compat.CompatOnLoadMoreListener
import com.github.panpf.assemblyadapter3.compat.sample.R

class LoadMoreItem(itemFactory: Factory, itemLayoutId: Int, parent: ViewGroup) :
    CompatAssemblyMoreItem(itemFactory, itemLayoutId, parent) {

    private val loadingView: View by bindView(R.id.moreItem_loadingLayout)
    private val errorView: View by bindView(R.id.moreItem_errorText)
    private val endView: View by bindView(R.id.moreItem_endText)

    override fun getErrorRetryView(): View {
        return errorView
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
        errorView.visibility = View.INVISIBLE
        endView.visibility = View.INVISIBLE
    }

    override fun showErrorRetry() {
        loadingView.visibility = View.INVISIBLE
        errorView.visibility = View.VISIBLE
        endView.visibility = View.INVISIBLE
    }

    override fun showEnd() {
        loadingView.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE
        endView.visibility = View.VISIBLE
    }

    class Factory(listener: CompatOnLoadMoreListener? = null) : CompatAssemblyMoreItemFactory(listener) {

        override fun createAssemblyItem(parent: ViewGroup): CompatAssemblyMoreItem {
            return LoadMoreItem(this, R.layout.item_more, parent)
        }
    }
}
