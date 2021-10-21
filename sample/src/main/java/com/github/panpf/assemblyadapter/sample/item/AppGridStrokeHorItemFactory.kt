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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.GridDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridStrokeHorBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppGridStrokeHorItemFactory(
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val gridDividerParamsData: MutableLiveData<GridDividerParams>
) : BindingItemFactory<AppInfo, ItemAppGridStrokeHorBinding>(AppInfo::class) {

    private var itemSize: Int = 0
    private var parent: RecyclerView? = null

    init {
        gridDividerParamsData.observe(lifecycleOwner) { dividerParams ->
            val parent = parent
            if (dividerParams != null && parent != null) {
                resetItemSize(parent, dividerParams)
            }
        }
    }

    private fun resetItemSize(parent: RecyclerView, dividerParams: GridDividerParams) {
        this.parent = parent
        itemSize = -1
        val spanCount = when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
        if (spanCount > 1) {
            val parentHeight = parent.height
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
            val dividerFinalSize = dividerParams.dividerSize + (dividerParams.dividerInsetsSize * 2)
            itemSize = ((parentHeight - (dividerFinalSize * dividerCount)) / spanCount)
        }
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGridStrokeHorBinding {
        if (parent is RecyclerView) {
            resetItemSize(parent, gridDividerParamsData.value!!)
        }
        return ItemAppGridStrokeHorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppGridStrokeHorBinding,
        item: BindingItem<AppInfo, ItemAppGridStrokeHorBinding>
    ) {
        binding.root.setOnClickListener {
            val data = item.dataOrThrow
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(data.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }

        binding.root.setOnLongClickListener {
            val data = item.dataOrThrow
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("App（${data.name}）").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }

        binding.appGridStrokeHorItemIconImage.options.shaper = RoundRectImageShaper(
            context.resources.getDimension(R.dimen.app_icon_corner_radius)
        ).apply {
            setStroke(
                ResourcesCompat.getColor(context.resources, R.color.app_icon_stroke, null),
                context.resources.getDimensionPixelSize(R.dimen.app_icon_stroke_width)
            )
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppGridStrokeHorBinding,
        item: BindingItem<AppInfo, ItemAppGridStrokeHorBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        binding.appGridStrokeHorItemContentLayout.apply {
            if (layoutParams.height != itemSize) {
                updateLayoutParams<ViewGroup.LayoutParams> {
                    height = itemSize
                }
            }
        }

        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appGridStrokeHorItemIconImage.displayImage(appIconUri)
        binding.appGridStrokeHorItemNameText.text = data.name
    }
}
