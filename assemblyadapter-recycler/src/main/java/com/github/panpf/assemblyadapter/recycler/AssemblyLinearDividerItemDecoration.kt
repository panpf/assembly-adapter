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
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.LinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDecorateProvider
import kotlin.reflect.KClass

open class AssemblyLinearDividerItemDecoration(
    itemDecorateProvider: AssemblyLinearItemDecorateProvider
) : LinearDividerItemDecoration(itemDecorateProvider) {

    class Builder(context: Context) : LinearDividerItemDecoration.Builder(context) {

        private var personaliseDividerItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseStartSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseEndSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null

        private var disableDividerItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableStartSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableEndSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null

        private var findItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)? =
            null

        override fun build(): AssemblyLinearDividerItemDecoration {
            return AssemblyLinearDividerItemDecoration(buildItemDecorateProvider())
        }

        override fun buildItemDecorateProvider(): AssemblyLinearItemDecorateProvider {
            val defaultItemDecorateProvider = super.buildItemDecorateProvider()
            return AssemblyLinearItemDecorateProvider(
                defaultLinearItemDecorateProvider = defaultItemDecorateProvider,
                personaliseDividerItemDecorateMap = personaliseDividerItemDecorateMap,
                personaliseStartSideItemDecorateMap = personaliseStartSideItemDecorateMap,
                personaliseEndSideItemDecorateMap = personaliseEndSideItemDecorateMap,
                disableDividerItemDecorateMap = disableDividerItemDecorateMap,
                disableStartSideItemDecorateMap = disableStartSideItemDecorateMap,
                disableEndSideItemDecorateMap = disableEndSideItemDecorateMap,
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


        override fun startSide(decorate: Decorate): Builder {
            super.startSide(decorate)
            return this
        }

        override fun endSide(decorate: Decorate): Builder {
            super.endSide(decorate)
            return this
        }

        override fun startAndEndSide(decorate: Decorate): Builder {
            super.startAndEndSide(decorate)
            return this
        }


        fun personaliseDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseStartSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseStartSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseStartSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseEndSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseEndSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseEndSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseStartAndEndSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseStartSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseStartSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseEndSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseEndSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun disableDivider(itemFactoryClass: KClass<*>): Builder {
            (disableDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableStartSide(itemFactoryClass: KClass<*>): Builder {
            (disableStartSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableStartSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableEndSide(itemFactoryClass: KClass<*>): Builder {
            (disableEndSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableEndSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableStartAndEndSide(itemFactoryClass: KClass<*>): Builder {
            (disableStartSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableStartSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableEndSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableEndSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }


        fun findItemFactoryClassByPosition(findItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)): Builder {
            this.findItemFactoryClassByPosition = findItemFactoryClassByPosition
            return this
        }
    }

    class AssemblyLinearItemDecorateProvider(
        private val defaultLinearItemDecorateProvider: LinearItemDecorateProvider,
        private val personaliseDividerItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseStartSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseEndSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val disableDividerItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableStartSideItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableEndSideItemDecorateMap: Map<Class<*>, Boolean>?,
        findItemFactoryClassByPosition: ((adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>?)?,
    ) : LinearItemDecorateProvider {

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
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type
        ): ItemDecorate? {
            if (itemCount == 0) return null
            val isLast = position == itemCount - 1
            val adapter = parent.adapter ?: return null
            val itemFactoryClass = findItemFactoryClassByPosition(adapter, position)
                ?: return defaultLinearItemDecorateProvider.getItemDecorate(
                    view, parent, itemCount, position, verticalOrientation, decorateType
                )
            if (isDisabledItemDecorate(
                    verticalOrientation, decorateType, isLast, itemFactoryClass,
                )
            ) {
                return null
            }
            return getPersonaliseItemDecorate(
                verticalOrientation, decorateType, isLast, itemFactoryClass
            ) ?: defaultLinearItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, verticalOrientation, decorateType
            )
        }

        private fun isDisabledItemDecorate(
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type,
            isLast: Boolean,
            itemFactoryClass: Class<*>,
        ): Boolean {
            return if (verticalOrientation) {
                when (decorateType) {
                    ItemDecorate.Type.START -> disableStartSideItemDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> disableEndSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else disableDividerItemDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> disableStartSideItemDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else disableDividerItemDecorateMap)
                    ItemDecorate.Type.BOTTOM -> disableEndSideItemDecorateMap
                }
            }?.containsKey(itemFactoryClass) == true
        }

        private fun getPersonaliseItemDecorate(
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type,
            isLast: Boolean,
            itemFactoryClass: Class<*>,
        ): ItemDecorate? {
            return if (verticalOrientation) {
                when (decorateType) {
                    ItemDecorate.Type.START -> personaliseStartSideItemDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> personaliseEndSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else personaliseDividerItemDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> personaliseStartSideItemDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else personaliseDividerItemDecorateMap)
                    ItemDecorate.Type.BOTTOM -> personaliseEndSideItemDecorateMap
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