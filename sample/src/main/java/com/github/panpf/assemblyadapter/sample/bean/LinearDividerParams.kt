package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.tools4a.dimen.ktx.dp2px

data class LinearDividerParams(
    var isShowDivider: Boolean = true,
    var isShowHeaderDivider: Boolean = true,
    var isShowFooterDivider: Boolean = true,
    var isShowSideHeaderDivider: Boolean = true,
    var isShowSideFooterDivider: Boolean = true,
    var isBigDivider: Boolean = true,
    var isShowDividerInsets: Boolean = false,
) {

    val dividerSize: Int
        get() = if (isBigDivider) 5f.dp2px else 2f.dp2px

    val dividerInsetsSize: Int
        get() = if (isShowDividerInsets) 2f.dp2px else 0f.dp2px
}