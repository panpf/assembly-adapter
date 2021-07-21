package com.github.panpf.assemblyadapter3.compat.sample.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.bean.AppInfo
import com.github.panpf.assemblyadapter3.compat.sample.databinding.ItemAppGridBinding
import me.panpf.sketch.uri.AppIconUriModel

class AppGridItem(private val binding: ItemAppGridBinding) :
    CompatAssemblyItem<AppInfo>(binding.root) {

    override fun onSetData(position: Int, data: AppInfo?) {
        data ?: return

        binding.appGridCardItemIconImage.displayImage(
            AppIconUriModel.makeUri(
                data.packageName,
                data.versionCode
            )
        )
        binding.appGridCardItemNameText.text = data.name
    }

    class Factory : CompatAssemblyItemFactory<AppInfo>() {

        init {
            setOnItemClickListener { context, _, _, _, data ->
                val launchIntent =
                    context.packageManager.getLaunchIntentForPackage(requireNotNull(data).packageName)
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                } else {
                    Toast.makeText(context, "无法启动 ${data.name}", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun match(data: Any?): Boolean {
            return data is AppInfo
        }

        override fun createAssemblyItem(parent: ViewGroup): AppGridItem {
            return AppGridItem(
                ItemAppGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
