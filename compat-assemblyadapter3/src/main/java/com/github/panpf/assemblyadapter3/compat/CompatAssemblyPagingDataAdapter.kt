package com.github.panpf.assemblyadapter3.compat

import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.paging.LoadType.REFRESH
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter3.compat.internal.CompatBaseAssemblyRecyclerAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Copied from PagingDataAdapter v3.0.0
 */
class CompatAssemblyPagingDataAdapter<T : Any> @JvmOverloads constructor(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : CompatBaseAssemblyRecyclerAdapter<T>() {

    /**
     * Track whether developer called [setStateRestorationPolicy] or not to decide whether the
     * automated state restoration should apply or not.
     */
    private var userSetRestorationPolicy = false

    private var pagingDataAdapter: AssemblyPagingDataAdapter<T>? = null
    private val requirePagingDataAdapter: AssemblyPagingDataAdapter<T>
        get() = pagingDataAdapter
            ?: throw IllegalStateException("Please call this method after setAdapter")

    override val dataCount: Int
        get() = pagingDataAdapter?.itemCount ?: 0

    override fun getData(positionInDataList: Int): T? {
        return pagingDataAdapter?.peek(positionInDataList)
    }

    override fun createBodyAdapter(bodyItemFactoryList: List<ItemFactory<*>>): RecyclerView.Adapter<*> {
        return AssemblyPagingDataAdapter(
            bodyItemFactoryList, diffCallback, mainDispatcher, workerDispatcher
        ).apply {
            stateRestorationPolicy = this@CompatAssemblyPagingDataAdapter.stateRestorationPolicy

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

            this@CompatAssemblyPagingDataAdapter.pagingDataAdapter = this
        }
    }

    init {
        // Wait on state restoration until the first insert event.
        super.setStateRestorationPolicy(StateRestorationPolicy.PREVENT)

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
    }

    private fun considerAllowingStateRestoration() {
        if (stateRestorationPolicy == StateRestorationPolicy.PREVENT && !userSetRestorationPolicy) {
            this@CompatAssemblyPagingDataAdapter.stateRestorationPolicy =
                StateRestorationPolicy.ALLOW
        }
    }

    override fun setStateRestorationPolicy(strategy: StateRestorationPolicy) {
        userSetRestorationPolicy = true
        super.setStateRestorationPolicy(strategy)
        pagingDataAdapter?.stateRestorationPolicy = strategy
    }

    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [CompatAssemblyPagingDataAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [CompatAssemblyPagingDataAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    /**
     * Stable ids are unsupported by [CompatAssemblyPagingDataAdapter]. Calling this method is an error and will
     * result in an [UnsupportedOperationException].
     *
     * @param hasStableIds Whether items in data set have unique identifiers or not.
     *
     * @throws UnsupportedOperationException Always thrown, since this is unsupported by
     * [CompatAssemblyPagingDataAdapter].
     */
    final override fun setHasStableIds(hasStableIds: Boolean) {
        throw UnsupportedOperationException("Stable ids are unsupported on AssemblyPagingDataAdapter.")
    }

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
     * @sample androidx.paging.samples.submitDataFlowSample
     *
     * @see [Pager]
     */
    suspend fun submitData(pagingData: PagingData<T>) {
        requirePagingDataAdapter.submitData(pagingData)
    }

    /**
     * Present a [PagingData] until it is either invalidated or another call to [submitData] is
     * made.
     *
     * This method is typically used when observing a RxJava or LiveData stream produced by [Pager].
     * For [Flow] support, use the suspending overload of [submitData], which automates cancellation
     * via [CoroutineScope][kotlinx.coroutines.CoroutineScope] instead of relying of [Lifecycle].
     *
     * @sample androidx.paging.samples.submitDataLiveDataSample
     * @sample androidx.paging.samples.submitDataRxSample
     *
     * @see submitData
     * @see [Pager]
     */
    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<T>) {
        requirePagingDataAdapter.submitData(lifecycle, pagingData)
    }

    /**
     * Retry any failed load requests that would result in a [LoadState.Error] update to this
     * [CompatAssemblyPagingDataAdapter].
     *
     * Unlike [refresh], this does not invalidate [PagingSource], it only retries failed loads
     * within the same generation of [PagingData].
     *
     * [LoadState.Error] can be generated from two types of load requests:
     *  * [PagingSource.load] returning [PagingSource.LoadResult.Error]
     *  * [RemoteMediator.load] returning [RemoteMediator.MediatorResult.Error]
     */
    fun retry() {
        requirePagingDataAdapter.retry()
    }

    /**
     * Refresh the data presented by this [CompatAssemblyPagingDataAdapter].
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
     *
     * @sample androidx.paging.samples.refreshSample
     */
    fun refresh() {
        requirePagingDataAdapter.refresh()
    }

//    /**
//     * Returns the presented item at the specified position, notifying Paging of the item access to
//     * trigger any loads necessary to fulfill [prefetchDistance][PagingConfig.prefetchDistance].
//     *
//     * @param position Index of the presented item to return, including placeholders.
//     * @return The presented item at [position], `null` if it is a placeholder
//     */
//    protected fun getItem(@IntRange(from = 0) position: Int) = differ.getItem(position)

    /**
     * Returns the presented item at the specified position, without notifying Paging of the item
     * access that would normally trigger page loads.
     *
     * @param index Index of the presented item to return, including placeholders.
     * @return The presented item at position [index], `null` if it is a placeholder.
     */
    fun peek(@IntRange(from = 0) index: Int): T? {
        return requirePagingDataAdapter.peek(index)
    }

    /**
     * Returns a new [ItemSnapshotList] representing the currently presented items, including any
     * placeholders if they are enabled.
     */
    fun snapshot(): ItemSnapshotList<T> {
        return requirePagingDataAdapter.snapshot()
    }

//    override fun getItemCount() = differ.itemCount

    /**
     * A hot [Flow] of [CombinedLoadStates] that emits a snapshot whenever the loading state of the
     * current [PagingData] changes.
     *
     * This flow is conflated, so it buffers the last update to [CombinedLoadStates] and
     * immediately delivers the current load states on collection.
     */
    val loadStateFlow: Flow<CombinedLoadStates>
        get() = requirePagingDataAdapter.loadStateFlow

    /**
     * Add a [CombinedLoadStates] listener to observe the loading state of the current [PagingData].
     *
     * As new [PagingData] generations are submitted and displayed, the listener will be notified to
     * reflect the current [CombinedLoadStates].
     *
     * @param listener [LoadStates] listener to receive updates.
     *
     * @see removeLoadStateListener
     * @sample androidx.paging.samples.addLoadStateListenerSample
     */
    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        requirePagingDataAdapter.addLoadStateListener(listener)
    }

    /**
     * Remove a previously registered [CombinedLoadStates] listener.
     *
     * @param listener Previously registered listener.
     * @see addLoadStateListener
     */
    fun removeLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        requirePagingDataAdapter.removeLoadStateListener(listener)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.PREPEND] [LoadState] as a list item at the end of the presented list.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    fun withLoadStateHeader(
        header: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
        }
        return ConcatAdapter(header, this)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.APPEND] [LoadState] as a list item at the start of the presented list.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    fun withLoadStateFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(this, footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.PREPEND] and [LoadType.APPEND] [LoadState]s as list items at the start and end
     * respectively.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: LoadStateAdapter<*>,
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(header, this, footer)
    }

    override var dataList: List<Any?>?
        get() = pagingDataAdapter?.snapshot()
        set(value) {
            throw UnsupportedOperationException("dataList set are unsupported on CompatAssemblyPagingDataAdapter.")
        }

    override fun addAll(collection: Collection<Any?>?) {
        throw UnsupportedOperationException("addAll are unsupported on CompatAssemblyPagingDataAdapter.")
    }

    override fun addAll(vararg items: Any?) {
        throw UnsupportedOperationException("addAll are unsupported on CompatAssemblyPagingDataAdapter.")
    }

    override fun insert(`object`: Any, index: Int) {
        throw UnsupportedOperationException("insert are unsupported on CompatAssemblyPagingDataAdapter.")
    }

    override fun remove(`object`: Any) {
        throw UnsupportedOperationException("remove are unsupported on CompatAssemblyPagingDataAdapter.")
    }

    override fun clear() {
        throw UnsupportedOperationException("clear are unsupported on CompatAssemblyPagingDataAdapter.")
    }

    override fun sort(comparator: Comparator<Any?>) {
        throw UnsupportedOperationException("sort are unsupported on CompatAssemblyPagingDataAdapter.")
    }
}