package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppItemFactory(private val activity: Activity) :
    BindingItemFactory<AppInfo, ItemAppBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppInfo
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppBinding {
        return ItemAppBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppBinding,
        item: Item<AppInfo>
    ) {
        super.initItem(context, binding, item)
        binding.root.setOnClickListener {
            val data = item.dataOrNull ?: return@setOnClickListener
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(data.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }

        binding.root.setOnLongClickListener {
            val data = item.dataOrNull ?: return@setOnLongClickListener false
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

        binding.appItemIconImage.options.shaper = RoundRectImageShaper(
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
        binding: ItemAppBinding,
        item: Item<AppInfo>,
        bindingAdapterPosition: Int,
        data: AppInfo
    ) {
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appItemIconImage.displayImage(appIconUri)
        binding.appItemNameText.text = data.name
        binding.appItemVersionText.text = "v${data.versionName}"
        binding.appItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
