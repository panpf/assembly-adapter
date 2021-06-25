package com.github.panpf.assemblyadapter.sample.base

import android.app.Activity
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory

class MyLoadStateAdapter(activity: Activity) : AssemblyLoadStateAdapter(LoadStateItemFactory(activity), true)