package me.panpf.adapter.sample.itemfactory

import android.view.View
import android.view.ViewGroup

import me.panpf.adapter.AssemblyLoadMoreItemFactory
import me.panpf.adapter.OnLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bindView

class LoadMoreItemFactory(eventListener: OnLoadMoreListener) : AssemblyLoadMoreItemFactory(eventListener) {

    override fun createAssemblyItem(parent: ViewGroup): AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem<*> {
        return LoadMoreItem(R.layout.list_item_load_more, parent)
    }

    inner class LoadMoreItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem<Int>(itemLayoutId, parent) {
        val loadingView: View by bindView(R.id.text_loadMoreListItem_loading)
        val errorView: View by bindView(R.id.text_loadMoreListItem_error)
        val endView: View by bindView(R.id.text_loadMoreListItem_end)

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
