package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import com.github.panpf.assemblyadapter.sample.base.StickyItemFactory

class ListSeparatorStickyItemFactory(activity: Activity, hideStartMargin: Boolean = false) :
    ListSeparatorItemFactory(activity, hideStartMargin), StickyItemFactory