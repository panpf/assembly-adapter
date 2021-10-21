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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.decorInsets
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.LinearDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppStrokeBinding
import com.github.panpf.tools4k.lang.asOrNull
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppStrokeItemFactory(
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val dividerParamsData: MutableLiveData<LinearDividerParams>
) : BindingItemFactory<AppInfo, ItemAppStrokeBinding>(AppInfo::class) {

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

    private fun resetItemSize(parent: RecyclerView, dividerParams: LinearDividerParams) {
        this.parent = parent
        val dividerFinalSize = dividerParams.dividerSize + (dividerParams.dividerInsetsSize * 2)
        val dividerCount = dividerParams.run {
            (if (isShowSideHeaderDivider) 1 else 0) + (if (isShowSideFooterDivider) 1 else 0)
        }
        itemSize = parent.width - (dividerFinalSize * dividerCount)
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppStrokeBinding {
        if (parent is RecyclerView) {
            resetItemSize(parent, dividerParamsData.value!!)
        }
        return ItemAppStrokeBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppStrokeBinding,
        item: BindingItem<AppInfo, ItemAppStrokeBinding>
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
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("data: ${item.dataOrThrow.name}").appendLine()
                    append("contentSize: ${binding.appStrokeItemContentLayout.width}x${binding.appStrokeItemContentLayout.height}").appendLine()
                    append("itemSize: ${binding.root.width}x${binding.root.height}").appendLine()
                    append("insets: ${binding.root.layoutParams.asOrNull<RecyclerView.LayoutParams>()?.decorInsets}")
                })
            }.show()
            true
        }

        binding.appStrokeItemIconImage.options.shaper = RoundRectImageShaper(
            context.resources.getDimension(R.dimen.app_icon_corner_radius)
        ).apply {
            setStroke(
                ResourcesCompat.getColor(context.resources, R.color.app_icon_stroke, null),
                context.resources.getDimensionPixelSize(R.dimen.app_icon_stroke_width)
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindItemData(
        context: Context,
        binding: ItemAppStrokeBinding,
        item: BindingItem<AppInfo, ItemAppStrokeBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        binding.appStrokeItemContentLayout.apply {
            if (layoutParams.width != itemSize) {
                updateLayoutParams<ViewGroup.LayoutParams> {
                    width = itemSize
                }
            }
        }

        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appStrokeItemIconImage.displayImage(appIconUri)
        binding.appStrokeItemNameText.text = data.name
        binding.appStrokeItemVersionText.text = "v${data.versionName}"
        binding.appStrokeItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
