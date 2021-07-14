package com.github.panpf.assemblyadapter3.compat.sample.bean

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.github.promeg.pinyinhelper.Pinyin
import com.github.panpf.assemblyadapter3.compat.CompatDiffable
import java.io.File
import java.util.*

/**
 * App信息
 */
class AppInfo(
    val packageName: String,
    val name: String,
    val namePinyin: String,
    val versionName: String,
    val versionCode: Int,
    val apkFilePath: String,
    val apkSize: Long,
    val systemApp: Boolean
) : CompatDiffable<AppInfo> {

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

    override fun areItemsTheSame(other: AppInfo): Boolean {
        return this.packageName == other.packageName && this.versionCode == other.versionCode
    }

    override fun areContentsTheSame(other: AppInfo): Boolean {
        return this == other
    }
}
