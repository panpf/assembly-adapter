package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridCardBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppCardGridItemFactory(private val activity: Activity) :
    BindingAssemblyItemFactory<AppInfo, ItemAppGridCardBinding>() {

    override fun match(data: Any?): Boolean {
        return data is AppInfo
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGridCardBinding {
        return ItemAppGridCardBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppGridCardBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppGridCardBinding>
    ) {
        super.initItem(context, binding, item)
        binding.root.setOnClickListener {
            val data = item.data ?: return@setOnClickListener
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(data.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }

        binding.root.setOnLongClickListener {
            val data = item.data ?: return@setOnLongClickListener false
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

        binding.appGridCardItemIconImage.options.shaper = RoundRectImageShaper(
            context.resources.getDimension(R.dimen.app_icon_corner_radius)
        ).apply {
            setStroke(
                ResourcesCompat.getColor(context.resources, R.color.app_icon_stroke, null),
                context.resources.getDimensionPixelSize(R.dimen.app_icon_stroke_width)
            )
        }
    }

    override fun bindData(
        context: Context, binding: ItemAppGridCardBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppGridCardBinding>,
        bindingAdapterPosition: Int,
        data: AppInfo?
    ) {
        data ?: return
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appGridCardItemIconImage.displayImage(appIconUri)
        binding.appGridCardItemNameText.text = data.name
    }
}
