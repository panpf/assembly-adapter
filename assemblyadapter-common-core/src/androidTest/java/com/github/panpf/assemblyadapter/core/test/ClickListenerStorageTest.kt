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
package com.github.panpf.assemblyadapter.core.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.internal.ClickListenerStorage
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClickListenerStorageTest {

    @Test
    fun test() {
        val clickListenerStorage = ClickListenerStorage<String>()
        val clickListenerStorageToString: () -> String = {
            clickListenerStorage.holders.joinToString {
                when (it) {
                    is ClickListenerStorage.ClickListenerHolder<*> -> "Click:${it.viewId}"
                    is ClickListenerStorage.LongClickListenerHolder<*> -> "LongClick:${it.viewId}"
                    else -> "unknown"
                }
            }
        }
        val onClickListener = OnClickListener<String> { _, _, _, _, _ ->
        }
        val onLongClickListener = OnLongClickListener<String> { _, _, _, _, _ ->
            false
        }

        Assert.assertEquals("", clickListenerStorageToString())

        clickListenerStorage.add(onClickListener)
        Assert.assertEquals(
            "Click:-1",
            clickListenerStorageToString()
        )

        clickListenerStorage.add(2, onClickListener)
        Assert.assertEquals(
            "Click:-1, Click:2",
            clickListenerStorageToString()
        )

        clickListenerStorage.add(onLongClickListener)
        Assert.assertEquals(
            "Click:-1, Click:2, LongClick:-1",
            clickListenerStorageToString()
        )

        clickListenerStorage.add(4, onLongClickListener)
        Assert.assertEquals(
            "Click:-1, Click:2, LongClick:-1, LongClick:4",
            clickListenerStorageToString()
        )
    }
}