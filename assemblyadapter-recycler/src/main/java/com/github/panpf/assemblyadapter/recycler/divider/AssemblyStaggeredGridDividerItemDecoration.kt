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
package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.internal.IsFullSpanByItemFactory

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、first or last divider、side divider、fist or last side divider
 *
 * On the basis of [StaggeredGridDividerItemDecoration], the divider can be disabled or personalized according to the ItemFactory class
 */
open class AssemblyStaggeredGridDividerItemDecoration(
    itemDividerProvider: StaggeredGridItemDividerProvider,
    isFullSpanByPosition: IsFullSpanByPosition
) : StaggeredGridDividerItemDecoration(
    itemDividerProvider, isFullSpanByPosition
) {

    class Builder(val context: Context) {

        private var dividerConfig: AssemblyDividerConfig? = null
        private var firstDividerConfig: AssemblyDividerConfig? = null
        private var lastDividerConfig: AssemblyDividerConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var sideDividerConfig: AssemblyDividerConfig? = null
        private var firstSideDividerConfig: AssemblyDividerConfig? = null
        private var lastSideDividerConfig: AssemblyDividerConfig? = null
        private var showFirstSideDivider = false
        private var showLastSideDivider = false

        private var isFullSpanByPosition: IsFullSpanByPosition? = null
        private var findItemFactoryClassByPosition: FindItemFactoryClassByPosition? = null

        fun build(): AssemblyStaggeredGridDividerItemDecoration {
            return AssemblyStaggeredGridDividerItemDecoration(
                buildItemDividerProvider(),
                isFullSpanByPosition ?: AssemblyIsFullSpanByPosition()
            )
        }

        private fun buildItemDividerProvider(): StaggeredGridItemDividerProvider {
            val finalDividerConfig =
                dividerConfig ?: context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    AssemblyDividerConfig.Builder(Divider.drawable(it)).build()
                }

            val finalFindItemFactoryClassByPosition =
                (findItemFactoryClassByPosition ?: AssemblyFindItemFactoryClassByPosition()).run {
                    ConcatFindItemFactoryClassByPosition(this)
                }

            return StaggeredGridItemDividerProvider(
                dividerConfig = finalDividerConfig
                    .toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstDividerConfig = (firstDividerConfig
                    ?: if (showFirstDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastDividerConfig = (lastDividerConfig
                    ?: if (showLastDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideDividerConfig = sideDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstSideDividerConfig = (firstSideDividerConfig
                    ?: if (showFirstSideDivider) sideDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastSideDividerConfig = (lastSideDividerConfig
                    ?: if (showLastSideDivider) sideDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
            )
        }


        /**
         * Set the divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun divider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider of the item
         */
        fun divider(config: AssemblyDividerConfig): Builder {
            this.dividerConfig = config
            return this
        }


        /**
         * Set the first divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun firstDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first divider of the item.
         */
        fun firstDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            return this
        }


        /**
         * Set the last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun lastDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the last divider of the item.
         */
        fun lastDivider(config: AssemblyDividerConfig): Builder {
            this.lastDividerConfig = config
            return this
        }


        /**
         * Set the first and last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun firstAndLastDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first and last divider of the item.
         */
        fun firstAndLastDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            this.lastDividerConfig = config
            return this
        }


        /**
         * Use divider as the first divider.
         */
        fun showFirstDivider(showFirstDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstDivider
            return this
        }

        /**
         * Use divider as the last divider.
         */
        fun showLastDivider(showLastDivider: Boolean = true): Builder {
            this.showLastDivider = showLastDivider
            return this
        }

        /**
         * Use divider as the first and last divider.
         */
        fun showFirstAndLastDivider(showFirstAndLastDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstAndLastDivider
            this.showLastDivider = showFirstAndLastDivider
            return this
        }


        /**
         * Set the divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider on the side of the item
         */
        fun sideDivider(config: AssemblyDividerConfig): Builder {
            this.sideDividerConfig = config
            return this
        }


        /**
         * Set the first divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun firstSideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first divider on the side of the item
         */
        fun firstSideDivider(config: AssemblyDividerConfig): Builder {
            this.firstSideDividerConfig = config
            return this
        }


        /**
         * Set the last divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun lastSideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastSideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastSideDivider(config: AssemblyDividerConfig): Builder {
            this.lastSideDividerConfig = config
            return this
        }


        /**
         * Set the first and last divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun firstAndLastSideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastSideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first and last divider on the side of the item
         */
        fun firstAndLastSideDivider(config: AssemblyDividerConfig): Builder {
            this.firstSideDividerConfig = config
            this.lastSideDividerConfig = config
            return this
        }


        /**
         * Use side divider as the first side divider
         */
        fun showFirstSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            return this
        }

        /**
         * Use side divider as the last side divider
         */
        fun showLastSideDivider(show: Boolean = true): Builder {
            this.showLastSideDivider = show
            return this
        }

        /**
         * Use side divider as the first and last side  divider
         */
        fun showFirstAndLastSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            this.showLastSideDivider = show
            return this
        }


        /**
         * Set the interface for determining FullSpan based on position.
         */
        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }

        /**
         * Set up the interface to find ItemFactory class based on position
         */
        fun findItemFactoryClassByPosition(findItemFactoryClassByPosition: FindItemFactoryClassByPosition?): Builder {
            this.findItemFactoryClassByPosition = findItemFactoryClassByPosition
            return this
        }


        private class AssemblyIsFullSpanByPosition : IsFullSpanByPosition {

            private val concatAdapterLocalHelper by lazy { ConcatAdapterLocalHelper() }

            override fun isFullSpan(parent: RecyclerView, position: Int): Boolean {
                val adapter = parent.adapter
                return if (adapter != null) {
                    val layoutManager = parent.layoutManager
                    val (localAdapter, localPosition) = concatAdapterLocalHelper
                        .findLocalAdapterAndPosition(adapter, position)
                    if (localAdapter is AssemblyAdapter<*> && layoutManager is IsFullSpanByItemFactory) {
                        layoutManager.isFullSpan(localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<*>)
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
        }
    }
}