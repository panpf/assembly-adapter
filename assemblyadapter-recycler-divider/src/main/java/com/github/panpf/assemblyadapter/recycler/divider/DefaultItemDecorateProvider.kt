package com.github.panpf.assemblyadapter.recycler.divider

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate

open class DefaultItemDecorateProvider(
    private val dividerItemDecorate: ItemDecorate,
    private val firstDividerItemDecorate: ItemDecorate?,
    private val lastDividerItemDecorate: ItemDecorate?,
    private val startSideItemDecorate: ItemDecorate?,
    private val endSideItemDecorate: ItemDecorate?,
) : ItemDecorateProvider {

    override fun getItemDecorate(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        verticalOrientation: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate? {
        val isFirst = position == 0
        val isLast = position == itemCount - 1
        return if (verticalOrientation) {
            when (decorateType) {
                ItemDecorate.Type.START -> startSideItemDecorate
                ItemDecorate.Type.TOP -> if (isFirst) firstDividerItemDecorate else null
                ItemDecorate.Type.END -> endSideItemDecorate
                ItemDecorate.Type.BOTTOM -> if (isLast) lastDividerItemDecorate else dividerItemDecorate
            }
        } else {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirst) firstDividerItemDecorate else null
                ItemDecorate.Type.TOP -> startSideItemDecorate
                ItemDecorate.Type.END -> if (isLast) lastDividerItemDecorate else dividerItemDecorate
                ItemDecorate.Type.BOTTOM -> endSideItemDecorate
            }
        }
    }
}