package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import com.github.panpf.assemblyadapter.sample.base.StickyItemFactory

class PinyinGroupStickyItemFactory(activity: Activity, hideStartMargin: Boolean = false) :
    PinyinGroupItemFactory(activity, hideStartMargin), StickyItemFactory