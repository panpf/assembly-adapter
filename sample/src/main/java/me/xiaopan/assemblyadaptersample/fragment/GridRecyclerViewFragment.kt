package me.xiaopan.assemblyadaptersample.fragment

import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.AppInfo
import me.xiaopan.assemblyadaptersample.itemfactory.AppItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.AppListHeaderItemFactory
import me.xiaopan.assemblyadaptersample.bindView
import java.io.File
import java.util.*

class GridRecyclerViewFragment : Fragment() {

    val recyclerView: RecyclerView by bindView(R.id.list_recyclerViewFragment_content)
    
    var adapter: AssemblyRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(activity, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = recyclerView.adapter
                if (adapter == null || adapter !is AssemblyRecyclerAdapter) {
                    return 1
                }
                return adapter.getSpanSize(position)
            }
        }
        recyclerView.layoutManager = gridLayoutManager

        if (adapter != null) {
            recyclerView.adapter = adapter
        } else {
            loadAppList()
        }
    }

    private fun loadAppList() {
        object : AsyncTask<Int, Int, Array<List<AppInfo>>>() {
            private val context = activity.baseContext

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Int?): Array<List<AppInfo>>? {
                val packageManager = context.packageManager
                val packageInfoList = packageManager.getInstalledPackages(0)
                val systemAppList = ArrayList<AppInfo>()
                val userAppList = ArrayList<AppInfo>()
                for (packageInfo in packageInfoList) {
                    val appInfo = AppInfo(true)
                    appInfo.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    appInfo.sortName = appInfo.name
                    appInfo.id = packageInfo.packageName
                    appInfo.versionName = packageInfo.versionName
                    appInfo.apkFilePath = packageInfo.applicationInfo.publicSourceDir
                    appInfo.appSize = Formatter.formatFileSize(context, File(appInfo.apkFilePath).length())
                    appInfo.versionCode = packageInfo.versionCode
                    if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                        systemAppList.add(appInfo)
                    } else {
                        userAppList.add(appInfo)
                    }
                }

                Collections.sort(systemAppList) { lhs, rhs ->
                    (lhs.sortName ?: "").compareTo(rhs.sortName ?: "")
                }

                return arrayOf<List<AppInfo>>(systemAppList, userAppList)
            }

            override fun onPostExecute(appInfoLists: Array<List<AppInfo>>) {
                if (activity == null) {
                    return
                }

                val systemAppList = appInfoLists[0]
                val userAppList = appInfoLists[1]

                val systemAppListSize = systemAppList?.size ?: 0
                val userAppListSize = userAppList?.size ?: 0

                var dataListSize = if (systemAppListSize > 0) systemAppListSize + 1 else 0
                dataListSize += if (userAppListSize > 0) userAppListSize + 1 else 0

                val dataList = ArrayList<Any>(dataListSize)
                if (userAppListSize > 0) {
                    dataList.add(String.format("自安装应用%d个", userAppListSize))
                    dataList.addAll(userAppList)
                }
                if (systemAppListSize > 0) {
                    dataList.add(String.format("系统应用%d个", systemAppListSize))
                    dataList.addAll(systemAppList)
                }
                val adapter = AssemblyRecyclerAdapter(dataList)
                adapter.addItemFactory(AppItemFactory())
                adapter.addItemFactory(AppListHeaderItemFactory().fullSpan(recyclerView))
                recyclerView.adapter = adapter
                recyclerView.scheduleLayoutAnimation()
                this@GridRecyclerViewFragment.adapter = adapter
            }
        }.execute(0)
    }
}
