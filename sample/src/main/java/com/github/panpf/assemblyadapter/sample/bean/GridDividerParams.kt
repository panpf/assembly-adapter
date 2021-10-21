package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.tools4a.dimen.ktx.dp2px
import kotlinx.serialization.Serializable

@Serializable
data class GridDividerParams(
    var isShowDivider: Boolean = true,
    var isShowHeaderDivider: Boolean = true,
    var isShowFooterDivider: Boolean = true,
    var isShowSideDivider: Boolean = true,
    var isShowSideHeaderDivider: Boolean = true,
    var isShowSideFooterDivider: Boolean = true,
    var isBigDivider: Boolean = true,
    var isShowDividerInsets: Boolean = false,
    var isShowListSeparator: Boolean = true,
) {

    val dividerSize: Int
        get() = if (isBigDivider) 5f.dp2px else 2f.dp2px

    val dividerInsetsSize: Int
        get() = if (isShowDividerInsets) 2f.dp2px else 0f.dp2px
}