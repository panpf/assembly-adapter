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
package com.github.panpf.assemblyadapter.list.test.expandable.internal

import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.OnChildClickListener
import com.github.panpf.assemblyadapter.list.expandable.OnChildLongClickListener
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildClickListenerStorage
import org.junit.Assert
import org.junit.Test

class ChildClickListenerStorageTest {

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        @Suppress("unused")
        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    @Test
    fun test() {
        val clickListenerStorage = ChildClickListenerStorage<TextGroup, Text>()
        val clickListenerStorageToString: () -> String = {
            clickListenerStorage.holders.joinToString {
                when (it) {
                    is ChildClickListenerStorage.ClickListenerHolder<*, *> -> "Click:${it.viewId}"
                    is ChildClickListenerStorage.LongClickListenerHolder<*, *> -> "LongClick:${it.viewId}"
                    else -> "unknown"
                }
            }
        }
        val onClickListener = OnChildClickListener<TextGroup, Text> { _, _, _, _, _, _, _, _, _ ->
        }
        val onLongClickListener =
            OnChildLongClickListener<TextGroup, Text> { _, _, _, _, _, _, _, _, _ ->
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