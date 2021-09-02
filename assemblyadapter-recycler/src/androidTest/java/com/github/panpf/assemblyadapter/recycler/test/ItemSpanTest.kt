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
package com.github.panpf.assemblyadapter.recycler.test

import com.github.panpf.assemblyadapter.recycler.ItemSpan
import org.junit.Assert
import org.junit.Test

class ItemSpanTest {

    @Test
    fun testMethodFullSpan() {
        Assert.assertTrue(ItemSpan.fullSpan().isFullSpan())
        Assert.assertEquals(-1, ItemSpan.fullSpan().size)

        Assert.assertTrue(ItemSpan.span(-2).isFullSpan())
        Assert.assertEquals(-2, ItemSpan.span(-2).size)

        Assert.assertSame(ItemSpan.fullSpan(), ItemSpan.fullSpan())
    }

    @Test
    fun testMethodSpan() {
        Assert.assertEquals(-1, ItemSpan.span(-1).size)
        Assert.assertEquals(0, ItemSpan.span(0).size)
        Assert.assertEquals(3, ItemSpan.span(3).size)
    }
}