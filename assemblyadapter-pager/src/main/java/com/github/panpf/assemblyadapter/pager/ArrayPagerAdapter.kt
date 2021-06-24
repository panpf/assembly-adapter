/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
package com.github.panpf.assemblyadapter.pager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ArrayPagerAdapter(views: List<View>) : PagerAdapter() {

    private var viewList: List<View> = views.toList()
    private var pageTitleList: List<CharSequence>? = null
    private var refreshHelper: PagerAdapterRefreshHelper? = null

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = refreshHelper != null
        set(enabled) {
            refreshHelper = if (enabled) PagerAdapterRefreshHelper() else null
        }

    override fun getCount(): Int {
        return viewList.size
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return viewList[position].apply {
            container.addView(this)
        }
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleList?.getOrNull(position)
    }


    fun getViewsSnapshot(): List<View> {
        return viewList.toList()
    }

    fun setViews(views: List<View>?) {
        viewList = views?.toList() ?: emptyList()
        notifyDataSetChanged()
    }

    fun getPageTitlesSnapshot(): List<CharSequence> {
        return pageTitleList?.toList() ?: emptyList()
    }

    fun setPageTitles(pageTitles: List<CharSequence>?) {
        pageTitleList = pageTitles?.toList() ?: emptyList()
    }
}