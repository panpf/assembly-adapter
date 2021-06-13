package com.github.panpf.assemblyadapter.sample.bean

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey

data class Link(val title: String, val fragment: Fragment) : DiffKey {

    override val diffKey: String = title
}