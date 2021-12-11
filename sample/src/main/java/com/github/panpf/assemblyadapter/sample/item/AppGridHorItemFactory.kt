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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.decorInsets
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridHorBinding
import com.github.panpf.tools4k.lang.asOrNull
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppGridHorItemFactory(
    private val activity: Activity,
) : BindingItemFactory<AppInfo, ItemAppGridHorBinding>(AppInfo::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGridHorBinding {
        return ItemAppGridHorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppGridHorBinding,
        item: BindingItem<AppInfo, ItemAppGridHorBinding>
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
                    append("contentSize: ${binding.root.width}x${binding.root.height}").appendLine()
                    append("itemSize: ${binding.root.width}x${binding.root.height}").appendLine()
                    append("insets: ${binding.root.layoutParams.asOrNull<RecyclerView.LayoutParams>()?.decorInsets}")
                })
            }.show()
            true
        }

        binding.appGridHorItemIconImage.options.shaper = RoundRectImageShaper(
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
        binding: ItemAppGridHorBinding,
        item: BindingItem<AppInfo, ItemAppGridHorBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appGridHorItemIconImage.displayImage(appIconUri)
        binding.appGridHorItemNameText.text = data.name
    }
}
