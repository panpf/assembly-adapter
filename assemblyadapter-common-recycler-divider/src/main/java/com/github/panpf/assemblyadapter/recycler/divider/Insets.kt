package com.github.panpf.assemblyadapter.recycler.divider

import androidx.annotation.Px

class Insets(
    @Px val start: Int,
    @Px val top: Int,
    @Px val end: Int,
    @Px val bottom: Int,
) {
    companion object {
        fun of(
            @Px start: Int = 0,
            @Px top: Int = 0,
            @Px end: Int = 0,
            @Px bottom: Int = 0,
        ): Insets = Insets(start, top, end, bottom)

        fun startAndEndOf(
            @Px startAndEndInset: Int,
        ): Insets = Insets(startAndEndInset, 0, startAndEndInset, 0)

        fun topAndBottomOf(
            @Px topAndBottomInset: Int,
        ): Insets = Insets(topAndBottomInset, 0, topAndBottomInset, 0)

        fun allOf(
            @Px allInset: Int,
        ): Insets = Insets(allInset, allInset, allInset, allInset)
    }
}