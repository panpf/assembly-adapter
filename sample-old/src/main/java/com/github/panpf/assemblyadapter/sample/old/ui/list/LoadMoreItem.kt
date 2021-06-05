package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.view.View
import android.view.ViewGroup
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.more.AssemblyMoreItem
import me.panpf.adapter.more.AssemblyMoreItemFactory
import me.panpf.adapter.more.OnLoadMoreListener
import com.github.panpf.assemblyadapter.sample.old.R

class LoadMoreItem(itemFactory: Factory, itemLayoutId: Int, parent: ViewGroup)
    : AssemblyMoreItem<Int>(itemFactory, itemLayoutId, parent) {
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

    class Factory(listener: OnLoadMoreListener? = null) : AssemblyMoreItemFactory<Int>(listener) {

        override fun createAssemblyItem(parent: ViewGroup): AssemblyMoreItem<Int> {
            return LoadMoreItem(this, R.layout.item_more, parent)
        }
    }
}
