package com.github.panpf.assemblyadapter.recycler

data class ItemSpan constructor(val span: Int) {

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