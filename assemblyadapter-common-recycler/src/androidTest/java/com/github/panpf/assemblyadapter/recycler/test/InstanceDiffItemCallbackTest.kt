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

import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import org.junit.Assert
import org.junit.Test

class InstanceDiffItemCallbackTest {

    @Test
    fun test() {
        val diffCallback = InstanceDiffItemCallback<Any>()

        val data1 = String(charArrayOf('a', 'b'))
        val data2 = String(charArrayOf('a', 'b'))

        Assert.assertEquals(data1, data2)

        Assert.assertTrue(diffCallback.areItemsTheSame(data1, data1))
        Assert.assertTrue(diffCallback.areItemsTheSame(data2, data2))
        Assert.assertFalse(diffCallback.areItemsTheSame(data1, data2))
        Assert.assertFalse(diffCallback.areItemsTheSame(data2, data1))

        Assert.assertTrue(diffCallback.areContentsTheSame(data1, data1))
        Assert.assertTrue(diffCallback.areContentsTheSame(data2, data2))
        Assert.assertFalse(diffCallback.areContentsTheSame(data1, data2))
        Assert.assertFalse(diffCallback.areContentsTheSame(data2, data1))
    }
}