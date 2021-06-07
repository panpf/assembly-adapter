package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGridBinding
import me.panpf.sketch.shaper.RoundRectImageShaper
import me.panpf.sketch.uri.AppIconUriModel

class AppGridItemFactory : BindingAssemblyItemFactory<AppInfo, ItemAppGridBinding>() {

    override fun match(data: Any?): Boolean {
        return data is AppInfo
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGridBinding {
        return ItemAppGridBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context, binding: ItemAppGridBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppGridBinding>
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
        binding.appGridItemIconImage.options.shaper = RoundRectImageShaper(
            context.resources.getDimension(R.dimen.app_icon_corner_radius)
        ).apply {
            setStroke(
                ResourcesCompat.getColor(context.resources, R.color.app_icon_stroke, null),
                context.resources.getDimensionPixelSize(R.dimen.app_icon_stroke_width)
            )
        }
    }

    override fun bindData(
        context: Context, binding: ItemAppGridBinding,
        item: BindingAssemblyItem<AppInfo, ItemAppGridBinding>,
        position: Int, data: AppInfo?
    ) {
        data ?: return
        val appIconUri = AppIconUriModel.makeUri(data.packageName, data.versionCode)
        binding.appGridItemIconImage.displayImage(appIconUri)
        binding.appGridItemNameText.text = data.name
    }
}
