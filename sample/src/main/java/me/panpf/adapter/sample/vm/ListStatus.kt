package me.panpf.adapter.sample.vm

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList

sealed class ListStatus

class Initialize : ListStatus()
class InitializeSuccess : ListStatus()
class InitializError : ListStatus()
class LoadMore : ListStatus()
class LoadMoreSuccess : ListStatus()
class LoadMoreError : ListStatus()
class Empty : ListStatus()
class End : ListStatus()
class EndAtFront : ListStatus()

class ListBoundaryCallback<Value>(private val listBoundaryLiveData: MutableLiveData<ListStatus>) : PagedList.BoundaryCallback<Value>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        listBoundaryLiveData.value = Empty()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Value) {
        super.onItemAtEndLoaded(itemAtEnd)
        listBoundaryLiveData.value = End()
    }

    override fun onItemAtFrontLoaded(itemAtFront: Value) {
        super.onItemAtFrontLoaded(itemAtFront)
        listBoundaryLiveData.value = EndAtFront()
    }
}
