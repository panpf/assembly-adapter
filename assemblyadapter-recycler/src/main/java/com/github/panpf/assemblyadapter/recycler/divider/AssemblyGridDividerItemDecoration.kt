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
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDividerProvider

open class AssemblyGridDividerItemDecoration(
    itemDividerProvider: GridItemDividerProvider
) : GridDividerItemDecoration(itemDividerProvider) {

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

        private var findItemFactoryClassByPosition: FindItemFactoryClassByPosition? = null

        fun build(): AssemblyGridDividerItemDecoration {
            return AssemblyGridDividerItemDecoration(buildItemDividerProvider())
        }

        private fun buildItemDividerProvider(): GridItemDividerProvider {
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

            return GridItemDividerProvider(
                dividerConfig = finalDividerConfig
                    .toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstDividerConfig = (firstDividerConfig
                    ?: if (showFirstDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastDividerConfig = (lastDividerConfig
                    ?: if (showLastDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                sideDividerConfig = sideDividerConfig
                    ?.toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                firstSideDividerConfig = (firstSideDividerConfig
                    ?: if (showFirstSideDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
                lastSideDividerConfig = (lastSideDividerConfig
                    ?: if (showLastSideDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context, finalFindItemFactoryClassByPosition),
            )
        }


        fun divider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun divider(config: AssemblyDividerConfig): Builder {
            this.dividerConfig = config
            return this
        }


        fun firstDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            return this
        }


        fun lastDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastDivider(config: AssemblyDividerConfig): Builder {
            this.lastDividerConfig = config
            return this
        }


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

        fun firstAndLastDivider(config: AssemblyDividerConfig): Builder {
            this.firstDividerConfig = config
            this.lastDividerConfig = config
            return this
        }


        fun showFirstDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            return this
        }

        fun showLastDivider(show: Boolean = true): Builder {
            this.showLastDivider = show
            return this
        }

        fun showFirstAndLastDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            this.showLastDivider = show
            return this
        }


        fun sideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun sideDivider(config: AssemblyDividerConfig): Builder {
            this.sideDividerConfig = config
            return this
        }


        fun firstSideDivider(
            divider: Divider,
            configBlock: (AssemblyDividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = AssemblyDividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstSideDivider(config: AssemblyDividerConfig): Builder {
            this.firstSideDividerConfig = config
            return this
        }


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

        fun firstAndLastSideDivider(config: AssemblyDividerConfig): Builder {
            this.firstSideDividerConfig = config
            this.lastSideDividerConfig = config
            return this
        }


        fun showFirstSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            return this
        }

        fun showLastSideDivider(show: Boolean = true): Builder {
            this.showLastSideDivider = show
            return this
        }

        fun showFirstAndLastSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            this.showLastSideDivider = show
            return this
        }


        fun findItemFactoryClassByPosition(findItemFactoryClassByPosition: FindItemFactoryClassByPosition?): Builder {
            this.findItemFactoryClassByPosition = findItemFactoryClassByPosition
            return this
        }
    }
}