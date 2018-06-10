package me.panpf.adapter.sample.item

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.AppInfo
import me.panpf.sketch.SketchImageView
import me.panpf.sketch.uri.ApkIconUriModel
import me.panpf.sketch.uri.AppIconUriModel

class AppItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppInfo>(itemLayoutId, parent) {
    private val iconImageView: SketchImageView by bindView(R.id.appItem_iconImage)
    private val nameTextView: TextView by bindView(R.id.appItem_nameText)

    override fun onSetData(i: Int, appInfo: AppInfo?) {
        appInfo ?: return

        if (appInfo.isTempInstalled) {
            iconImageView.displayImage(AppIconUriModel.makeUri(appInfo.id, appInfo.versionCode))
        } else {
            iconImageView.displayImage(ApkIconUriModel.makeUri(appInfo.apkFilePath))
        }
        nameTextView.text = appInfo.name
    }

    class Factory : AssemblyItemFactory<AppInfo>() {

        init {
            setOnItemClickListener {context, view, position, positionInPart, data ->
                val launchIntent = context.packageManager.getLaunchIntentForPackage(data?.packageName)
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                } else {
                    Toast.makeText(context, "无法启动 ${data?.name}", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun match(data: Any?): Boolean {
            return data is AppInfo
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppItem {
            return AppItem(R.layout.item_app, viewGroup)
        }
    }
}
