/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.recycler.divider

import androidx.annotation.Px

/**
 * Define the spacing around the divider
 */
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
        ): Insets = Insets(0, topAndBottomInset, 0, topAndBottomInset)

        fun allOf(
            @Px allInset: Int,
        ): Insets = Insets(allInset, allInset, allInset, allInset)
    }
}