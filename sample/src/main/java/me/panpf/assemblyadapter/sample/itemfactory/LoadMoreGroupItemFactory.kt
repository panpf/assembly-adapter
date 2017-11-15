package me.panpf.assemblyadapter.sample.itemfactory

import android.view.View
import android.view.ViewGroup

import me.panpf.assemblyadapter.AssemblyLoadMoreGroupItemFactory
import me.panpf.assemblyadapter.OnGroupLoadMoreListener
import me.panpf.assemblyadapter.sample.R
import me.panpf.assemblyadapter.sample.bindView

class LoadMoreGroupItemFactory(eventListener: OnGroupLoadMoreListener) : AssemblyLoadMoreGroupItemFactory(eventListener) {

    override fun createAssemblyItem(parent: ViewGroup): AssemblyLoadMoreGroupItemFactory.AssemblyLoadMoreGroupItem<*> {
        return LoadMoreGroupItem(R.layout.list_item_load_more, parent)
    }

    inner class LoadMoreGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyLoadMoreGroupItemFactory.AssemblyLoadMoreGroupItem<Int>(itemLayoutId, parent) {
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
