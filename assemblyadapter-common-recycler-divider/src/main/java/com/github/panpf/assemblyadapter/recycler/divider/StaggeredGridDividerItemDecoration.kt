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
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.FullSpanSupport
import com.github.panpf.assemblyadapter.recycler.FullSpanSupportFromLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridDividerNoSideHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridDividerOnlySideHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridDividerSideAndFooterHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridDividerSideAndHeaderFooterHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridDividerSideAndHeaderHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemParams
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerConfig

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、header and footer divider、side divider、header and footer side divider
 */
open class StaggeredGridDividerItemDecoration(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig?,
    val sideHeaderDividerConfig: ItemDividerConfig?,
    val sideFooterDividerConfig: ItemDividerConfig?,
    val isFullSpanByPosition: FullSpanSupport?,
) : ItemDecoration() {

    val dividerHelper = when {
        sideDividerConfig != null && sideHeaderDividerConfig != null && sideFooterDividerConfig != null -> {
            GridDividerSideAndHeaderFooterHelper(
                dividerConfig,
                headerDividerConfig,
                footerDividerConfig,
                sideDividerConfig,
                sideHeaderDividerConfig,
                sideFooterDividerConfig
            )
        }
        sideDividerConfig != null && sideHeaderDividerConfig != null && sideFooterDividerConfig == null -> {
            GridDividerSideAndHeaderHelper(
                dividerConfig,
                headerDividerConfig,
                footerDividerConfig,
                sideDividerConfig,
                sideHeaderDividerConfig
            )
        }
        sideDividerConfig != null && sideHeaderDividerConfig == null && sideFooterDividerConfig != null -> {
            GridDividerSideAndFooterHelper(
                dividerConfig,
                headerDividerConfig,
                footerDividerConfig,
                sideDividerConfig,
                sideFooterDividerConfig
            )
        }
        sideDividerConfig != null && sideHeaderDividerConfig == null && sideFooterDividerConfig == null -> {
            GridDividerOnlySideHelper(
                dividerConfig,
                headerDividerConfig,
                footerDividerConfig,
                sideDividerConfig
            )
        }
        else -> {
            GridDividerNoSideHelper(
                dividerConfig,
                headerDividerConfig,
                footerDividerConfig
            )
        }
    }
    private var reusableItemParams: GridItemParams? = null

    init {
        if ((headerDividerConfig != null || footerDividerConfig != null) && isFullSpanByPosition == null) {
            throw IllegalArgumentException(
                "Must be set the 'isFullSpanByPosition' property, because you configured 'headerDivider' or 'footerDivider'"
            )
        }
        if ((sideHeaderDividerConfig != null || sideFooterDividerConfig != null) && sideDividerConfig == null) {
            throw IllegalArgumentException("When sideHeaderDivider or sideFooterDivider not null, sideDivider cannot be null")
        }
        if (sideDividerConfig != null) {
            if (sideHeaderDividerConfig != null
                && !sideDividerConfig.itemDivider.compareSizeAndInsets(sideHeaderDividerConfig.itemDivider)
            ) {
                throw IllegalArgumentException("The size and insets of sideHeaderDivider must be the same as sideDivider")
            }
            if (sideFooterDividerConfig != null
                && !sideDividerConfig.itemDivider.compareSizeAndInsets(sideFooterDividerConfig.itemDivider)
            ) {
                throw IllegalArgumentException("The size and insets of sideHeaderDivider must be the same as sideDivider")
            }
        }

        sideDividerConfig?.run {
            if (personaliseByPositionArray != null) {
                val allSame = (0 until personaliseByPositionArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseByPositionArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedByPosition divider of the sideDivider must be the same as the sideDivider ")
                }
            }
            if (personaliseBySpanIndexArray != null) {
                val allSame = (0 until personaliseBySpanIndexArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseBySpanIndexArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedBySpanIndex divider of the sideDivider must be the same as the sideDivider ")
                }
            }
        }
        sideHeaderDividerConfig?.run {
            if (personaliseByPositionArray != null) {
                val allSame = (0 until personaliseByPositionArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseByPositionArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedByPosition divider of the sideHeaderDivider must be the same as the sideHeaderDivider ")
                }
            }
            if (personaliseBySpanIndexArray != null) {
                val allSame = (0 until personaliseBySpanIndexArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseBySpanIndexArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedBySpanIndex divider of the sideHeaderDivider must be the same as the sideHeaderDivider ")
                }
            }
        }
        sideFooterDividerConfig?.run {
            if (personaliseByPositionArray != null) {
                val allSame = (0 until personaliseByPositionArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseByPositionArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedByPosition divider of the sideFooterDivider must be the same as the sideFooterDivider ")
                }
            }
            if (personaliseBySpanIndexArray != null) {
                val allSame = (0 until personaliseBySpanIndexArray.size()).all { index ->
                    itemDivider.compareSizeAndInsets(personaliseBySpanIndexArray.valueAt(index))
                }
                if (!allSame) {
                    throw IllegalArgumentException("The size and insets of the personalizedBySpanIndex divider of the sideFooterDivider must be the same as the sideFooterDivider ")
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is StaggeredGridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be StaggeredGridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: return
        val isVerticalOrientation = layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
        val isLTRDirection = layoutManager.layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR
        val spanCount = layoutManager.spanCount
        val spanIndex = childLayoutParams.spanIndex
        val isFullSpan = childLayoutParams.isFullSpan
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1
        val spanSize = if (isFullSpan) spanCount else 1
        val isColumnFirst = if (isFullSpanByPosition != null && position < spanCount) {
            when {
                isFullSpan -> position == 0
                0.rangeTo(position).all { // The position before is all not fullSpan
                    !isFullSpanByPosition.isFullSpan(parent, it)
                } -> true
                else -> false
            }
        } else {
            false
        }
        val isColumnLast =
            if (isFullSpanByPosition != null && position >= itemCount - spanCount) {
                when {
                    isFullSpan -> position == itemCount - 1
                    position.until(itemCount).all { // The position back is all not fullSpan
                        !isFullSpanByPosition.isFullSpan(parent, it)
                    } -> true
                    else -> false
                }
            } else {
                false
            }
        val itemParams = this.reusableItemParams?.apply {
            set(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, isColumnFirst, isColumnLast,
                isVerticalOrientation, isLTRDirection
            )
        } ?: GridItemParams(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, isColumnFirst, isColumnLast,
            isVerticalOrientation, isLTRDirection
        ).apply {
            this@StaggeredGridDividerItemDecoration.reusableItemParams = this
        }
        dividerHelper.getItemOffsets(outRect, itemParams, true)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is StaggeredGridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be StaggeredGridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childCount = parent.childCount.takeIf { it != 0 } ?: return
        val isVerticalOrientation = layoutManager.orientation == GridLayoutManager.VERTICAL
        val isLTRDirection = layoutManager.layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR
        val spanCount = layoutManager.spanCount

        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanIndex = childLayoutParams.spanIndex
            val isFullSpan = childLayoutParams.isFullSpan
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1
            val spanSize = if (isFullSpan) spanCount else 1
            val isColumnFirst = if (isFullSpanByPosition != null && position < spanCount) {
                when {
                    isFullSpan -> position == 0
                    0.rangeTo(position).all { // The position before is all not fullSpan
                        !isFullSpanByPosition.isFullSpan(parent, it)
                    } -> true
                    else -> false
                }
            } else {
                false
            }
            val isColumnLast =
                if (isFullSpanByPosition != null && position >= itemCount - spanCount) {
                    when {
                        isFullSpan -> position == itemCount - 1
                        position.until(itemCount).all { // The position back is all not fullSpan
                            !isFullSpanByPosition.isFullSpan(parent, it)
                        } -> true
                        else -> false
                    }
                } else {
                    false
                }
            val itemParams = this.reusableItemParams?.apply {
                set(
                    view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                    isFullSpan, isFirstSpan, isLastSpan, isColumnFirst, isColumnLast,
                    isVerticalOrientation, isLTRDirection
                )
            } ?: GridItemParams(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, isColumnFirst, isColumnLast,
                isVerticalOrientation, isLTRDirection
            ).apply {
                this@StaggeredGridDividerItemDecoration.reusableItemParams = this
            }
            dividerHelper.drawItem(canvas, itemParams, true)
        }
    }

    class Builder(val context: Context) {

        private var dividerConfig: DividerConfig? = null
        private var headerDividerConfig: DividerConfig? = null
        private var footerDividerConfig: DividerConfig? = null
        private var useDividerAsHeaderDivider = false
        private var useDividerAsFooterDivider = false

        private var sideDividerConfig: DividerConfig? = null
        private var sideHeaderDividerConfig: DividerConfig? = null
        private var sideFooterDividerConfig: DividerConfig? = null
        private var useSideDividerAsSideHeaderDivider = false
        private var useSideDividerAsSideFooterDivider = false

        private var disableDefaultDivider = false
        private var isFullSpanByPosition: FullSpanSupport? = null

        fun build(): StaggeredGridDividerItemDecoration {
            if ((useSideDividerAsSideHeaderDivider || useSideDividerAsSideFooterDivider) && sideDividerConfig == null) {
                throw IllegalArgumentException("Must call the sideDivider() method to configure the sideDivider")
            }

            val finalDividerConfig = when {
                dividerConfig != null -> dividerConfig
                !disableDefaultDivider -> context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    DividerConfig.Builder(Divider.drawable(it)).build()
                }
                else -> null
            }
            if ((useDividerAsHeaderDivider || useDividerAsFooterDivider) && finalDividerConfig == null) {
                throw IllegalArgumentException("Must call the divider() method to configure the divider")
            }

            val headerDividerConfig = (headerDividerConfig
                ?: if (useDividerAsHeaderDivider) finalDividerConfig else null)
                ?.toItemDividerConfig(context)
            val footerDividerConfig = (footerDividerConfig
                ?: if (useDividerAsFooterDivider) finalDividerConfig else null)
                ?.toItemDividerConfig(context)
            val isFullSpanByPosition = isFullSpanByPosition
                ?: (if (headerDividerConfig != null || footerDividerConfig != null) FullSpanSupportFromLayoutManager() else null)
            return StaggeredGridDividerItemDecoration(
                dividerConfig = finalDividerConfig?.toItemDividerConfig(context),
                headerDividerConfig = headerDividerConfig,
                footerDividerConfig = footerDividerConfig,
                sideDividerConfig = sideDividerConfig?.toItemDividerConfig(context),
                sideHeaderDividerConfig = (sideHeaderDividerConfig
                    ?: if (useSideDividerAsSideHeaderDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context),
                sideFooterDividerConfig = (sideFooterDividerConfig
                    ?: if (useSideDividerAsSideFooterDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context),
                isFullSpanByPosition = isFullSpanByPosition
            )
        }


        /**
         * Set the divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun divider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider of the item
         */
        fun divider(config: DividerConfig): Builder {
            this.dividerConfig = config
            return this
        }


        /**
         * Set the header divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerDivider(config: DividerConfig): Builder {
            this.headerDividerConfig = config
            return this
        }


        /**
         * Set the footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun footerDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.footerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun footerDivider(config: DividerConfig): Builder {
            this.footerDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerAndFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.footerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerAndFooterDivider(config: DividerConfig): Builder {
            this.headerDividerConfig = config
            this.footerDividerConfig = config
            return this
        }


        /**
         * Use divider as the header divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsHeaderDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            return this
        }

        /**
         * Use divider as the footer divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsFooterDivider = use
            return this
        }

        /**
         * Use divider as the header and footer divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            this.useDividerAsFooterDivider = use
            return this
        }


        /**
         * Set the divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider on the side of the item
         */
        fun sideDivider(config: DividerConfig): Builder {
            this.sideDividerConfig = config
            return this
        }


        /**
         * Set the header divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider on the side of the item
         */
        fun sideHeaderDivider(config: DividerConfig): Builder {
            this.sideHeaderDividerConfig = config
            return this
        }


        /**
         * Set the footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideFooterDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider on the side of the item
         */
        fun sideFooterDivider(config: DividerConfig): Builder {
            this.sideFooterDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderAndFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.sideFooterDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider on the side of the item
         */
        fun sideHeaderAndFooterDivider(config: DividerConfig): Builder {
            this.sideHeaderDividerConfig = config
            this.sideFooterDividerConfig = config
            return this
        }


        /**
         * Use side divider as the header side divider
         */
        fun useSideDividerAsSideHeaderDivider(use: Boolean = true): Builder {
            this.useSideDividerAsSideHeaderDivider = use
            return this
        }

        /**
         * Use side divider as the footer side divider
         */
        fun useSideDividerAsSideFooterDivider(use: Boolean = true): Builder {
            this.useSideDividerAsSideFooterDivider = use
            return this
        }

        /**
         * Use side divider as the header and footer side divider
         */
        fun useSideDividerAsSideHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useSideDividerAsSideHeaderDivider = use
            this.useSideDividerAsSideFooterDivider = use
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
         * Set the interface for determining FullSpan based on position.
         * If you use [headerDivider], [footerDivider], [headerAndFooterDivider], [useDividerAsHeaderDivider],
         * [useDividerAsFooterDivider], [useDividerAsHeaderAndFooterDivider] then you must set this interface,
         * otherwise an exception will be thrown
         *
         * @see headerDivider
         * @see footerDivider
         * @see headerAndFooterDivider
         * @see useDividerAsHeaderDivider
         * @see useDividerAsFooterDivider
         * @see useDividerAsHeaderAndFooterDivider
         */
        @Deprecated(message= "Please use isFullSpanByPosition(FullSpanSupport?) instead")
        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }

        /**
         * Set the interface for determining FullSpan based on position.
         * If you use [headerDivider], [footerDivider], [headerAndFooterDivider], [useDividerAsHeaderDivider],
         * [useDividerAsFooterDivider], [useDividerAsHeaderAndFooterDivider] then you must set this interface,
         * otherwise an exception will be thrown
         *
         * @see headerDivider
         * @see footerDivider
         * @see headerAndFooterDivider
         * @see useDividerAsHeaderDivider
         * @see useDividerAsFooterDivider
         * @see useDividerAsHeaderAndFooterDivider
         */
        fun isFullSpanByPosition(isFullSpanByPosition: FullSpanSupport?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }
    }
}