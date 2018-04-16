package me.panpf.adapter.sample.fragment

import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.adapter.recycler.AssemblyGridLayoutManager
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.AppInfo
import me.panpf.adapter.sample.bindView
import me.panpf.adapter.sample.itemfactory.AppItemFactory
import me.panpf.adapter.sample.itemfactory.AppListHeaderItemFactory
import java.io.File
import java.lang.ref.WeakReference
import java.util.*

class GridRecyclerViewFragment : Fragment() {

    val recyclerView: RecyclerView by bindView(R.id.list_recyclerViewFragment_content)
    lateinit var adapter: AssemblyRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context ?: return
        recyclerView.layoutManager = AssemblyGridLayoutManager(context, 4, recyclerView)

        adapter = AssemblyRecyclerAdapter().apply {
            addItemFactory(AppItemFactory())
            addItemFactory(AppListHeaderItemFactory().fullSpan(recyclerView))
        }
        recyclerView.adapter = adapter

        LoadDataTask(WeakReference(this)).execute(0)
    }

    class LoadDataTask(private val fragmentRef: WeakReference<GridRecyclerViewFragment>) : AsyncTask<Int, Int, Array<List<AppInfo>>>() {
        override fun doInBackground(vararg params: Int?): Array<List<AppInfo>>? {
            val fragment = fragmentRef.get() ?: return null
            val appContext = fragment.context?.applicationContext ?: return null

            val packageManager = appContext.packageManager
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
                appInfo.appSize = Formatter.formatFileSize(appContext, File(appInfo.apkFilePath).length())
                appInfo.versionCode = packageInfo.versionCode
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                    systemAppList.add(appInfo)
                } else {
                    userAppList.add(appInfo)
                }
            }

            systemAppList.sortWith(Comparator { lhs, rhs ->
                (lhs.sortName ?: "").compareTo(rhs.sortName ?: "")
            })

            return arrayOf(systemAppList, userAppList)
        }

        override fun onPostExecute(appInfoLists: Array<List<AppInfo>>) {
            val fragment = fragmentRef.get() ?: return

            fragment.apply {
                val systemAppList = appInfoLists[0]
                val userAppList = appInfoLists[1]

                val systemAppListSize = systemAppList.size
                val userAppListSize = userAppList.size

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

                adapter.dataList = dataList
                recyclerView.scheduleLayoutAnimation()
            }
        }
    }
}
