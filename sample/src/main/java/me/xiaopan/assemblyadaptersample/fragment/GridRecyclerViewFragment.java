package me.xiaopan.assemblyadaptersample.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.AppInfo;
import me.xiaopan.assemblyadaptersample.itemfactory.AppItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.AppListHeaderItemFactory;

public class GridRecyclerViewFragment extends Fragment {

    private AssemblyRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerViewFragment_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                return adapter != null && adapter instanceof AssemblyRecyclerAdapter
                        ? ((AssemblyRecyclerAdapter) adapter).getSpanSize(position) : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        } else {
            loadAppList();
        }
    }

    private void loadAppList() {
        new AsyncTask<Integer, Integer, List<AppInfo>[]>() {
            private Context context = getActivity().getBaseContext();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<AppInfo>[] doInBackground(Integer... params) {
                PackageManager packageManager = context.getPackageManager();
                List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
                List<AppInfo> systemAppList = new ArrayList<AppInfo>();
                List<AppInfo> userAppList = new ArrayList<AppInfo>();
                for (PackageInfo packageInfo : packageInfoList) {
                    AppInfo appInfo = new AppInfo(true);
                    appInfo.setName(String.valueOf(packageInfo.applicationInfo.loadLabel(packageManager)));
                    appInfo.setSortName(appInfo.getName());
                    appInfo.setId(packageInfo.packageName);
                    appInfo.setVersionName(packageInfo.versionName);
                    appInfo.setApkFilePath(packageInfo.applicationInfo.publicSourceDir);
                    appInfo.setAppSize(Formatter.formatFileSize(context, new File(appInfo.getApkFilePath()).length()));
                    appInfo.setVersionCode(packageInfo.versionCode);
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        systemAppList.add(appInfo);
                    } else {
                        userAppList.add(appInfo);
                    }
                }

                Collections.sort(systemAppList, new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo lhs, AppInfo rhs) {
                        return lhs.getSortName().compareToIgnoreCase(rhs.getSortName());
                    }
                });

                return new List[]{systemAppList, userAppList};
            }

            @Override
            protected void onPostExecute(List<AppInfo>[] appInfoLists) {
                if (getActivity() == null) {
                    return;
                }

                List<AppInfo> systemAppList = appInfoLists[0];
                List<AppInfo> userAppList = appInfoLists[1];

                int systemAppListSize = systemAppList != null ? systemAppList.size() : 0;
                int userAppListSize = userAppList != null ? userAppList.size() : 0;

                int dataListSize = systemAppListSize > 0 ? systemAppListSize + 1 : 0;
                dataListSize += userAppListSize > 0 ? userAppListSize + 1 : 0;

                List<Object> dataList = new ArrayList<Object>(dataListSize);
                if (userAppListSize > 0) {
                    dataList.add(String.format("自安装应用%d个", userAppListSize));
                    dataList.addAll(userAppList);
                }
                if (systemAppListSize > 0) {
                    dataList.add(String.format("系统应用%d个", systemAppListSize));
                    dataList.addAll(systemAppList);
                }
                AssemblyRecyclerAdapter adapter = new AssemblyRecyclerAdapter(dataList);
                adapter.addItemFactory(new AppItemFactory());
                adapter.addItemFactory(new AppListHeaderItemFactory(recyclerView));
                recyclerView.setAdapter(adapter);
                recyclerView.scheduleLayoutAnimation();
                GridRecyclerViewFragment.this.adapter = adapter;
            }
        }.execute(0);
    }
}
