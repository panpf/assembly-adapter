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
package com.github.panpf.assemblyadapter.list

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.list.ConcatListAdapter.Config
import com.github.panpf.assemblyadapter.list.ConcatListAdapter.Config.StableIdMode
import com.github.panpf.assemblyadapter.list.internal.ConcatListAdapterController
import java.util.*

/**
 * An [BaseAdapter] implementation that presents the contents of multiple adapters in sequence.
 *
 * @param config   The configuration for this ConcatListAdapter
 * @param adapters The list of adapters to add
 * @see Config.Builder
 */
open class ConcatListAdapter(config: Config, adapters: List<BaseAdapter>) : BaseAdapter() {

    companion object {
        const val NO_ID: Long = -1
        const val TAG = "ConcatListAdapter"
    }

    /**
     * Bulk of the logic is in the controller to keep this class isolated to the public API.
     */
    @Suppress("LeakingThis")
    private val mController = ConcatListAdapterController(this, config, adapters)

    /**
     * Returns an unmodifiable copy of the list of adapters in this [ConcatListAdapter].
     * Note that this is a copy hence future changes in the ConcatListAdapter are not reflected in
     * this list.
     *
     * @return A copy of the list of adapters in this ConcatListAdapter.
     */
    val adapters: List<BaseAdapter>
        get() = Collections.unmodifiableList(mController.copyOfAdapters)

    /**
     * Creates a ConcatListAdapter with [Config.DEFAULT] and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(adapters: List<BaseAdapter>) : this(Config.DEFAULT, adapters)

    /**
     * Creates a ConcatListAdapter with the given config and the given adapters in the given order.
     *
     * @param config   The configuration for this ConcatListAdapter
     * @param adapters The list of adapters to add
     * @see Config.Builder
     */
    constructor(config: Config, vararg adapters: BaseAdapter) : this(config, adapters.toList())

    /**
     * Creates a ConcatListAdapter with [Config.DEFAULT] and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(vararg adapters: BaseAdapter) : this(Config.DEFAULT, adapters.toList())

    /**
     * Appends the given adapter to the existing list of adapters and notifies the observers of
     * this [ConcatListAdapter].
     *
     * @param adapter The new adapter to add
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(adapter: BaseAdapter): Boolean {
        return mController.addAdapter(adapter)
    }

    /**
     * Adds the given adapter to the given index among other adapters that are already added.
     *
     * @param index   The index into which to insert the adapter. ConcatListAdapter will throw an
     * [IndexOutOfBoundsException] if the index is not between 0 and current
     * adapter count (inclusive).
     * @param adapter The new adapter to add to the adapters list.
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(index: Int, adapter: BaseAdapter): Boolean {
        return mController.addAdapter(index, adapter)
    }

    /**
     * Removes the given adapter from the adapters list if it exists
     *
     * @param adapter The adapter to remove
     * @return `true` if the adapter was previously added to this `ConcatListAdapter` and
     * now removed or `false` if it couldn't be found.
     */
    open fun removeAdapter(adapter: BaseAdapter): Boolean {
        return mController.removeAdapter(adapter)
    }

    override fun getViewTypeCount(): Int {
        return mController.getItemViewTypeCount()
    }

    override fun getItemViewType(position: Int): Int {
        return mController.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return mController.getItemId(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return mController.getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return mController.totalCount
    }

    override fun getItem(position: Int): Any? {
        return mController.getItem(position)
    }

    override fun hasStableIds(): Boolean {
        return mController.hasStableIds()
    }

    open fun findLocalAdapterAndPosition(position: Int): Pair<BaseAdapter, Int> {
        return mController.findLocalAdapterAndPosition(position)
    }

    /**
     * The configuration object for a [ConcatListAdapter].
     */
    class Config internal constructor(
        /**
         * If `false`, [ConcatListAdapter] assumes all assigned adapters share a global
         * view type pool such that they use the same view types to refer to the same convertView.
         *
         *
         * Setting this to `false` will allow nested adapters to share convertViews but
         * it also means these adapters should not have conflicting view types
         * ([BaseAdapter.getItemViewType]) such that two different adapters return the same
         * view type for different convertViews.
         *
         *
         * By default, it is set to `true` which means [ConcatListAdapter] will isolate
         * view types across adapters, preventing them from using the same convertViews.
         */
        val isolateViewTypes: Boolean,

        /**
         * Defines whether the [ConcatListAdapter] should support stable ids or not
         * ([BaseAdapter.hasStableIds].
         *
         *
         * There are 3 possible options:
         *
         *
         * [StableIdMode.NO_STABLE_IDS]: In this mode, [ConcatListAdapter] ignores the
         * stable
         * ids reported by sub adapters. This is the default mode.
         *
         *
         * [StableIdMode.ISOLATED_STABLE_IDS]: In this mode, [ConcatListAdapter] will return
         * `true` from [ConcatListAdapter.hasStableIds] and will **require** all added
         * [BaseAdapter]s to have stable ids. As two different adapters may return same stable ids
         * because they are unaware of each-other, [ConcatListAdapter] will isolate each
         * [BaseAdapter]'s id pool from each other such that it will overwrite the reported stable
         * id before reporting back to the [android.widget.ListView].
         *
         *
         * [StableIdMode.SHARED_STABLE_IDS]: In this mode, [ConcatListAdapter] will return
         * `true` from [ConcatListAdapter.hasStableIds] and will **require** all added
         * [BaseAdapter]s to have stable ids. Unlike [StableIdMode.ISOLATED_STABLE_IDS],
         * [ConcatListAdapter] will not override the returned item ids. In this mode,
         * child [BaseAdapter]s must be aware of each-other and never return the same id unless
         * an item is moved between [BaseAdapter]s.
         *
         *
         * Default value is [StableIdMode.NO_STABLE_IDS].
         */
        val stableIdMode: StableIdMode
    ) {

        companion object {
            /**
             * Default configuration for [ConcatListAdapter] where [isolateViewTypes]
             * is set to `true` and [stableIdMode] is set to
             * [StableIdMode.NO_STABLE_IDS].
             */
            val DEFAULT = Config(true, StableIdMode.NO_STABLE_IDS)
        }

        /**
         * Defines how [ConcatListAdapter] handle stable ids ([BaseAdapter.hasStableIds]).
         */
        enum class StableIdMode {
            /**
             * In this mode, [ConcatListAdapter] ignores the stable
             * ids reported by sub adapters. This is the default mode.
             * Adding an [BaseAdapter] with stable ids will result in a warning as it will be
             * ignored.
             */
            NO_STABLE_IDS,

            /**
             * In this mode, [ConcatListAdapter] will return `true` from
             * [ConcatListAdapter.hasStableIds] and will **require** all added
             * [BaseAdapter]s to have stable ids. As two different adapters may return
             * same stable ids because they are unaware of each-other, [ConcatListAdapter] will
             * isolate each [BaseAdapter]'s id pool from each other such that it will overwrite
             * the reported stable id before reporting back to the [android.widget.ListView]. In this
             * mode, the value returned from [BaseAdapter.getItemId] might differ from the
             * value returned from [BaseAdapter.getItemId].
             *
             *
             * Adding an adapter without stable ids will result in an
             * [IllegalArgumentException].
             */
            ISOLATED_STABLE_IDS,

            /**
             * In this mode, [ConcatListAdapter] will return `true` from
             * [ConcatListAdapter.hasStableIds] and will **require** all added
             * [BaseAdapter]s to have stable ids. Unlike [StableIdMode.ISOLATED_STABLE_IDS],
             * [ConcatListAdapter] will not override the returned item ids. In this mode,
             * child [BaseAdapter]s must be aware of each-other and never return the same id
             * unless and item is moved between [BaseAdapter]s.
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
             * Sets whether [ConcatListAdapter] should isolate view types of nested adapters from
             * each other.
             *
             * @param isolateViewTypes `true` if [ConcatListAdapter] should override view
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
             * Sets how the [ConcatListAdapter] should handle stable ids
             * ([BaseAdapter.hasStableIds]). See documentation in [stableIdMode]
             * for details.
             *
             * @param stableIdMode The stable id mode for the [ConcatListAdapter]. Defaults to
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