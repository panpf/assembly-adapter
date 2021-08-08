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

fun RecyclerView.assemblyLinearDividerItemDecorationBuilder(): AssemblyLinearDividerItemDecoration.Builder {
    return AssemblyLinearDividerItemDecoration.Builder(context)
}

open class AssemblyLinearDividerItemDecoration(
    itemDecorateProvider: AssemblyLinearItemDecorateProvider
) : LinearDividerItemDecoration(itemDecorateProvider) {

    class Builder(context: Context) : LinearDividerItemDecoration.Builder(context) {

        private var personaliseDividerItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseFirstSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseLastSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null

        private var disableDividerItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableFirstSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableLastSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null

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
                personaliseFirstSideItemDecorateMap = personaliseFirstSideItemDecorateMap,
                personaliseLastSideItemDecorateMap = personaliseLastSideItemDecorateMap,
                disableDividerItemDecorateMap = disableDividerItemDecorateMap,
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


        fun personaliseDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseLastSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseLastSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstAndLastSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseLastSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun disableDivider(itemFactoryClass: KClass<*>): Builder {
            (disableDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstSide(itemFactoryClass: KClass<*>): Builder {
            (disableFirstSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableLastSide(itemFactoryClass: KClass<*>): Builder {
            (disableLastSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstAndLastSide(itemFactoryClass: KClass<*>): Builder {
            (disableFirstSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstSideItemDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableLastSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastSideItemDecorateMap = this
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
        private val personaliseFirstSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseLastSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val disableDividerItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableFirstSideItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableLastSideItemDecorateMap: Map<Class<*>, Boolean>?,
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
                    ItemDecorate.Type.START -> disableFirstSideItemDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> disableLastSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else disableDividerItemDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> disableFirstSideItemDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else disableDividerItemDecorateMap)
                    ItemDecorate.Type.BOTTOM -> disableLastSideItemDecorateMap
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
                    ItemDecorate.Type.START -> personaliseFirstSideItemDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> personaliseLastSideItemDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else personaliseDividerItemDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> personaliseFirstSideItemDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else personaliseDividerItemDecorateMap)
                    ItemDecorate.Type.BOTTOM -> personaliseLastSideItemDecorateMap
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