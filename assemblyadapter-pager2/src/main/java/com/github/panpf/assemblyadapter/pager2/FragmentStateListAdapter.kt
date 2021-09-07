package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * [AsyncListDiffer] version of [FragmentStateAdapter], similar to [ListAdapter]
 */
abstract class FragmentStateListAdapter<DATA> : FragmentStateAdapter {

    val mDiffer: AsyncListDiffer<DATA>
    private val mListener: ListListener<DATA> = ListListener<DATA> { previousList, currentList ->
        onCurrentListChanged(previousList, currentList)
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : super(fragmentManager, lifecycle) {
        mDiffer = AsyncListDiffer<DATA>(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )
        mDiffer.addListListener(mListener)
    }

    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, diffCallback)

    constructor(
        activity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : this(activity.supportFragmentManager, activity.lifecycle, diffCallback)

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        config: AsyncDifferConfig<DATA>
    ) : super(fragmentManager, lifecycle) {
        mDiffer = AsyncListDiffer(AdapterListUpdateCallback(this), config)
        mDiffer.addListListener(mListener)
    }

    constructor(
        fragment: Fragment,
        config: AsyncDifferConfig<DATA>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, config)

    constructor(
        activity: FragmentActivity,
        config: AsyncDifferConfig<DATA>
    ) : this(activity.supportFragmentManager, activity.lifecycle, config)

    /**
     * Submits a new list to be diffed, and displayed.
     *
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    open fun submitList(list: List<DATA>?) {
        mDiffer.submitList(list)
    }

    /**
     * Set the new list to be displayed.
     *
     *
     * If a List is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     *
     * The commit callback can be used to know when the List is committed, but note that it
     * may not be executed. If List B is submitted immediately after List A, and is
     * committed directly, the callback associated with List A will not be run.
     *
     * @param list The new list to be displayed.
     * @param commitCallback Optional runnable that is executed when the List is committed, if
     * it is committed.
     */
    open fun submitList(list: List<DATA>?, commitCallback: Runnable?) {
        mDiffer.submitList(list, commitCallback)
    }

    protected fun getItem(position: Int): DATA {
        return mDiffer.currentList[position]
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [FragmentStateListAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [FragmentStateListAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    /**
     * Get the current List - any diffing to present this list has already been computed and
     * dispatched via the ListUpdateCallback.
     *
     *
     * If a `null` List, or no List has been submitted, an empty list will be returned.
     *
     *
     * The returned list may not be mutated - mutations to content must be done through
     * [.submitList].
     *
     * @return The list currently being displayed.
     *
     * @see .onCurrentListChanged
     */
    val currentList: List<DATA>
        get() = mDiffer.currentList

    /**
     * Called when the current List is updated.
     *
     *
     * If a `null` List is passed to [.submitList], or no List has been
     * submitted, the current List is represented as an empty List.
     *
     * @param previousList List that was displayed previously.
     * @param currentList new List being displayed, will be empty if `null` was passed to
     * [.submitList].
     *
     * @see .getCurrentList
     */
    open fun onCurrentListChanged(previousList: List<DATA>, currentList: List<DATA>) {

    }
}