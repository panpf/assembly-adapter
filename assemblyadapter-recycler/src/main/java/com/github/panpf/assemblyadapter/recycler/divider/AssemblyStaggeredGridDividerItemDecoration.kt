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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassByPosition
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.internal.IsFullSpanByItemFactory

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


        fun showFirstDivider(showFirstDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstDivider
            return this
        }

        fun showLastDivider(showLastDivider: Boolean = true): Builder {
            this.showLastDivider = showLastDivider
            return this
        }

        fun showFirstAndLastDivider(showFirstAndLastDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstAndLastDivider
            this.showLastDivider = showFirstAndLastDivider
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


        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }

        fun findItemFactoryClassByPosition(findItemFactoryClassByPosition: FindItemFactoryClassByPosition?): Builder {
            this.findItemFactoryClassByPosition = findItemFactoryClassByPosition
            return this
        }


        class AssemblyIsFullSpanByPosition : IsFullSpanByPosition {

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