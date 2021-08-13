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
package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.IsFullSpanByPosition
import com.github.panpf.assemblyadapter.recycler.divider.StaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.internal.IsFullSpanByItemFactory

open class AssemblyStaggeredGridDividerItemDecoration(
    itemDecorateProviderStaggered: StaggeredGridItemDecorateProvider,
    isFullSpanByPosition: IsFullSpanByPosition
) : StaggeredGridDividerItemDecoration(
    itemDecorateProviderStaggered, isFullSpanByPosition
) {

    class Builder(val context: Context) {

        private var dividerDecorateConfig: AssemblyDecorateConfig? = null
        private var firstDividerDecorateConfig: AssemblyDecorateConfig? = null
        private var lastDividerDecorateConfig: AssemblyDecorateConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var sideDecorateConfig: AssemblyDecorateConfig? = null
        private var firstSideDecorateConfig: AssemblyDecorateConfig? = null
        private var lastSideDecorateConfig: AssemblyDecorateConfig? = null
        private var showFirstSide = false
        private var showLastSide = false

        private var isFullSpanByPosition: IsFullSpanByPosition? = null
        private var findItemFactoryClassByPosition: FindItemFactoryClassByPosition? = null

        fun build(): StaggeredGridDividerItemDecoration {
            return AssemblyStaggeredGridDividerItemDecoration(
                buildItemDecorateProvider(),
                isFullSpanByPosition ?: AssemblyIsFullSpanByPosition()
            )
        }

        private fun buildItemDecorateProvider(): StaggeredGridItemDecorateProvider {
            val finalDividerDecorateConfig =
                dividerDecorateConfig ?: context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    AssemblyDecorateConfig.Builder(Decorate.drawable(it)).build()
                }

            val finalFindItemFactoryClassByPosition =
                findItemFactoryClassByPosition ?: AssemblyFindItemFactoryClassByPosition()

            val finalDividerItemDecorateConfig =
                finalDividerDecorateConfig.toItemDecorateHolder(
                    context,
                    finalFindItemFactoryClassByPosition
                )
            val firstDividerItemDecorate = (firstDividerDecorateConfig
                ?: if (showFirstDivider) finalDividerDecorateConfig else null)
                ?.toItemDecorateHolder(context, finalFindItemFactoryClassByPosition)
            val lastDividerItemDecorate = (lastDividerDecorateConfig
                ?: if (showLastDivider) finalDividerDecorateConfig else null)
                ?.toItemDecorateHolder(context, finalFindItemFactoryClassByPosition)

            val sideItemDecorate =
                sideDecorateConfig?.toItemDecorateHolder(
                    context,
                    finalFindItemFactoryClassByPosition
                )
            val firstSideItemDecorate = (firstSideDecorateConfig
                ?: if (showFirstSide) sideDecorateConfig else null)
                ?.toItemDecorateHolder(context, finalFindItemFactoryClassByPosition)
            val lastSideItemDecorate = (lastSideDecorateConfig
                ?: if (showLastSide) sideDecorateConfig else null)
                ?.toItemDecorateHolder(context, finalFindItemFactoryClassByPosition)

            return StaggeredGridItemDecorateProvider(
                finalDividerItemDecorateConfig,
                firstDividerItemDecorate,
                lastDividerItemDecorate,
                sideItemDecorate,
                firstSideItemDecorate,
                lastSideItemDecorate,
            )
        }


        fun divider(decorate: Decorate): Builder {
            this.dividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun divider(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun divider(decorateConfig: AssemblyDecorateConfig): Builder {
            this.dividerDecorateConfig = decorateConfig
            return this
        }


        fun firstDivider(decorate: Decorate): Builder {
            this.firstDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstDivider(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstDivider(decorateConfig: AssemblyDecorateConfig): Builder {
            this.firstDividerDecorateConfig = decorateConfig
            return this
        }


        fun lastDivider(decorate: Decorate): Builder {
            this.lastDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun lastDivider(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastDivider(decorateConfig: AssemblyDecorateConfig): Builder {
            this.lastDividerDecorateConfig = decorateConfig
            return this
        }


        fun firstAndLastDivider(decorate: Decorate): Builder {
            this.firstDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            this.lastDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstAndLastDivider(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastDividerDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstAndLastDivider(decorateConfig: AssemblyDecorateConfig): Builder {
            this.firstDividerDecorateConfig = decorateConfig
            this.lastDividerDecorateConfig = decorateConfig
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


        fun side(decorate: Decorate): Builder {
            this.sideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun side(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun side(decorateConfig: AssemblyDecorateConfig): Builder {
            this.sideDecorateConfig = decorateConfig
            return this
        }


        fun firstSide(decorate: Decorate): Builder {
            this.firstSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstSide(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstSide(decorateConfig: AssemblyDecorateConfig): Builder {
            this.firstSideDecorateConfig = decorateConfig
            return this
        }


        fun lastSide(decorate: Decorate): Builder {
            this.lastSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun lastSide(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastSide(decorateConfig: AssemblyDecorateConfig): Builder {
            this.lastSideDecorateConfig = decorateConfig
            return this
        }


        fun firstAndLastSide(decorate: Decorate): Builder {
            this.firstSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            this.lastSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstAndLastSide(
            decorate: Decorate,
            configBlock: (AssemblyDecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastSideDecorateConfig = AssemblyDecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstAndLastSide(decorateConfig: AssemblyDecorateConfig): Builder {
            this.firstSideDecorateConfig = decorateConfig
            this.lastSideDecorateConfig = decorateConfig
            return this
        }


        fun showFirstSide(showFirstSide: Boolean = true): Builder {
            this.showFirstSide = showFirstSide
            return this
        }

        fun showLastSide(showLastSide: Boolean = true): Builder {
            this.showLastSide = showLastSide
            return this
        }

        fun showFirstAndLastSide(showFirstAndLastSide: Boolean = true): Builder {
            this.showFirstSide = showFirstAndLastSide
            this.showLastSide = showFirstAndLastSide
            return this
        }


        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }

        fun findItemFactoryClassByPosition(getItemFactoryClassByPosition: FindItemFactoryClassByPosition?): Builder {
            this.findItemFactoryClassByPosition = getItemFactoryClassByPosition
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

        class AssemblyFindItemFactoryClassByPosition : FindItemFactoryClassByPosition {

            private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

            override fun find(adapter: RecyclerView.Adapter<*>, position: Int): Class<*>? {
                val (localAdapter, localPosition) = concatAdapterLocalHelper
                    .findLocalAdapterAndPosition(adapter, position)
                return if (localAdapter is AssemblyAdapter<*>) {
                    localAdapter.getItemFactoryByPosition(localPosition).javaClass
                } else {
                    null
                }
            }
        }
    }
}