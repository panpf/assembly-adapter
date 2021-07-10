/*
 * Copyright 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.list.concat.expandable

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.list.concat.expandable.ConcatExpandableListAdapter.Config
import com.github.panpf.assemblyadapter.list.concat.expandable.ConcatExpandableListAdapter.Config.StableIdMode
import java.util.*

/**
 * Creates a ConcatExpandableListAdapter with the given config and the given adapters in the given order.
 *
 * @param config   The configuration for this ConcatExpandableListAdapter
 * @param adapters The list of adapters to add
 * @see Config.Builder
 */
open class ConcatExpandableListAdapter(config: Config, adapters: List<BaseExpandableListAdapter>) :
    BaseExpandableListAdapter() {

    companion object {
        const val NO_ID: Long = -1
        const val TAG = "ConcatExpandableListAdapter"
    }

    /**
     * Bulk of the logic is in the controller to keep this class isolated to the public API.
     */
    private val mController: ConcatExpandableListAdapterController =
        ConcatExpandableListAdapterController(this, config)

    /**
     * Returns an unmodifiable copy of the list of adapters in this [ConcatExpandableListAdapter].
     * Note that this is a copy hence future changes in the ConcatExpandableListAdapter are not reflected in
     * this list.
     *
     * @return A copy of the list of adapters in this ConcatExpandableListAdapter.
     */
    val adapters: List<BaseExpandableListAdapter>
        get() = Collections.unmodifiableList(mController.copyOfAdapters)

    /**
     * Creates a ConcatExpandableListAdapter with [Config.DEFAULT] and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(adapters: List<BaseExpandableListAdapter>) : this(Config.DEFAULT, adapters)

    /**
     * Creates a ConcatExpandableListAdapter with the given config and the given adapters in the given order.
     *
     * @param config   The configuration for this ConcatExpandableListAdapter
     * @param adapters The list of adapters to add
     * @see Config.Builder
     */
    constructor(config: Config, vararg adapters: BaseExpandableListAdapter) : this(
        config,
        adapters.toList()
    )

    /**
     * Creates a ConcatExpandableListAdapter with [Config.DEFAULT] and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(vararg adapters: BaseExpandableListAdapter) : this(
        Config.DEFAULT,
        adapters.toList()
    )

    init {
        for (adapter in adapters) {
            addAdapter(adapter)
        }
    }

    /**
     * Appends the given adapter to the existing list of adapters and notifies the observers of
     * this [ConcatExpandableListAdapter].
     *
     * @param adapter The new adapter to add
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(adapter: BaseExpandableListAdapter): Boolean {
        return mController.addAdapter(adapter)
    }

    /**
     * Adds the given adapter to the given index among other adapters that are already added.
     *
     * @param index   The index into which to insert the adapter. ConcatExpandableListAdapter will throw an
     * [IndexOutOfBoundsException] if the index is not between 0 and current
     * adapter count (inclusive).
     * @param adapter The new adapter to add to the adapters list.
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(index: Int, adapter: BaseExpandableListAdapter): Boolean {
        return mController.addAdapter(index, adapter)
    }

    /**
     * Removes the given adapter from the adapters list if it exists
     *
     * @param adapter The adapter to remove
     * @return `true` if the adapter was previously added to this `ConcatExpandableListAdapter` and
     * now removed or `false` if it couldn't be found.
     */
    open fun removeAdapter(adapter: BaseExpandableListAdapter): Boolean {
        return mController.removeAdapter(adapter)
    }

    override fun getGroupCount(): Int {
        return mController.groupCount
    }

    override fun getGroup(groupPosition: Int): Any? {
        return mController.getGroup(groupPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return mController.getGroupId(groupPosition)
    }

    override fun getGroupTypeCount(): Int {
        return mController.groupTypeCount
    }

    override fun getGroupType(groupPosition: Int): Int {
        return mController.getGroupType(groupPosition)
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        return mController.getGroupView(groupPosition, isExpanded, convertView, parent)
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mController.getChildrenCount(groupPosition)
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return mController.getChild(groupPosition, childPosition)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return mController.getChildId(groupPosition, childPosition)
    }

    override fun getChildTypeCount(): Int {
        return mController.childTypeCount
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return mController.getChildType(groupPosition, childPosition)
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        return mController.getChildView(
            groupPosition, childPosition, isLastChild, convertView, parent
        )
    }

    override fun hasStableIds(): Boolean {
        return mController.hasStableIds()
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return mController.isChildSelectable(groupPosition, childPosition)
    }

    override fun onGroupCollapsed(groupPosition: Int) {
        mController.onGroupCollapsed(groupPosition)
    }

    override fun onGroupExpanded(groupPosition: Int) {
        mController.onGroupExpanded(groupPosition)
    }

    open fun findLocalAdapterAndPosition(position: Int): Pair<BaseExpandableListAdapter, Int> {
        return mController.findLocalAdapterAndPosition(position)
    }

    /**
     * The configuration object for a [ConcatExpandableListAdapter].
     */
    class Config internal constructor(
        /**
         * If `false`, [ConcatExpandableListAdapter] assumes all assigned adapters share a global
         * view type pool such that they use the same view types to refer to the same convertView.
         *
         *
         * Setting this to `false` will allow nested adapters to share convertViews but
         * it also means these adapters should not have conflicting view types
         * ([BaseExpandableListAdapter.getGroupType] or [BaseExpandableListAdapter.getChildType]) such that two different adapters return the same
         * view type for different convertViews.
         *
         *
         * By default, it is set to `true` which means [ConcatExpandableListAdapter] will isolate
         * view types across adapters, preventing them from using the same convertViews.
         */
        val isolateViewTypes: Boolean,
        /**
         * Defines whether the [ConcatExpandableListAdapter] should support stable ids or not
         * ([BaseExpandableListAdapter.hasStableIds].
         *
         *
         * There are 3 possible options:
         *
         *
         * [StableIdMode.NO_STABLE_IDS]: In this mode, [ConcatExpandableListAdapter] ignores the
         * stable
         * ids reported by sub adapters. This is the default mode.
         *
         *
         * [StableIdMode.ISOLATED_STABLE_IDS]: In this mode, [ConcatExpandableListAdapter] will return
         * `true` from [ConcatExpandableListAdapter.hasStableIds] and will **require** all added
         * [BaseExpandableListAdapter]s to have stable ids. As two different adapters may return same stable ids
         * because they are unaware of each-other, [ConcatExpandableListAdapter] will isolate each
         * [BaseExpandableListAdapter]'s id pool from each other such that it will overwrite the reported stable
         * id before reporting back to the [android.widget.ExpandableListView].
         *
         *
         * [StableIdMode.SHARED_STABLE_IDS]: In this mode, [ConcatExpandableListAdapter] will return
         * `true` from [ConcatExpandableListAdapter.hasStableIds] and will **require** all added
         * [BaseExpandableListAdapter]s to have stable ids. Unlike [StableIdMode.ISOLATED_STABLE_IDS],
         * [ConcatExpandableListAdapter] will not override the returned item ids. In this mode,
         * child [BaseExpandableListAdapter]s must be aware of each-other and never return the same id unless
         * an item is moved between [BaseExpandableListAdapter]s.
         *
         *
         * Default value is [StableIdMode.NO_STABLE_IDS].
         */
        val stableIdMode: StableIdMode
    ) {

        companion object {
            /**
             * Default configuration for [ConcatExpandableListAdapter] where [isolateViewTypes]
             * is set to `true` and [stableIdMode] is set to
             * [StableIdMode.NO_STABLE_IDS].
             */
            val DEFAULT = Config(true, StableIdMode.NO_STABLE_IDS)
        }

        /**
         * Defines how [ConcatExpandableListAdapter] handle stable ids ([BaseExpandableListAdapter.hasStableIds]).
         */
        enum class StableIdMode {
            /**
             * In this mode, [ConcatExpandableListAdapter] ignores the stable
             * ids reported by sub adapters. This is the default mode.
             * Adding an [BaseExpandableListAdapter] with stable ids will result in a warning as it will be
             * ignored.
             */
            NO_STABLE_IDS,

            /**
             * In this mode, [ConcatExpandableListAdapter] will return `true` from
             * [ConcatExpandableListAdapter.hasStableIds] and will **require** all added
             * [BaseExpandableListAdapter]s to have stable ids. As two different adapters may return
             * same stable ids because they are unaware of each-other, [ConcatExpandableListAdapter] will
             * isolate each [BaseExpandableListAdapter]'s id pool from each other such that it will overwrite
             * the reported stable id before reporting back to the [android.widget.ExpandableListView]. In this
             * mode, the value returned from ([BaseExpandableListAdapter.getGroupId] or [BaseExpandableListAdapter.getChildId]) might differ from the
             * value returned from ([BaseExpandableListAdapter.getGroupId] or [BaseExpandableListAdapter.getChildId]).
             *
             *
             * Adding an adapter without stable ids will result in an
             * [IllegalArgumentException].
             */
            ISOLATED_STABLE_IDS,

            /**
             * In this mode, [ConcatExpandableListAdapter] will return `true` from
             * [ConcatExpandableListAdapter.hasStableIds] and will **require** all added
             * [BaseExpandableListAdapter]s to have stable ids. Unlike [StableIdMode.ISOLATED_STABLE_IDS],
             * [ConcatExpandableListAdapter] will not override the returned item ids. In this mode,
             * child [BaseExpandableListAdapter]s must be aware of each-other and never return the same id
             * unless and item is moved between [BaseExpandableListAdapter]s.
             * Adding an adapter without stable ids will result in an
             * [IllegalArgumentException].
             */
            SHARED_STABLE_IDS
        }

        /**
         * The builder for [Config] class.
         */
        class Builder {
            private var mIsolateViewTypes = DEFAULT.isolateViewTypes
            private var mStableIdMode = DEFAULT.stableIdMode

            /**
             * Sets whether [ConcatExpandableListAdapter] should isolate view types of nested adapters from
             * each other.
             *
             * @param isolateViewTypes `true` if [ConcatExpandableListAdapter] should override view
             * types of nested adapters to avoid view type
             * conflicts, `false` otherwise.
             * Defaults to [Config.DEFAULT]'s
             * [isolateViewTypes] value (`true`).
             * @return this
             * @see isolateViewTypes
             */
            fun setIsolateViewTypes(isolateViewTypes: Boolean): Builder {
                mIsolateViewTypes = isolateViewTypes
                return this
            }

            /**
             * Sets how the [ConcatExpandableListAdapter] should handle stable ids
             * ([BaseExpandableListAdapter.hasStableIds]). See documentation in [stableIdMode]
             * for details.
             *
             * @param stableIdMode The stable id mode for the [ConcatExpandableListAdapter]. Defaults to
             * [Config.DEFAULT]'s [stableIdMode] value
             * ([StableIdMode.NO_STABLE_IDS]).
             * @return this
             * @see stableIdMode
             */
            fun setStableIdMode(stableIdMode: StableIdMode): Builder {
                mStableIdMode = stableIdMode
                return this
            }

            /**
             * @return A new instance of [Config] with the given parameters.
             */
            fun build(): Config {
                return Config(mIsolateViewTypes, mStableIdMode)
            }
        }
    }
}