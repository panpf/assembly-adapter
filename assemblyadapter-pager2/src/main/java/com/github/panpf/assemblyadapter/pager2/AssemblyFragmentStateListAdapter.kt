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
package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import kotlin.reflect.KClass

/**
 * An implementation of [FragmentStateListAdapter], which implements multi-type adapters through standardized [FragmentItemFactory].
 * [AssemblyFragmentStateListAdapter] will use the data corresponding to position to find a matching [FragmentItemFactory] (cannot find an exception will be thrown),
 * and then use [FragmentItemFactory] to create an [Fragment]
 *
 * @see FragmentItemFactory
 */
open class AssemblyFragmentStateListAdapter<DATA> : FragmentStateListAdapter<DATA>,
    AssemblyAdapter<DATA, FragmentItemFactory<out Any>> {

    private val itemFactoryStorage: ItemFactoryStorage<FragmentItemFactory<out Any>>
    private var recyclerView: RecyclerView? = null
    private val concatAdapterAbsoluteHelper = ConcatAdapterAbsoluteHelper()

    /**
     * Use [FragmentManager] and [Lifecycle] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : super(fragmentManager, lifecycle, diffCallback) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(
            itemFactoryList,
            "FragmentItemFactory",
            "AssemblyFragmentStateListAdapter",
            "itemFactoryList"
        )
        submitList(initDataList)
    }

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        initDataList,
        diffCallback
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        initDataList,
        diffCallback
    )

    /**
     * Use [FragmentManager] and [Lifecycle] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        config: AsyncDifferConfig<DATA>
    ) : super(fragmentManager, lifecycle, config) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(
            itemFactoryList,
            "FragmentItemFactory",
            "AssemblyFragmentStateListAdapter",
            "itemFactoryList"
        )
        submitList(initDataList)
    }

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        config: AsyncDifferConfig<DATA>
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        initDataList,
        config
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblyFragmentStateListAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        config: AsyncDifferConfig<DATA>
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        initDataList,
        config
    )

    fun getItemData(position: Int): DATA {
        return getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun createFragment(position: Int): Fragment {
        val data = getItemData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter != null) {
            concatAdapterAbsoluteHelper.findAbsoluteAdapterPosition(parentAdapter, this, position)
        } else {
            bindingAdapterPosition
        }

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(data) as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<Any> {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data) as FragmentItemFactory<Any>
    }

    override fun getItemFactoryByData(data: DATA): FragmentItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data ?: Placeholder) as FragmentItemFactory<Any>
    }

    override fun <T : FragmentItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: KClass<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass.java)
    }

    override fun <T : FragmentItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }
}