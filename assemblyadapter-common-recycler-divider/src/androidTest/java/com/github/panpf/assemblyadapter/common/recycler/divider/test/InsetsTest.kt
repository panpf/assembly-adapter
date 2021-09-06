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
package com.github.panpf.assemblyadapter.common.recycler.divider.test

import com.github.panpf.assemblyadapter.recycler.divider.Insets
import org.junit.Assert
import org.junit.Test

class InsetsTest {

    @Test
    fun test() {
        Insets.of(1, 2, 3, 4).apply {
            Assert.assertEquals(1, start)
            Assert.assertEquals(2, top)
            Assert.assertEquals(3, end)
            Assert.assertEquals(4, bottom)
        }
        Insets.of(4, 5, 6, 7).apply {
            Assert.assertEquals(4, start)
            Assert.assertEquals(5, top)
            Assert.assertEquals(6, end)
            Assert.assertEquals(7, bottom)
        }

        Insets.startAndEndOf(2).apply {
            Assert.assertEquals(2, start)
            Assert.assertEquals(0, top)
            Assert.assertEquals(2, end)
            Assert.assertEquals(0, bottom)
        }
        Insets.startAndEndOf(4).apply {
            Assert.assertEquals(4, start)
            Assert.assertEquals(0, top)
            Assert.assertEquals(4, end)
            Assert.assertEquals(0, bottom)
        }

        Insets.topAndBottomOf(2).apply {
            Assert.assertEquals(0, start)
            Assert.assertEquals(2, top)
            Assert.assertEquals(0, end)
            Assert.assertEquals(2, bottom)
        }
        Insets.topAndBottomOf(4).apply {
            Assert.assertEquals(0, start)
            Assert.assertEquals(4, top)
            Assert.assertEquals(0, end)
            Assert.assertEquals(4, bottom)
        }

        Insets.allOf(2).apply {
            Assert.assertEquals(2, start)
            Assert.assertEquals(2, top)
            Assert.assertEquals(2, end)
            Assert.assertEquals(2, bottom)
        }
        Insets.allOf(4).apply {
            Assert.assertEquals(4, start)
            Assert.assertEquals(4, top)
            Assert.assertEquals(4, end)
            Assert.assertEquals(4, bottom)
        }
    }
}