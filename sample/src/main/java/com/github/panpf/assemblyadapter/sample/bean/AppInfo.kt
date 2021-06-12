package com.github.panpf.assemblyadapter.sample.bean

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.github.panpf.assemblyadapter.paging.DiffKey
import com.github.promeg.pinyinhelper.Pinyin
import java.io.File
import java.util.*

data class AppInfo constructor(
    val packageName: String,
    val name: String,
    val namePinyin: String,
    val versionName: String,
    val versionCode: Int,
    val apkFilePath: String,
    val apkSize: Long,
    val systemApp: Boolean,
) : DiffKey {

    override val diffKey: String = packageName

    val namePinyinLowerCase by lazy { namePinyin.lowercase(Locale.getDefault()) }

    companion object {
        fun fromPackageInfo(context: Context, packageInfo: PackageInfo): AppInfo? {
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName) ?: return null
            val name =
                packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
            return AppInfo(
                packageName = packageInfo.packageName,
                name = name,
                namePinyin = Pinyin.toPinyin(name, ""),
                versionName = packageInfo.versionName.orEmpty(),
                versionCode = packageInfo.versionCode,
                apkFilePath = packageInfo.applicationInfo.publicSourceDir,
                apkSize = File(packageInfo.applicationInfo.publicSourceDir).length(),
                packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
            )
        }
    }
}
