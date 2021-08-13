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
import android.view.View
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.GridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import kotlin.reflect.KClass

open class AssemblyGridDividerItemDecoration(
    itemDecorateProvider: AssemblyGridItemDecorateProvider
) : GridDividerItemDecoration(itemDecorateProvider) {

    class Builder(context: Context) : GridDividerItemDecoration.Builder(context) {

        private var personaliseDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? = null
        private var personaliseFirstDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? =
            null
        private var personaliseLastDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? =
            null
        private var personaliseSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? = null
        private var personaliseFirstSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? = null
        private var personaliseLastSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>? = null

        private var disableDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var disableFirstDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var disableLastDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var disableSideItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var disableFirstSideItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var disableLastSideItemDecorateMap: ArrayMap<Class<*>, Boolean>? = null

        private var findItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)? =
            null

        override fun build(): AssemblyGridDividerItemDecoration {
            return AssemblyGridDividerItemDecoration(buildItemDecorateProvider())
        }

        override fun buildItemDecorateProvider(): AssemblyGridItemDecorateProvider {
            val defaultItemDecorateProvider = super.buildItemDecorateProvider()
            return AssemblyGridItemDecorateProvider(
                defaultGridItemDecorateProvider = defaultItemDecorateProvider,
                personaliseDividerItemDecorateMap = personaliseDividerItemDecorateMap,
                personaliseFirstDividerItemDecorateMap = personaliseFirstDividerItemDecorateMap,
                personaliseLastDividerItemDecorateMap = personaliseLastDividerItemDecorateMap,
                personaliseSideItemDecorateMap = personaliseSideItemDecorateMap,
                personaliseFirstSideItemDecorateMap = personaliseFirstSideItemDecorateMap,
                personaliseLastSideItemDecorateMap = personaliseLastSideItemDecorateMap,
                disableDividerItemDecorateMap = disableDividerItemDecorateMap,
                disableFirstDividerItemDecorateMap = disableFirstDividerItemDecorateMap,
                disableLastDividerItemDecorateMap = disableLastDividerItemDecorateMap,
                disableSideItemDecorateMap = disableSideItemDecorateMap,
                disableFirstSideItemDecorateMap = disableFirstSideItemDecorateMap,
                disableLastSideItemDecorateMap = disableLastSideItemDecorateMap,
                findItemFactoryClassByPosition = findItemFactoryClassByPosition,
            )
        }

        override fun divider(decorate: Decorate): Builder {
            super.divider(decorate)
            return this
        }

        override fun firstDivider(decorate: Decorate): Builder {
            super.firstDivider(decorate)
            return this
        }

        override fun lastDivider(decorate: Decorate): Builder {
            super.lastDivider(decorate)
            return this
        }

        override fun firstAndLastDivider(decorate: Decorate): Builder {
            super.firstAndLastDivider(decorate)
            return this
        }

        override fun showFirstDivider(showFirstDivider: Boolean): Builder {
            super.showFirstDivider(showFirstDivider)
            return this
        }

        override fun showLastDivider(showLastDivider: Boolean): Builder {
            super.showLastDivider(showLastDivider)
            return this
        }

        override fun showFirstAndLastDivider(showFirstAndLastDivider: Boolean): Builder {
            super.showFirstAndLastDivider(showFirstAndLastDivider)
            return this
        }


        override fun side(decorate: Decorate): Builder {
            super.side(decorate)
            return this
        }

        override fun firstSide(decorate: Decorate): Builder {
            super.firstSide(decorate)
            return this
        }

        override fun lastSide(decorate: Decorate): Builder {
            super.lastSide(decorate)
            return this
        }

        override fun firstAndLastSide(decorate: Decorate): Builder {
            super.firstAndLastSide(decorate)
            return this
        }

        override fun showFirstSide(showFirstSide: Boolean): Builder {
            super.showFirstSide(showFirstSide)
            return this
        }

        override fun showLastSide(showLastSide: Boolean): Builder {
            super.showLastSide(showLastSide)
            return this
        }

        override fun showFirstAndLastSide(showFirstAndLastSide: Boolean): Builder {
            super.showFirstAndLastSide(showFirstAndLastSide)
            return this
        }


        fun personaliseDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseDividerItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstDividerItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseLastDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseLastDividerItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstAndLastDivider(
            itemFactoryClass: KClass<*>,
            decorate: Decorate
        ): Builder {
            (personaliseFirstDividerItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseLastDividerItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun personaliseSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseSideItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstSideItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseLastSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseLastSideItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstAndLastSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstSideItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseLastSideItemDecorateMap ?: ArrayMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun disableDivider(itemFactoryClass: KClass<*>): Builder {
            (disableDividerItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstDivider(itemFactoryClass: KClass<*>): Builder {
            (disableFirstDividerItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableLastDivider(itemFactoryClass: KClass<*>): Builder {
            (disableLastDividerItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstAndLastDivider(itemFactoryClass: KClass<*>): Builder {
            (disableFirstDividerItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableLastDividerItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }


        fun disableSide(itemFactoryClass: KClass<*>): Builder {
            (disableSideItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstSide(itemFactoryClass: KClass<*>): Builder {
            (disableFirstSideItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableLastSide(itemFactoryClass: KClass<*>): Builder {
            (disableLastSideItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstAndLastSide(itemFactoryClass: KClass<*>): Builder {
            (disableFirstSideItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableLastSideItemDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }


        fun findItemFactoryClassByPosition(getItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)): Builder {
            this.findItemFactoryClassByPosition = getItemFactoryClassByPosition
            return this
        }
    }

    class AssemblyGridItemDecorateProvider(
        private val defaultGridItemDecorateProvider: GridItemDecorateProvider,
        private val personaliseDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val personaliseFirstDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val personaliseLastDividerItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val personaliseSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val personaliseFirstSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val personaliseLastSideItemDecorateMap: ArrayMap<Class<*>, ItemDecorate>?,
        private val disableDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        private val disableFirstDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        private val disableLastDividerItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        private val disableSideItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        private val disableFirstSideItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        private val disableLastSideItemDecorateMap: ArrayMap<Class<*>, Boolean>?,
        findItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)?,
    ) : GridItemDecorateProvider {

        private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()
        private val finalFindItemFactoryClassByPosition =
            findItemFactoryClassByPosition ?: { adapter, position ->
                if (adapter is AssemblyAdapter<*>) {
                    adapter.getItemFactoryByPosition(position).javaClass
                } else {
                    null
                }
            }

        override fun getItemDecorate(
            view: View,
            parent: RecyclerView,
            itemCount: Int,
            position: Int,
            spanCount: Int,
            spanSize: Int,
            spanIndex: Int,
            spanGroupCount: Int,
            spanGroupIndex: Int,
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type,
        ): ItemDecorate? {
            if (itemCount == 0) return null
            val isFirstGroup = spanGroupIndex == 0
            val isLastGroup = spanGroupIndex == spanGroupCount - 1
            val isFullSpan = spanSize == spanCount
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1
            val adapter = parent.adapter ?: return null
            val itemFactoryClass = findItemFactoryClassByPosition(adapter, position)
                ?: return defaultGridItemDecorateProvider.getItemDecorate(
                    view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                    spanGroupCount, spanGroupIndex, verticalOrientation, decorateType
                )
            if (isDisabledItemDecorate(
                    verticalOrientation, decorateType, isFirstGroup, isLastGroup,
                    isFirstSpan, isLastSpan, itemFactoryClass,
                )
            ) {
                return null
            }
            return getPersonaliseItemDecorate(
                verticalOrientation, decorateType, isFirstGroup, isLastGroup,
                isFirstSpan, isLastSpan, itemFactoryClass
            ) ?: defaultGridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, verticalOrientation, decorateType
            )
        }

        private fun isDisabledItemDecorate(
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type,
            isFirstGroup: Boolean,
            isLastGroup: Boolean,
            isFirstSpan: Boolean,
            isLastSpan: Boolean,
            itemFactoryClass: Class<*>,
        ): Boolean {
            return if (verticalOrientation) {
                when (decorateType) {
                    ItemDecorate.Type.START -> if (isFirstSpan) disableFirstSideItemDecorateMap else null
                    ItemDecorate.Type.TOP -> if (isFirstGroup) disableFirstDividerItemDecorateMap else null
                    ItemDecorate.Type.END -> if (isLastSpan) disableLastSideItemDecorateMap else disableSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> if (isLastGroup) disableLastDividerItemDecorateMap else disableDividerItemDecorateMap
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> if (isFirstGroup) disableFirstDividerItemDecorateMap else null
                    ItemDecorate.Type.TOP -> if (isFirstSpan) disableFirstSideItemDecorateMap else null
                    ItemDecorate.Type.END -> if (isLastGroup) disableLastDividerItemDecorateMap else disableDividerItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> if (isLastSpan) disableLastSideItemDecorateMap else disableSideItemDecorateMap
                }
            }?.containsKey(itemFactoryClass) == true
        }

        private fun getPersonaliseItemDecorate(
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type,
            isFirstGroup: Boolean,
            isLastGroup: Boolean,
            isFirstSpan: Boolean,
            isLastSpan: Boolean,
            itemFactoryClass: Class<*>,
        ): ItemDecorate? {
            return if (verticalOrientation) {
                when (decorateType) {
                    ItemDecorate.Type.START -> if (isFirstSpan) personaliseFirstSideItemDecorateMap else null
                    ItemDecorate.Type.TOP -> if (isFirstGroup) personaliseFirstDividerItemDecorateMap else null
                    ItemDecorate.Type.END -> if (isLastSpan) personaliseLastSideItemDecorateMap else personaliseSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> if (isLastGroup) personaliseLastDividerItemDecorateMap else personaliseDividerItemDecorateMap
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> if (isFirstGroup) personaliseFirstDividerItemDecorateMap else null
                    ItemDecorate.Type.TOP -> if (isFirstSpan) personaliseFirstSideItemDecorateMap else null
                    ItemDecorate.Type.END -> if (isLastGroup) personaliseLastDividerItemDecorateMap else personaliseDividerItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> if (isLastSpan) personaliseLastSideItemDecorateMap else personaliseSideItemDecorateMap
                }
            }?.get(itemFactoryClass)
        }

        private fun findItemFactoryClassByPosition(
            adapter: RecyclerView.Adapter<*>, position: Int
        ): Class<*>? {
            val (localAdapter, localPosition) = concatAdapterLocalHelper
                .findLocalAdapterAndPosition(adapter, position)
            return finalFindItemFactoryClassByPosition(localAdapter, localPosition)
        }
    }
}