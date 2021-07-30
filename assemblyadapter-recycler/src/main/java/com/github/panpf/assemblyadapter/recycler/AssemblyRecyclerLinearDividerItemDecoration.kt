package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.ItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.divider.RecyclerLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import kotlin.reflect.KClass

open class AssemblyRecyclerLinearDividerItemDecoration(
    itemDecorateProvider: AssemblyItemDecorateProvider
) : RecyclerLinearDividerItemDecoration(itemDecorateProvider) {

    class Builder(context: Context) : RecyclerLinearDividerItemDecoration.Builder(context) {

        private var personaliseDividerDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseStartSideDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null
        private var personaliseEndSideDecorateMap: MutableMap<Class<*>, ItemDecorate>? = null

        private var disableDividerDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableStartSideDecorateMap: MutableMap<Class<*>, Boolean>? = null
        private var disableEndSideDecorateMap: MutableMap<Class<*>, Boolean>? = null

        private var itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)? = null

        override fun build(): AssemblyRecyclerLinearDividerItemDecoration {
            return AssemblyRecyclerLinearDividerItemDecoration(buildDecorateProvider())
        }

        override fun buildDecorateProvider(): AssemblyItemDecorateProvider {
            val defaultDecorateProvider = super.buildDecorateProvider()
            return AssemblyItemDecorateProvider(
                defaultItemDecorateProvider = defaultDecorateProvider,
                personaliseDividerDecorateMap = personaliseDividerDecorateMap,
                personaliseStartSideDecorateMap = personaliseStartSideDecorateMap,
                personaliseEndSideDecorateMap = personaliseEndSideDecorateMap,
                disableDividerDecorateMap = disableDividerDecorateMap,
                disableStartSideDecorateMap = disableStartSideDecorateMap,
                disableEndSideDecorateMap = disableEndSideDecorateMap,
                itemFactoryClassConverter = itemFactoryClassConverter,
            )
        }

        override fun divider(decorate: Decorate): Builder {
            super.divider(decorate)
            return this
        }

        override fun startSide(decorate: Decorate): Builder {
            super.startSide(decorate)
            return this
        }

        override fun firstDivider(decorate: Decorate): Builder {
            super.firstDivider(decorate)
            return this
        }

        override fun endSide(decorate: Decorate): Builder {
            super.endSide(decorate)
            return this
        }

        override fun lastDivider(decorate: Decorate): Builder {
            super.lastDivider(decorate)
            return this
        }

        override fun startAndEndSide(decorate: Decorate): Builder {
            super.startAndEndSide(decorate)
            return this
        }

        override fun firstAndLastDivider(decorate: Decorate): Builder {
            super.firstAndLastDivider(decorate)
            return this
        }


        fun personaliseDivider(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseDividerDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseDividerDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseStartSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseStartSideDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseStartSideDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseEndSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseEndSideDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseEndSideDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }

        fun personaliseStartAndEndSide(itemFactoryClass: KClass<*>, decorate: Decorate): Builder {
            (personaliseStartSideDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseStartSideDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            (personaliseEndSideDecorateMap ?: HashMap<Class<*>, ItemDecorate>().apply {
                this@Builder.personaliseEndSideDecorateMap = this
            })[itemFactoryClass.java] = decorate.createItemDecorate(context)
            return this
        }


        fun disableDivider(itemFactoryClass: KClass<*>): Builder {
            (disableDividerDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableDividerDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableStartSide(itemFactoryClass: KClass<*>): Builder {
            (disableStartSideDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableStartSideDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableEndSide(itemFactoryClass: KClass<*>): Builder {
            (disableEndSideDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableEndSideDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        fun disableStartAndEndSide(itemFactoryClass: KClass<*>): Builder {
            (disableStartSideDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableStartSideDecorateMap = this
            })[itemFactoryClass.java] = true
            (disableEndSideDecorateMap ?: HashMap<Class<*>, Boolean>().apply {
                this@Builder.disableEndSideDecorateMap = this
            })[itemFactoryClass.java] = true
            return this
        }


        fun itemFactoryClassConverter(itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)): Builder {
            this.itemFactoryClassConverter = itemFactoryClassConverter
            return this
        }
    }

    class AssemblyItemDecorateProvider(
        private val defaultItemDecorateProvider: ItemDecorateProvider,
        private val personaliseDividerDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseStartSideDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val personaliseEndSideDecorateMap: Map<Class<*>, ItemDecorate>?,
        private val disableDividerDecorateMap: Map<Class<*>, Boolean>?,
        private val disableStartSideDecorateMap: Map<Class<*>, Boolean>?,
        private val disableEndSideDecorateMap: Map<Class<*>, Boolean>?,
        private val itemFactoryClassConverter: ((ItemFactory<*>) -> Class<*>)?,
    ) : ItemDecorateProvider {

        private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

        override fun getItemDecorate(
            view: View,
            parent: RecyclerView,
            itemCount: Int,
            position: Int,
            verticalOrientation: Boolean,
            decorateType: ItemDecorate.Type
        ): ItemDecorate? {
            val isLast = position == itemCount - 1
            val adapter = parent.adapter ?: return null
            val itemFactory = findItemFactory(adapter, position) ?: return null
            val itemFactoryClass =
                itemFactoryClassConverter?.invoke(itemFactory) ?: itemFactory.javaClass
            if (isDisabledItemDecorate(
                    verticalOrientation, decorateType, isLast, itemFactoryClass,
                )
            ) {
                return null
            }
            return getPersonaliseItemDecorate(
                verticalOrientation, decorateType, isLast, itemFactoryClass
            ) ?: defaultItemDecorateProvider.getItemDecorate(
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
                    ItemDecorate.Type.START -> disableStartSideDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> disableEndSideDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else disableDividerDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> disableStartSideDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else disableDividerDecorateMap)
                    ItemDecorate.Type.BOTTOM -> disableEndSideDecorateMap
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
                    ItemDecorate.Type.START -> personaliseStartSideDecorateMap
                    ItemDecorate.Type.TOP -> null
                    ItemDecorate.Type.END -> personaliseEndSideDecorateMap
                    ItemDecorate.Type.BOTTOM -> (if (isLast) null else personaliseDividerDecorateMap)
                }
            } else {
                when (decorateType) {
                    ItemDecorate.Type.START -> null
                    ItemDecorate.Type.TOP -> personaliseStartSideDecorateMap
                    ItemDecorate.Type.END -> (if (isLast) null else personaliseDividerDecorateMap)
                    ItemDecorate.Type.BOTTOM -> personaliseEndSideDecorateMap
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