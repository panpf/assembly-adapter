package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.recycler.paging.AssemblyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory

class MyLoadStateAdapter : AssemblyLoadStateAdapter(LoadStateItemFactory(), true)