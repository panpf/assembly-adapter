package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppItemFactory : BindingAssemblyItemFactory<AppInfo, ItemAppBinding>() {

    override fun match(data: Any?): Boolean {
        return data is AppInfo
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppBinding {
        return ItemAppBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppBinding>
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
        binding.appItemIconImage.options.shaper = RoundRectImageShaper(
            context.resources.getDimension(R.dimen.app_icon_corner_radius)
        ).apply {
            setStroke(
                ResourcesCompat.getColor(context.resources, R.color.app_icon_stroke, null),
                context.resources.getDimensionPixelSize(R.dimen.app_icon_stroke_width)
            )
        }
    }

    override fun bindData(
        context: Context, binding: ItemAppBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppBinding>,
        position: Int, data: AppInfo?
    ) {
        data ?: return
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appItemIconImage.displayImage(appIconUri)
        binding.appItemNameText.text = data.name
        binding.appItemVersionText.text = "v${data.versionName}"
        binding.appItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
    }
}
