package me.panpf.adapter.sample.bean

/**
 * App信息
 */
class AppInfo(val isTempInstalled: Boolean) {
    var packageName: String? = null
    var name: String? = null
    var id: String? = null
    var versionName: String? = null
    var appSize: String? = null
    var sortName: String? = null
    var apkFilePath: String? = null
    var versionCode: Int = 0
}
