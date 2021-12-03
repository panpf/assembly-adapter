package com.github.panpf.assemblyadapter.recycler.divider.internal

interface DividerSize {
    fun getWidth(isStartOrEnd: Boolean): Int
    fun getHeight(isStartOrEnd: Boolean): Int
}

data class ClearlyDividerSize(private val width: Int, private val height: Int) : DividerSize {

    override fun getWidth(isStartOrEnd: Boolean): Int = width

    override fun getHeight(isStartOrEnd: Boolean): Int = height
}

data class VagueDividerSize(private val size: Int) : DividerSize {
    override fun getWidth(isStartOrEnd: Boolean): Int = if (isStartOrEnd) size else -1

    override fun getHeight(isStartOrEnd: Boolean): Int = if (isStartOrEnd) -1 else size
}