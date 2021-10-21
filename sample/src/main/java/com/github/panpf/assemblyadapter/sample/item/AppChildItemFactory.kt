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
import androidx.core.view.ViewCompat
import com.github.panpf.assemblyadapter.list.expandable.BindingExpandableChildItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppChildItemFactory(private val activity: Activity, private val showBg: Boolean = false) :
    BindingExpandableChildItemFactory<AppGroup, AppInfo, ItemAppBinding>(AppInfo::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppBinding {
        return ItemAppBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppBinding,
        item: BindingExpandableChildItem<AppGroup, AppInfo, ItemAppBinding>
    ) {
        if (!showBg) {
            ViewCompat.setBackground(binding.root, null)
        }

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
                    append("groupBindingAdapterPosition: ${item.groupBindingAdapterPosition}").appendLine()
                    append("groupAbsoluteAdapterPosition: ${item.groupAbsoluteAdapterPosition}").appendLine()
                    append("groupData: ${item.groupDataOrThrow.title}").appendLine()
                    append("isLastChild: ${item.isLastChild}").appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("data: ${item.dataOrThrow.name}").appendLine()
                })
            }.show()
            true
        }

        binding.appItemIconImage.options.shaper = RoundRectImageShaper(
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
        binding: ItemAppBinding,
        item: BindingExpandableChildItem<AppGroup, AppInfo, ItemAppBinding>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: AppGroup,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo,
    ) {
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appItemIconImage.displayImage(appIconUri)
        binding.appItemNameText.text = data.name
        binding.appItemVersionText.text = "v${data.versionName}"
        binding.appItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
