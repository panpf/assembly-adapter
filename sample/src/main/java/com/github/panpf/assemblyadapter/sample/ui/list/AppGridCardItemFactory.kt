package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridCardBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppGridCardItemFactory : BindingAssemblyItemFactory<AppInfo, ItemAppGridCardBinding>() {

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
        position: Int, data: AppInfo?
    ) {
        data ?: return
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appGridCardItemIconImage.displayImage(appIconUri)
        binding.appGridCardItemNameText.text = data.name
    }
}
