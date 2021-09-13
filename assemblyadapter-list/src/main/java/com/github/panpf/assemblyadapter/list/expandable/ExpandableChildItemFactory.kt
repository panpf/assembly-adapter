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
package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildClickListenerStorage
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildClickListenerWrapper
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildLongClickListenerWrapper
import kotlin.reflect.KClass

/**
 * Specially used in the [BaseExpandableListAdapter.getChildView] method
 *
 * It is not recommended to directly inherit [ExpandableChildItemFactory],
 * you can inherit [BindingExpandableChildItemFactory] and [SimpleExpandableChildItemFactory] to implement your own ItemFactory
 *
 * @see ExpandableChildItem
 * @see BindingExpandableChildItemFactory
 * @see SimpleExpandableChildItemFactory
 * @see ViewExpandableChildItemFactory
 */
abstract class ExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    dataClass: KClass<CHILD_DATA>
) : ItemFactory<CHILD_DATA>(dataClass) {

    private var childClickListenerStorage: ChildClickListenerStorage<GROUP_DATA, CHILD_DATA>? = null

    final override fun createItem(parent: ViewGroup): Item<CHILD_DATA> {
        return createExpandableChildItem(parent).apply {
            registerChildClickListener(this)
        }
    }

    abstract fun createExpandableChildItem(parent: ViewGroup): ExpandableChildItem<GROUP_DATA, CHILD_DATA>

    /**
     * Set the click listener of the specified View in the child item view
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnChildViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnChildClickListener<GROUP_DATA, CHILD_DATA>
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    /**
     * Set the click listener of the specified View in the child item view
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnChildViewClickListener(
        @IdRes viewId: Int,
        onClickListener: (
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA
        ) -> Unit
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    /**
     * Set the long click listener of the specified View in the child item view
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnChildViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnChildLongClickListener<GROUP_DATA, CHILD_DATA>
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the long click listener of the specified View in the child item view
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnChildViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: (
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA
        ) -> Boolean
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the click listener of the child item view
     *
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnChildItemClickListener(
        onClickListener: OnChildClickListener<GROUP_DATA, CHILD_DATA>
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the click listener of the child item view
     *
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnChildItemClickListener(
        onClickListener: (
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA
        ) -> Unit
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the long click listener of the child item view
     *
     * @param onLongClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnChildItemLongClickListener(
        onLongClickListener: OnChildLongClickListener<GROUP_DATA, CHILD_DATA>
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    /**
     * Set the long click listener of the child item view
     *
     * @param onLongClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnChildItemLongClickListener(
        onLongClickListener: (
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA
        ) -> Boolean
    ): ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ChildClickListenerStorage<GROUP_DATA, CHILD_DATA> {
        return (childClickListenerStorage
            ?: (ChildClickListenerStorage<GROUP_DATA, CHILD_DATA>().apply {
                this@ExpandableChildItemFactory.childClickListenerStorage = this
            }))
    }

    private fun registerChildClickListener(item: ExpandableChildItem<GROUP_DATA, CHILD_DATA>) {
        val clickListenerManager = childClickListenerStorage ?: return
        val itemView = item.itemView
        for (holder in clickListenerManager.holders) {
            if (holder is ChildClickListenerStorage.ClickListenerHolder<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder =
                    holder as ChildClickListenerStorage.ClickListenerHolder<GROUP_DATA, CHILD_DATA>
                val viewId = clickListenerHolder.viewId
                val targetView = if (viewId != -1) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnClickListener(ChildClickListenerWrapper(clickListenerHolder.listener))
            } else if (holder is ChildClickListenerStorage.LongClickListenerHolder<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder =
                    holder as ChildClickListenerStorage.LongClickListenerHolder<GROUP_DATA, CHILD_DATA>
                val viewId = longClickListenerHolder.viewId
                val targetView = if (viewId != -1) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found long click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnLongClickListener(
                    ChildLongClickListenerWrapper(longClickListenerHolder.listener)
                )
            }
        }
    }
}