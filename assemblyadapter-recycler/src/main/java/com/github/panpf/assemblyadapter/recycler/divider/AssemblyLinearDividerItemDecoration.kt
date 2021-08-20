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
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassSupport
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassSupport

/**
 * [LinearLayoutManager] dedicated divider ItemDecoration. Support divider、first or last divider、fist or last side divider
 *
 * On the basis of [LinearDividerItemDecoration], the divider can be disabled or personalized according to the ItemFactory class
 */
open class AssemblyLinearDividerItemDecoration(
    itemDividerProvider: LinearItemDividerProvider
) : LinearDividerItemDecoration(itemDividerProvider) {

    class Builder(val context: Context) {

        private var dividerConfig: AssemblyDividerConfig? = null
        private var firstDividerConfig: AssemblyDividerConfig? = null
        private var lastDividerConfig: AssemblyDividerConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var firstSideDividerConfig: AssemblyDividerConfig? = null
        private var lastSideDividerConfig: AssemblyDividerConfig? = null

        private var findItemFactoryClassSupport: FindItemFactoryClassSupport? = null

        fun build(): AssemblyLinearDividerItemDecoration {
            return AssemblyLinearDividerItemDecoration(buildItemDividerProvider())
        }

        private fun buildItemDividerProvider(): LinearItemDividerProvider {
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
                (findItemFactoryClassSupport ?: AssemblyFindItemFactoryClassSupport()).run {
                    ConcatFindItemFactoryClassSupport(this)
                }

            return LinearItemDividerProvider(
                dividerConfig = finalDividerConfig
                    .toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstDividerConfig = (firstDividerConfig
                    ?: if (showFirstDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastDividerConfig = (lastDividerConfig
                    ?: if (showLastDivider) finalDividerConfig else null)
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstSideDividerConfig = firstSideDividerConfig
                    ?.toAssemblyItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastSideDividerConfig = lastSideDividerConfig
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
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the first divider of the item
         */
        fun firstDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            return this
        }


        /**
         * Set the last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the last divider of the item
         */
        fun lastDivider(config: AssemblyDividerConfig): Builder {
            this.lastDividerConfig = config
            return this
        }


        /**
         * Set the first and last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
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
         * Set the first and last divider of the item
         */
        fun firstAndLastDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            this.lastDividerConfig = config
            return this
        }


        /**
         * Use divider as the first divider
         */
        fun showFirstDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            return this
        }

        /**
         * Use divider as the last divider
         */
        fun showLastDivider(show: Boolean = true): Builder {
            this.showLastDivider = show
            return this
        }

        /**
         * Use divider as the first and last divider
         */
        fun showFirstAndLastDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            this.showLastDivider = show
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

        /**
         * Set the last divider on the side of the item
         */
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
         * Set up the interface to find ItemFactory class
         */
        fun findItemFactoryClassSupport(findItemFactoryClassSupport: FindItemFactoryClassSupport?): Builder {
            this.findItemFactoryClassSupport = findItemFactoryClassSupport
            return this
        }
    }
}