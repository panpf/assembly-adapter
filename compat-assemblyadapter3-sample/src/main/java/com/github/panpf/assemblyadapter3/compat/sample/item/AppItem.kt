package com.github.panpf.assemblyadapter3.compat.sample.item

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.bean.AppInfo
import com.github.panpf.assemblyadapter3.compat.sample.databinding.ItemAppBinding
import me.panpf.sketch.uri.AppIconUriModel

class AppItem(val binding: ItemAppBinding) : CompatAssemblyItem<AppInfo>(binding.root) {

    override fun onSetData(position: Int, data: AppInfo?) {
        data ?: return

        binding.appItemIconImage.displayImage(AppIconUriModel.makeUri(data.packageName, data.versionCode))
        binding.appItemNameText.text = data.name
        binding.appItemSizeText.text = Formatter.formatFileSize(context, data.apkSize)
        binding.appItemVersionText.text = data.versionName
    }

    class Factory : CompatAssemblyItemFactory<AppInfo>() {

        init {
            setOnItemClickListener {context, _, _, _, data ->
                val launchIntent = context.packageManager.getLaunchIntentForPackage(requireNotNull(data).packageName.orEmpty())
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

        override fun createAssemblyItem(parent: ViewGroup): AppItem {
            return AppItem(ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
}
