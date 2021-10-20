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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppStrokeBinding
import com.github.panpf.assemblyadapter.sample.util.TwoCombineMediatorLiveData
import com.github.panpf.tools4a.dimen.ktx.dp2px
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppStrokeItemFactory(
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val dividerSizeDpData: LiveData<Float>,
    private val dividerInsetsDpData: LiveData<Float>,
) : BindingItemFactory<AppInfo, ItemAppStrokeBinding>(AppInfo::class) {
    
    private var itemSize: Int = 0
    private var parent: RecyclerView? = null
    private val dividerParamsData = TwoCombineMediatorLiveData(
        dividerSizeDpData,
        dividerInsetsDpData,
        initValue = true
    )

    init {
        dividerParamsData.observe(lifecycleOwner) {
            val parent = parent
            if (it != null && parent != null) {
                resetItemSize(parent, it.t1!!.dp2px, it.t2!!.dp2px)
            }
        }
    }

    private fun resetItemSize(parent: RecyclerView, dividerSize: Int, dividerInsets: Int) {
        this.parent = parent
        itemSize = parent.width - ((dividerSize + (dividerInsets * 2)) * 2)
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppStrokeBinding {
        if (parent is RecyclerView) {
            resetItemSize(
                parent,
                dividerSizeDpData.value!!.dp2px,
                dividerInsetsDpData.value!!.dp2px
            )
        }
        return ItemAppStrokeBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppStrokeBinding, item: BindingItem<AppInfo, ItemAppStrokeBinding>
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
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("data: ${item.dataOrThrow.name}").appendLine().appendLine()
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
