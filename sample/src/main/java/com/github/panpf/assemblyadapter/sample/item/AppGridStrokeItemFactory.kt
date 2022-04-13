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
package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.decorInsets
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.GridDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridStrokeBinding
import com.github.panpf.sketch.displayImage
import com.github.panpf.sketch.fetch.newAppIconUri
import com.github.panpf.tools4k.lang.asOrNull
import kotlin.math.roundToInt

class AppGridStrokeItemFactory(
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val dividerParamsData: MutableLiveData<GridDividerParams>
) : BindingItemFactory<AppInfo, ItemAppGridStrokeBinding>(AppInfo::class) {

    private var itemSize: Int = 0
    private var parent: RecyclerView? = null

    init {
        dividerParamsData.observe(lifecycleOwner) { dividerParams ->
            val parent = parent
            if (dividerParams != null && parent != null) {
                resetItemSize(parent, dividerParams)
            }
        }
    }

    private fun resetItemSize(parent: RecyclerView, dividerParams: GridDividerParams) {
        this.parent = parent
        itemSize = -1
        val spanCount = dividerParams.getSpanCount(true)
        if (spanCount > 1) {
            val parentWidth = parent.width
            val dividerCount = dividerParams.run {
                when {
                    isShowSideDivider && isShowSideHeaderDivider && isShowSideFooterDivider -> {
                        spanCount + 1
                    }
                    isShowSideDivider && !isShowSideHeaderDivider && !isShowSideFooterDivider -> {
                        spanCount - 1
                    }
                    isShowSideDivider && (isShowSideHeaderDivider || isShowSideFooterDivider) -> {
                        spanCount
                    }
                    !isShowSideDivider && (isShowSideHeaderDivider || isShowSideFooterDivider) -> {
                        1
                    }
                    else -> 0
                }
            }
            val dividerFinalSize =
                dividerParams.dividerSize + (dividerParams.dividerInsetsSize * 2f)
            itemSize = ((parentWidth - (dividerFinalSize * dividerCount)) / spanCount).roundToInt()
        }
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGridStrokeBinding {
        if (parent is RecyclerView) {
            resetItemSize(parent, dividerParamsData.value!!)
        }
        return ItemAppGridStrokeBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppGridStrokeBinding,
        item: BindingItem<AppInfo, ItemAppGridStrokeBinding>
    ) {
        binding.root.setOnClickListener {
            val data = item.dataOrThrow
            Toast.makeText(
                context,
                context.getString(R.string.toast_opened_app, data.name),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.root.setOnLongClickListener {
            val data = item.dataOrThrow
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("App（${data.name}）").appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("contentSize: ${binding.appGridStrokeItemContentLayout.width}x${binding.appGridStrokeItemContentLayout.height}").appendLine()
                    append("itemSize: ${binding.root.width}x${binding.root.height}").appendLine()
                    append("insets: ${binding.root.layoutParams.asOrNull<RecyclerView.LayoutParams>()?.decorInsets}")
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppGridStrokeBinding,
        item: BindingItem<AppInfo, ItemAppGridStrokeBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        val itemWidth = if (dividerParamsData.value?.compactMode != true) {
            itemSize
        } else {
            ViewGroup.LayoutParams.MATCH_PARENT
        }
        binding.appGridStrokeItemContentLayout.apply {
            if (layoutParams.width != itemWidth) {
                updateLayoutParams<ViewGroup.LayoutParams> {
                    width = itemWidth
                }
            }
        }

        val appIconUri = newAppIconUri(data.packageName, data.versionCode)
        binding.appGridStrokeItemIconImage.displayImage(appIconUri)
        binding.appGridStrokeItemNameText.text = data.name
    }
}
