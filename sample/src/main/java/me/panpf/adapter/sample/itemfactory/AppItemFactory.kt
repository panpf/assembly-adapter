package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.sketch.SketchImageView
import me.panpf.sketch.uri.ApkIconUriModel
import me.panpf.sketch.uri.AppIconUriModel
import me.panpf.adapter.AssemblyRecyclerItem
import me.panpf.adapter.AssemblyRecyclerItemFactory
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.AppInfo
import me.panpf.adapter.sample.bindView

class AppItemFactory : AssemblyRecyclerItemFactory<AppItemFactory.AppItem>() {
    override fun isTarget(data: Any): Boolean {
        return data is AppInfo
    }

    override fun createAssemblyItem(viewGroup: ViewGroup): AppItem {
        return AppItem(R.layout.list_item_app, viewGroup)
    }

    inner class AppItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyRecyclerItem<AppInfo>(itemLayoutId, parent) {
        val iconImageView: SketchImageView by bindView (R.id.image_installedApp_icon)
        val nameTextView: TextView by bindView(R.id.text_installedApp_name)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(i: Int, appInfo: AppInfo) {
            if (appInfo.isTempInstalled) {
                iconImageView.displayImage(AppIconUriModel.makeUri(appInfo.id, appInfo.versionCode))
            } else {
                iconImageView.displayImage(ApkIconUriModel.makeUri(appInfo.apkFilePath))
            }
            nameTextView.text = appInfo.name
        }
    }
}
