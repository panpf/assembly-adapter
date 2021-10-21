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
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassSupport
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassSupport
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupport

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、header and footer divider、side divider、header and footer side divider
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
        private var headerDividerConfig: AssemblyDividerConfig? = null
        private var footerDividerConfig: AssemblyDividerConfig? = null
        private var useDividerAsHeaderDivider = false
        private var useDividerAsFooterDivider = false

        private var sideDividerConfig: AssemblyDividerConfig? = null
        private var sideHeaderAndFooterDividerConfig: AssemblyDividerConfig? = null
        private var useSideDividerAsSideHeaderAndFooterDivider = false

        private var disableDefaultDivider = false
        private var isFullSpanByPosition: IsFullSpanByPosition? = null
        private var findItemFactoryClassSupport: FindItemFactoryClassSupport? = null

        fun build(): AssemblyStaggeredGridDividerItemDecoration {
            return AssemblyStaggeredGridDividerItemDecoration(
                buildItemDividerProvider(),
                isFullSpanByPosition ?: AssemblyIsFullSpanByPosition()
            )
        }

        private fun buildItemDividerProvider(): StaggeredGridItemDividerProvider {
            val finalDividerConfig = when {
                dividerConfig != null -> dividerConfig
                !disableDefaultDivider -> context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    AssemblyDividerConfig.Builder(Divider.drawable(it)).build()
                }
                else -> null
            }

            val finalFindItemFactoryClassByPosition =
                (findItemFactoryClassSupport ?: AssemblyFindItemFactoryClassSupport()).run {
                    ConcatFindItemFactoryClassSupport(this)
                }

            return StaggeredGridItemDividerProvider(
                dividerConfig = finalDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                headerDividerConfig = (headerDividerConfig
                    ?: if (useDividerAsHeaderDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                footerDividerConfig = (footerDividerConfig
                    ?: if (useDividerAsFooterDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideDividerConfig = sideDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideHeaderAndFooterDividerConfig = (sideHeaderAndFooterDividerConfig
                    ?: if (useSideDividerAsSideHeaderAndFooterDivider) sideDividerConfig else null)
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
         * Set the header divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun headerDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider of the item.
         */
        fun headerDivider(config: AssemblyDividerConfig): Builder {
            this.headerDividerConfig = config
            return this
        }


        /**
         * Set the footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun footerDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.footerDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider of the item.
         */
        fun footerDivider(config: AssemblyDividerConfig): Builder {
            this.footerDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         */
        fun headerAndFooterDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.footerDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider of the item.
         */
        fun headerAndFooterDivider(config: AssemblyDividerConfig): Builder {
            this.headerDividerConfig = config
            this.footerDividerConfig = config
            return this
        }


        /**
         * Use divider as the header divider.
         */
        fun useDividerAsHeaderDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            return this
        }

        /**
         * Use divider as the footer divider.
         */
        fun useDividerAsFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsFooterDivider = use
            return this
        }

        /**
         * Use divider as the header and footer divider.
         */
        fun useDividerAsHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            this.useDividerAsFooterDivider = use
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
         * Set the header divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideHeaderDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            return sideHeaderAndFooterDivider(divider, configBlock)
        }

        /**
         * Set the header divider on the side of the item
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideHeaderDivider(config: AssemblyDividerConfig): Builder {
            return sideHeaderAndFooterDivider(config)
        }


        /**
         * Set the footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideFooterDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            return sideHeaderAndFooterDivider(divider, configBlock)
        }

        /**
         * Set the footer divider on the side of the item
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideFooterDivider(config: AssemblyDividerConfig): Builder {
            return sideHeaderAndFooterDivider(config)
        }


        /**
         * Set the header and footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderAndFooterDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderAndFooterDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider on the side of the item
         */
        fun sideHeaderAndFooterDivider(config: AssemblyDividerConfig): Builder {
            this.sideHeaderAndFooterDividerConfig = config
            return this
        }


        /**
         * Use side divider as the header side divider
         */
        @Deprecated(
            "Please use useSideDividerAsSideHeaderAndFooterDivider instead",
            ReplaceWith("useSideDividerAsSideHeaderAndFooterDivider")
        )
        fun useSideDividerAsSideHeaderDivider(use: Boolean = true): Builder {
            return useSideDividerAsSideHeaderAndFooterDivider(use)
        }

        /**
         * Use side divider as the footer side divider
         */
        @Deprecated(
            "Please use useSideDividerAsSideHeaderAndFooterDivider instead",
            ReplaceWith("useSideDividerAsSideHeaderAndFooterDivider")
        )
        fun useSideDividerAsSideFooterDivider(use: Boolean = true): Builder {
            return useSideDividerAsSideHeaderAndFooterDivider(use)
        }

        /**
         * Use side divider as the header and footer side divider
         */
        fun useSideDividerAsSideHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useSideDividerAsSideHeaderAndFooterDivider = use
            return this
        }


        /**
         * Prohibit using the system default divider when no divider is specified
         */
        fun disableDefaultDivider(disableDefaultDivider: Boolean = true): Builder {
            this.disableDefaultDivider = disableDefaultDivider
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
         * Set up the interface to find ItemFactory class
         */
        fun findItemFactoryClassSupport(findItemFactoryClassSupport: FindItemFactoryClassSupport?): Builder {
            this.findItemFactoryClassSupport = findItemFactoryClassSupport
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
                    if (localAdapter is AssemblyAdapter<*, *> && layoutManager is FullSpanSupport) {
                        layoutManager.isFullSpanByItemFactoryClass(
                            (localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<Any>).javaClass
                        )
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