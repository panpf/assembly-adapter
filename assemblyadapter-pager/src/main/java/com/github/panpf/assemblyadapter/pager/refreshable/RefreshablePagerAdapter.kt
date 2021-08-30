package com.github.panpf.assemblyadapter.pager.refreshable

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.ArrayPagerAdapter

abstract class RefreshablePagerAdapter : GetItemDataPagerAdapter() {

    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper(this)

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [PagerAdapter] will not refresh the item when the dataset changes.
     *
     * [ArrayPagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
     * method return POSITION_NONE when the dataset changes.
     */
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else PagerAdapterRefreshHelper(this)
                notifyDataSetChanged()
            }
        }

    final override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return getView(container, position).apply {
            container.addView(this)
            refreshHelper?.bindPositionAndData(this, position, getItemData(position))
        }
    }

    abstract fun getView(container: ViewGroup, position: Int): View

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as View) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        container.removeView(item as View)
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }
}