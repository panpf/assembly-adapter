package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.bean.AppInfo
import me.panpf.sketch.SketchImageView
import me.panpf.sketch.uri.ApkIconUriModel
import me.panpf.sketch.uri.AppIconUriModel

class AppItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppInfo>(itemLayoutId, parent) {
    private val iconImageView: SketchImageView by bindView(R.id.appItem_iconImage)
    private val nameTextView: TextView by bindView(R.id.appItem_nameText)

    override fun onSetData(i: Int, appInfo: AppInfo?) {
        appInfo ?: return

        if (appInfo.isTempInstalled) {
            iconImageView.displayImage(AppIconUriModel.makeUri(appInfo.id.orEmpty(), appInfo.versionCode))
        } else {
            iconImageView.displayImage(ApkIconUriModel.makeUri(appInfo.apkFilePath.orEmpty()))
        }
        nameTextView.text = appInfo.name
    }

    class Factory : AssemblyItemFactory<AppInfo>() {

        init {
            setOnItemClickListener {context, view, position, positionInPart, data ->
                val launchIntent = context.packageManager.getLaunchIntentForPackage(requireNotNull(data).packageName.orEmpty())
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
