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
package com.github.panpf.assemblyadapter.list.test.internal

import android.database.DataSetObserver
import com.github.panpf.assemblyadapter.list.internal.AdapterDataObservable
import org.junit.Assert
import org.junit.Test

class AdapterDataObservableTest {

    @Test
    fun test() {
        AdapterDataObservable().apply {
            Assert.assertFalse(hasObservers())

            val dataObserver1 = object : DataSetObserver() {}
            val dataObserver2 = object : DataSetObserver() {}

            registerObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            registerObserver(dataObserver2)
            Assert.assertTrue(hasObservers())

            unregisterObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            unregisterObserver(dataObserver2)
            Assert.assertFalse(hasObservers())
        }
    }
}