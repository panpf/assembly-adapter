package com.github.panpf.assemblyadapter.recycler

data class ItemSpan constructor(val size: Int) {

    fun isFullSpan(): Boolean = size < 0

    companion object {

        @JvmStatic
        val FULL_SPAN = ItemSpan(-1)

        @JvmStatic
        fun fullSpan(): ItemSpan {
            return FULL_SPAN
        }

        @JvmStatic
        fun span(span: Int): ItemSpan {
            return ItemSpan(span)
        }
    }
}