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
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.decorInsets
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.LinearDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppStrokeHorBinding
import com.github.panpf.sketch.displayImage
import com.github.panpf.sketch.fetch.newAppIconUri
import com.github.panpf.tools4k.lang.asOrNull
import kotlin.math.roundToInt

class AppStrokeHorItemFactory(
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val dividerParamsData: MutableLiveData<LinearDividerParams>
) : BindingItemFactory<AppInfo, ItemAppStrokeHorBinding>(AppInfo::class) {

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
        val dividerFinalSize = dividerParams.dividerSize + (dividerParams.dividerInsetsSize * 2f)
        val dividerCount = dividerParams.run {
            (if (isShowSideHeaderDivider) 1 else 0) + (if (isShowSideFooterDivider) 1 else 0)
        }
        itemSize = parent.height - (dividerFinalSize * dividerCount).roundToInt()
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppStrokeHorBinding {
        if (parent is RecyclerView) {
            resetItemSize(parent, dividerParamsData.value!!)
        }
        return ItemAppStrokeHorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppStrokeHorBinding,
        item: BindingItem<AppInfo, ItemAppStrokeHorBinding>
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
                    append("contentSize: ${binding.appStrokeHorItemContentLayout.width}x${binding.appStrokeHorItemContentLayout.height}").appendLine()
                    append("itemSize: ${binding.root.width}x${binding.root.height}").appendLine()
                    append("insets: ${binding.root.layoutParams.asOrNull<RecyclerView.LayoutParams>()?.decorInsets}")
                })
            }.show()
            true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindItemData(
        context: Context,
        binding: ItemAppStrokeHorBinding,
        item: BindingItem<AppInfo, ItemAppStrokeHorBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppInfo
    ) {
        binding.appStrokeHorItemContentLayout.apply {
            if (layoutParams.height != itemSize) {
                updateLayoutParams<ViewGroup.LayoutParams> {
                    height = itemSize
                }
            }
        }

        val appIconUri = newAppIconUri(data.packageName, data.versionCode)
        binding.appStrokeHorItemIconImage.displayImage(appIconUri)
        binding.appStrokeHorItemNameText.text = data.name
        binding.appStrokeHorItemVersionText.text = "v${data.versionName}"
        binding.appStrokeHorItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
