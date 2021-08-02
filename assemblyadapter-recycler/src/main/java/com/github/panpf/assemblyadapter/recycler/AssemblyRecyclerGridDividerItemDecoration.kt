package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.RecyclerGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import kotlin.reflect.KClass

open class AssemblyRecyclerGridDividerItemDecoration(
    itemDecorateProvider: AssemblyGridItemDecorateProvider
) : RecyclerGridDividerItemDecoration(itemDecorateProvider) {

    class Builder(context: Context) : RecyclerGridDividerItemDecoration.Builder(context) {

        private var personaliseDividerItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseFirstDividerItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? =
            null
        private var personaliseLastDividerItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? =
            null
        private var personaliseSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseFirstSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseLastSideItemDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null

        private var disableDividerItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableFirstDividerItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableLastDividerItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableFirstSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableLastSideItemDecorateMap: MutableMap<Class<*>, Boolean>? = null

        private var itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)? = null

        override fun build(): AssemblyRecyclerGridDividerItemDecoration {
            return AssemblyRecyclerGridDividerItemDecoration(buildItemDecorateProvider())
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
                itemFactoryClassConverter = itemFactoryClassConverter,
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


        fun personaliseDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseFirstDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseLastDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseLastDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseFirstAndLastDivider(
            itemFactoryClass: KClass<*>,
            decorate: Decorate
        ): Builder {
            (personaliseFirstDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseLastDividerItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun personaliseSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseSideItemDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseSideItemDecorateMap = this
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

        fun disableFirstDivider(itemFactoryClass: KClass<*>): Builder {
            (disableFirstDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableLastDivider(itemFactoryClass: KClass<*>): Builder {
            (disableLastDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableFirstAndLastDivider(itemFactoryClass: KClass<*>): Builder {
            (disableFirstDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableFirstDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableLastDividerItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableLastDividerItemDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }


        fun disableSide(itemFactoryClass: KClass<*>): Builder {
            (disableSideItemDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableSideItemDecorateMap = this
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


        fun itemFactoryClassConverter(itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)): Builder {
            this.itemFactoryClassConverter = itemFactoryClassConverter
            return this
        }
    }

    class AssemblyGridItemDecorateProvider(
        private val defaultGridItemDecorateProvider: GridItemDecorateProvider,
        private val personaliseDividerItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseFirstDividerItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseLastDividerItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseFirstSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseLastSideItemDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val disableDividerItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableFirstDividerItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableLastDividerItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableSideItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableFirstSideItemDecorateMap: Map<Class<*>, Boolean>?,
        private val disableLastSideItemDecorateMap: Map<Class<*>, Boolean>?,
        private val itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)?,
    ) : GridItemDecorateProvider {

        private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

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
            val isFirstGroup = spanGroupIndex == 0
            val isLastGroup = spanGroupIndex == spanGroupCount - 1
            val isFullSpan = spanSize == spanCount
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1
            val adapter = parent.adapter ?: return null
            val itemFactory = findItemFactory(adapter, position) ?: return null
            val itemFactoryClass =
                itemFactoryClassConverter?.invoke(itemFactory) ?: itemFactory.javaClass
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

        private fun findItemFactory(
            adapter: RecyclerView.Adapter<*>,
            position: Int
        ): ItemFactory<*>? {
            val (localAdapter, localPosition) = concatAdapterLocalHelper
                .findLocalAdapterAndPosition(adapter, position)
            return if (localAdapter is AssemblyAdapter<*>) {
                localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<*>
            } else {
                null
            }
        }
    }
}