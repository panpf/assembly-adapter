package com.github.panpf.assemblyadapter.sample.bean

data class AppInfo(
    val packageName: String,
    val name: String,
    val namePinyin: String,
    val versionName: String,
    val versionCode: Int,
    val apkFilePath: String,
    val apkSize: Long,
    val systemApp: Boolean,
)
