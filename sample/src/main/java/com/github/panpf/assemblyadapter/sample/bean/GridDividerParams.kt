package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.tools4a.dimen.ktx.dp2px
import kotlinx.serialization.Serializable

@Serializable
class GridDividerParams {

    private var _isShowDivider: Boolean = true
    private var _isShowHeaderDivider: Boolean = true
    private var _isShowFooterDivider: Boolean = true
    private var _isShowSideDivider: Boolean = true
    private var _isShowSideHeaderDivider: Boolean = true
    private var _isShowSideFooterDivider: Boolean = true
    private var _isBigDivider: Boolean = true
    private var _isShortDivider: Boolean = false
    private var _isShowDividerInsets: Boolean = false
    private var _isShowListSeparator: Boolean = true
    private var _isLessSpan: Boolean = false
    private var _compactMode: Boolean = false

    var isShowDivider: Boolean
        get() = _isShowDivider
        set(value) {
            _isShowDivider = value
        }
    var isShowHeaderDivider: Boolean
        get() = _isShowHeaderDivider
        set(value) {
            _isShowHeaderDivider = value
        }
    var isShowFooterDivider: Boolean
        get() = _isShowFooterDivider
        set(value) {
            _isShowFooterDivider = value
        }
    var isShowSideDivider: Boolean
        get() = _isShowSideDivider
        set(value) {
            _isShowSideDivider = value
        }
    var isShowSideHeaderDivider: Boolean
        get() = _isShowSideHeaderDivider && _isShowSideDivider
        set(value) {
            _isShowSideHeaderDivider = value
        }
    var isShowSideFooterDivider: Boolean
        get() = _isShowSideFooterDivider && _isShowSideDivider
        set(value) {
            _isShowSideFooterDivider = value
        }
    var isBigDivider: Boolean
        get() = _isBigDivider
        set(value) {
            _isBigDivider = value
        }
    var isShortDivider: Boolean
        get() = _isShortDivider
        set(value) {
            _isShortDivider = value
        }
    var isShowDividerInsets: Boolean
        get() = _isShowDividerInsets
        set(value) {
            _isShowDividerInsets = value
        }
    var isShowListSeparator: Boolean
        get() = _isShowListSeparator && !compactMode
        set(value) {
            _isShowListSeparator = value
        }
    var isLessSpan: Boolean
        get() = _isLessSpan
        set(value) {
            _isLessSpan = value
        }
    var compactMode: Boolean
        get() = _compactMode
        set(value) {
            _compactMode = value
        }

    val dividerSize: Int
        get() = if (isBigDivider) 5f.dp2px else 2f.dp2px

    val dividerInsetsSize: Int
        get() = if (isShowDividerInsets) 2f.dp2px else 0f.dp2px

    val isShowAppsOverview: Boolean
        get() = !compactMode

    val spanSizeByPosition: Map<Int, ItemSpan>?
        get() = if (compactMode) {
            HashMap<Int, ItemSpan>().apply {
                put(4, ItemSpan.span(2))
                put(8, ItemSpan.span(2))
                put(12, ItemSpan.span(2))
                put(13, ItemSpan.span(3))
                put(16, ItemSpan.span(3))
                put(17, ItemSpan.fullSpan())
            }
        } else {
            null
        }

    fun getSpanCount(vertical: Boolean): Int {
        return when {
            compactMode -> 4
            vertical -> if (isLessSpan) 3 else 4
            else -> if (isLessSpan) 4 else 6
        }
    }
}