/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter3.compat.sample.util

import android.content.Context
import android.content.pm.PackageManager
import com.github.panpf.assemblyadapter3.compat.sample.bean.AppGroup
import com.github.panpf.assemblyadapter3.compat.sample.bean.AppInfo
import java.util.*

class PinyinGroupAppsHelper(private val context: Context) {

    // Contains the following class types: AppGroup
    private val pinyinFlatAppList = loadInstalledAppList()

    private fun loadInstalledAppList(): List<AppGroup> {
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val appPackageList = packageInfoList.mapNotNull { packageInfo ->
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                ?: return@mapNotNull null
            AppInfo.fromPackageInfo(context, packageInfo)!!
        }.sortedBy { it.namePinyinLowerCase }

        return appPackageList
            .groupBy { it.namePinyinLowerCase.first().uppercase() }
            .map { it -> AppGroup(it.key, it.value.sortedBy { it.namePinyinLowerCase }) }
            .sortedBy { it.title }
    }

    val count = pinyinFlatAppList.size

    /**
     * Contains the following class types: AppGroup
     */
    fun getRange(fromIndex: Int, toIndexExclusive: Int): List<AppGroup> {
        if (fromIndex >= count) return emptyList()
        return pinyinFlatAppList.subList(fromIndex, toIndexExclusive.coerceAtMost(count))
    }

    /**
     * Contains the following class types: AppGroup
     */
    fun getAll() = pinyinFlatAppList
}