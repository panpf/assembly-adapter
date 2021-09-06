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
package com.github.panpf.assemblyadapter.common.recycler.test

import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class KeyEqualsDiffItemCallbackTest {

    @Test
    fun test() {
        val diffCallback = KeyEqualsDiffItemCallback<Any>()

        val liteHuang = People("huang", 10)
        val bigHuang = People("huang", 20)
        val zhang = People("zhang", 30)

        Assert.assertNotEquals(liteHuang, bigHuang)
        Assert.assertNotEquals(liteHuang, zhang)
        Assert.assertNotEquals(bigHuang, zhang)

        Assert.assertTrue(diffCallback.areItemsTheSame(liteHuang, liteHuang))
        Assert.assertTrue(diffCallback.areItemsTheSame(bigHuang, bigHuang))
        Assert.assertTrue(diffCallback.areItemsTheSame(liteHuang, bigHuang))
        Assert.assertTrue(diffCallback.areItemsTheSame(bigHuang, liteHuang))
        Assert.assertFalse(diffCallback.areItemsTheSame(liteHuang, zhang))
        Assert.assertFalse(diffCallback.areItemsTheSame(bigHuang, zhang))

        Assert.assertTrue(diffCallback.areContentsTheSame(liteHuang, liteHuang))
        Assert.assertTrue(diffCallback.areContentsTheSame(bigHuang, bigHuang))
        Assert.assertFalse(diffCallback.areContentsTheSame(liteHuang, bigHuang))
        Assert.assertFalse(diffCallback.areContentsTheSame(bigHuang, liteHuang))
        Assert.assertFalse(diffCallback.areContentsTheSame(liteHuang, zhang))
        Assert.assertFalse(diffCallback.areContentsTheSame(bigHuang, zhang))

        assertThrow(IllegalArgumentException::class) {
            diffCallback.areItemsTheSame(liteHuang, "no diff key")
        }
        assertThrow(IllegalArgumentException::class) {
            diffCallback.areItemsTheSame("no diff key", liteHuang)
        }
    }

    private data class People(val name: String, val age: Int) : DiffKey {
        override val diffKey: Any = "Name:$name"
    }
}