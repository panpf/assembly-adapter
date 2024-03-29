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
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassSupport
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyItemDividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassSupport

/**
 * [LinearLayoutManager] dedicated divider ItemDecoration. Support divider、header and footer divider、header and footer side divider
 *
 * On the basis of [LinearDividerItemDecoration], the divider can be disabled or personalized according to the ItemFactory class
 */
open class AssemblyLinearDividerItemDecoration(
    dividerConfig: AssemblyItemDividerConfig?,
    headerDividerConfig: AssemblyItemDividerConfig?,
    footerDividerConfig: AssemblyItemDividerConfig?,
    sideHeaderDividerConfig: AssemblyItemDividerConfig?,
    sideFooterDividerConfig: AssemblyItemDividerConfig?,
) : LinearDividerItemDecoration(
    dividerConfig,
    headerDividerConfig,
    footerDividerConfig,
    sideHeaderDividerConfig,
    sideFooterDividerConfig
) {

    class Builder(val context: Context) {

        private var dividerConfig: AssemblyDividerConfig? = null
        private var headerDividerConfig: AssemblyDividerConfig? = null
        private var footerDividerConfig: AssemblyDividerConfig? = null
        private var useDividerAsHeaderDivider = false
        private var useDividerAsFooterDivider = false

        private var sideHeaderDividerConfig: AssemblyDividerConfig? = null
        private var sideFooterDividerConfig: AssemblyDividerConfig? = null

        private var disableDefaultDivider = false
        private var findItemFactoryClassSupport: FindItemFactoryClassSupport? = null

        fun build(): AssemblyLinearDividerItemDecoration {
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
            if ((useDividerAsHeaderDivider || useDividerAsFooterDivider) && finalDividerConfig == null) {
                throw IllegalArgumentException("Must call the divider() method to configure the divider")
            }

            val finalFindItemFactoryClassByPosition =
                (findItemFactoryClassSupport ?: AssemblyFindItemFactoryClassSupport()).run {
                    ConcatFindItemFactoryClassSupport(this)
                }

            return AssemblyLinearDividerItemDecoration(
                dividerConfig = finalDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                headerDividerConfig = (headerDividerConfig
                    ?: if (useDividerAsHeaderDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                footerDividerConfig = (footerDividerConfig
                    ?: if (useDividerAsFooterDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideHeaderDividerConfig = sideHeaderDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideFooterDividerConfig = sideFooterDividerConfig
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
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the header divider of the item
         */
        fun headerDivider(config: AssemblyDividerConfig): Builder {
            this.headerDividerConfig = config
            return this
        }


        /**
         * Set the footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the footer divider of the item
         */
        fun footerDivider(config: AssemblyDividerConfig): Builder {
            this.footerDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the header and footer divider of the item
         */
        fun headerAndFooterDivider(config: AssemblyDividerConfig): Builder {
            this.headerDividerConfig = config
            this.footerDividerConfig = config
            return this
        }


        /**
         * Use divider as the header divider
         */
        fun useDividerAsHeaderDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            return this
        }

        /**
         * Use divider as the footer divider
         */
        fun useDividerAsFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsFooterDivider = use
            return this
        }

        /**
         * Use divider as the header and footer divider
         */
        fun useDividerAsHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            this.useDividerAsFooterDivider = use
            return this
        }


        /**
         * Set the header divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider on the side of the item
         */
        fun sideHeaderDivider(config: AssemblyDividerConfig): Builder {
            this.sideHeaderDividerConfig = config
            return this
        }


        /**
         * Set the footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideFooterDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideFooterDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider on the side of the item
         */
        fun sideFooterDivider(config: AssemblyDividerConfig): Builder {
            this.sideFooterDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderAndFooterDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.sideFooterDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider on the side of the item
         */
        fun sideHeaderAndFooterDivider(config: AssemblyDividerConfig): Builder {
            this.sideHeaderDividerConfig = config
            this.sideFooterDividerConfig = config
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
         * Set up the interface to find ItemFactory class
         */
        fun findItemFactoryClassSupport(findItemFactoryClassSupport: FindItemFactoryClassSupport?): Builder {
            this.findItemFactoryClassSupport = findItemFactoryClassSupport
            return this
        }
    }
}