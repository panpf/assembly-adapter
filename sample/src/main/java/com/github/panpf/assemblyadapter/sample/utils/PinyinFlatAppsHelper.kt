package com.github.panpf.assemblyadapter.sample.utils

import android.content.Context
import android.content.pm.PackageManager
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import java.util.*

class PinyinFlatAppsHelper(private val context: Context) {

    // Contains the following class types: PinyinGroup、AppInfo
    private val pinyinFlatAppList = loadInstalledAppList()

    private fun loadInstalledAppList(): List<Any> {
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val appPackageList = packageInfoList.mapNotNull { packageInfo ->
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                ?: return@mapNotNull null
            AppInfo.fromPackageInfo(context, packageInfo)!!
        }.sortedBy { it.namePinyinLowerCase }

        return ArrayList<Any>().apply {
            var lastPinyinFirstChar: Char? = null
            appPackageList.forEach { app ->
                val namePinyinFirstChar = app.namePinyin.first().uppercaseChar()
                if (lastPinyinFirstChar == null || namePinyinFirstChar != lastPinyinFirstChar) {
                    add(ListSeparator(namePinyinFirstChar.toString()))
                    lastPinyinFirstChar = namePinyinFirstChar
                }
                add(app)
            }
        }
    }

    val count = pinyinFlatAppList.size

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    fun getRange(fromIndex: Int, toIndexExclusive: Int): List<Any> {
        return pinyinFlatAppList.subList(fromIndex, toIndexExclusive)
    }

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    fun getAll() = pinyinFlatAppList
}