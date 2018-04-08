package me.panpf.adapter.sample.itemfactory

import android.view.View
import android.view.ViewGroup
import me.panpf.adapter.more.AssemblyLoadMoreItem
import me.panpf.adapter.more.AssemblyLoadMoreItemFactory
import me.panpf.adapter.more.OnLoadMoreListener

import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bindView

class LoadMoreItemFactory(eventListenerList: OnLoadMoreListener) : AssemblyLoadMoreItemFactory<AssemblyLoadMoreItem<Int>>(eventListenerList) {

    override fun createAssemblyItem(parent: ViewGroup): LoadMoreItem {
        return LoadMoreItem(this, R.layout.list_item_load_more, parent)
    }

    inner class LoadMoreItem(itemFactory: LoadMoreItemFactory, itemLayoutId: Int, parent: ViewGroup)
        : AssemblyLoadMoreItem<Int>(itemFactory, itemLayoutId, parent) {
        private val loadingView: View by bindView(R.id.text_loadMoreListItem_loading)
        private val errorView: View by bindView(R.id.text_loadMoreListItem_error)
        private val endView: View by bindView(R.id.text_loadMoreListItem_end)

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
    }
}
