package com.github.panpf.assemblyadapter.recycler

data class ItemSpan constructor(val size: Int) {

    companion object {
        @JvmStatic
        fun fullSpan(): ItemSpan {
            return ItemSpan(-1)
        }

        @JvmStatic
        fun span(span: Int): ItemSpan {
            return ItemSpan(span)
        }
    }
}