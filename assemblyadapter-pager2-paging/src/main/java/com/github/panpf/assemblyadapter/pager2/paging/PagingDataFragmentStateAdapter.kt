/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.pager2.paging

import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.paging.LoadType.REFRESH
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [Fragment] version of [PagingDataAdapter]
 *
 * @see PagingDataAdapter
 */
abstract class PagingDataFragmentStateAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * Track whether developer called [setStateRestorationPolicy] or not to decide whether the
     * automated state restoration should apply or not.
     */
    private var userSetRestorationPolicy = false

    private val differ = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = AdapterListUpdateCallback(this),
        mainDispatcher = mainDispatcher,
        workerDispatcher = workerDispatcher
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [PagingDataFragmentStateAdapter]
     */
    constructor(
        fragmentActivity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        diffCallback,
        mainDispatcher,
        workerDispatcher
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [PagingDataFragmentStateAdapter]
     */
    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        diffCallback,
        mainDispatcher,
        workerDispatcher
    )

    override fun setStateRestorationPolicy(strategy: StateRestorationPolicy) {
        userSetRestorationPolicy = true
        super.setStateRestorationPolicy(strategy)
    }

    init {
        // Wait on state restoration until the first insert event.
        super.setStateRestorationPolicy(StateRestorationPolicy.PREVENT)

        fun considerAllowingStateRestoration() {
            if (stateRestorationPolicy == StateRestorationPolicy.PREVENT && !userSetRestorationPolicy) {
                this@PagingDataFragmentStateAdapter.stateRestorationPolicy =
                    StateRestorationPolicy.ALLOW
            }
        }

        // Watch for adapter insert before triggering state restoration. This is almost redundant
        // with loadState below, but can handle cached case.
        @Suppress("LeakingThis")
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                considerAllowingStateRestoration()
                unregisterAdapterDataObserver(this)
                super.onItemRangeInserted(positionStart, itemCount)
            }
        })

        // Watch for loadState update before triggering state restoration. This is almost
        // redundant with data observer above, but can handle empty page case.
        addLoadStateListener(object : Function1<CombinedLoadStates, Unit> {
            // Ignore the first event we get, which is always the initial state, since we only
            // want to observe for Insert events.
            private var ignoreNextEvent = true

            override fun invoke(loadStates: CombinedLoadStates) {
                if (ignoreNextEvent) {
                    ignoreNextEvent = false
                } else if (loadStates.source.refresh is LoadState.NotLoading) {
                    considerAllowingStateRestoration()
                    removeLoadStateListener(this)
                }
            }
        })
    }

    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [PagingDataFragmentStateAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [PagingDataFragmentStateAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

//    /**
//     * Stable ids are unsupported by [PagingFragmentStateAdapter]. Calling this method is an error and will
//     * result in an [UnsupportedOperationException].
//     *
//     * @param hasStableIds Whether items in data set have unique identifiers or not.
//     *
//     * @throws UnsupportedOperationException Always thrown, since this is unsupported by
//     * [PagingFragmentStateAdapter].
//     */
//    final override fun setHasStableIds(hasStableIds: Boolean) {
//        throw UnsupportedOperationException("Stable ids are unsupported on PagingFragmentStateAdapter.")
//    }

    /**
     * Present a [PagingData] until it is invalidated by a call to [refresh] or
     * [PagingSource.invalidate].
     *
     * [submitData] should be called on the same [CoroutineDispatcher] where updates will be
     * dispatched to UI, typically [Dispatchers.Main] (this is done for you if you use
     * `lifecycleScope.launch {}`).
     *
     * This method is typically used when collecting from a [Flow] produced by [Pager]. For RxJava
     * or LiveData support, use the non-suspending overload of [submitData], which accepts a
     * [Lifecycle].
     *
     * Note: This method suspends while it is actively presenting page loads from a [PagingData],
     * until the [PagingData] is invalidated. Although cancellation will propagate to this call
     * automatically, collecting from a [Pager.flow] with the intention of presenting the most
     * up-to-date representation of your backing dataset should typically be done using
     * [collectLatest][kotlinx.coroutines.flow.collectLatest].
     *
     * @see [Pager]
     */
    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    /**
     * Present a [PagingData] until it is either invalidated or another call to [submitData] is
     * made.
     *
     * This method is typically used when observing a RxJava or LiveData stream produced by [Pager].
     * For [Flow] support, use the suspending overload of [submitData], which automates cancellation
     * via [CoroutineScope][kotlinx.coroutines.CoroutineScope] instead of relying of [Lifecycle].
     *
     * @see submitData
     * @see [Pager]
     */
    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<T>) {
        differ.submitData(lifecycle, pagingData)
    }

    /**
     * Retry any failed load requests that would result in a [LoadState.Error] update to this
     * [PagingDataFragmentStateAdapter].
     *
     * Unlike [refresh], this does not invalidate [PagingSource], it only retries failed loads
     * within the same generation of [PagingData].
     *
     * [LoadState.Error] can be generated from two types of load requests:
     *  * [PagingSource.load] returning [PagingSource.LoadResult.Error]
     *  * [RemoteMediator.load] returning [RemoteMediator.MediatorResult.Error]
     */
    fun retry() {
        differ.retry()
    }

    /**
     * Refresh the data presented by this [PagingDataFragmentStateAdapter].
     *
     * [refresh] triggers the creation of a new [PagingData] with a new instance of [PagingSource]
     * to represent an updated snapshot of the backing dataset. If a [RemoteMediator] is set,
     * calling [refresh] will also trigger a call to [RemoteMediator.load] with [LoadType] [REFRESH]
     * to allow [RemoteMediator] to check for updates to the dataset backing [PagingSource].
     *
     * Note: This API is intended for UI-driven refresh signals, such as swipe-to-refresh.
     * Invalidation due repository-layer signals, such as DB-updates, should instead use
     * [PagingSource.invalidate].
     *
     * @see PagingSource.invalidate
     */
    fun refresh() {
        differ.refresh()
    }

    /**
     * Returns the presented item at the specified position, notifying Paging of the item access to
     * trigger any loads necessary to fulfill [prefetchDistance][PagingConfig.prefetchDistance].
     *
     * @param position Index of the presented item to return, including placeholders.
     * @return The presented item at [position], `null` if it is a placeholder
     */
    protected fun getItem(@IntRange(from = 0) position: Int) = differ.getItem(position)

    /**
     * Returns the presented item at the specified position, without notifying Paging of the item
     * access that would normally trigger page loads.
     *
     * @param index Index of the presented item to return, including placeholders.
     * @return The presented item at position [index], `null` if it is a placeholder.
     */
    fun peek(@IntRange(from = 0) index: Int) = differ.peek(index)

    /**
     * Returns a new [ItemSnapshotList] representing the currently presented items, including any
     * placeholders if they are enabled.
     */
    fun snapshot(): ItemSnapshotList<T> = differ.snapshot()

    override fun getItemCount() = differ.itemCount

    /**
     * A hot [Flow] of [CombinedLoadStates] that emits a snapshot whenever the loading state of the
     * current [PagingData] changes.
     *
     * This flow is conflated, so it buffers the last update to [CombinedLoadStates] and
     * immediately delivers the current load states on collection.
     */
    val loadStateFlow: Flow<CombinedLoadStates>
        get() = differ.loadStateFlow

    /**
     * Add a [CombinedLoadStates] listener to observe the loading state of the current [PagingData].
     *
     * As new [PagingData] generations are submitted and displayed, the listener will be notified to
     * reflect the current [CombinedLoadStates].
     *
     * @param listener [LoadStates] listener to receive updates.
     *
     * @see removeLoadStateListener
     */
    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.addLoadStateListener(listener)
    }

    /**
     * Remove a previously registered [CombinedLoadStates] listener.
     *
     * @param listener Previously registered listener.
     * @see addLoadStateListener
     */
    fun removeLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.removeLoadStateListener(listener)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateFragmentStateAdapter]s displaying the
     * [LoadType.PREPEND] [LoadState] as a list item at the end of the presented list.
     *
     * @see LoadStateFragmentStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    fun withLoadStateHeader(header: LoadStateFragmentStateAdapter): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
        }
        return ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            header, this
        )
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateFragmentStateAdapter]s displaying the
     * [LoadType.APPEND] [LoadState] as a list item at the start of the presented list.
     *
     * @see LoadStateFragmentStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    fun withLoadStateFooter(footer: LoadStateFragmentStateAdapter): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            this, footer
        )
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateFragmentStateAdapter]s displaying the
     * [LoadType.PREPEND] and [LoadType.APPEND] [LoadState]s as list items at the start and end
     * respectively.
     *
     * @see LoadStateFragmentStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: LoadStateFragmentStateAdapter,
        footer: LoadStateFragmentStateAdapter
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            header, this, footer
        )
    }
}