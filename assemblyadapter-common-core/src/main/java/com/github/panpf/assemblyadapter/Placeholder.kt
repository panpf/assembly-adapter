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
package com.github.panpf.assemblyadapter

/**
 * If there are null elements in your data set, [Placeholder] will be used instead of null elements
 * when creating item view and binding data to item view, so you need to add a ItemFactory
 * that supports [Placeholder] when configuring the itemFactoryList property of AssemblyAdapter
 *
 * @see com.github.panpf.assemblyadapter.internal.ItemFactoryStorage.getItemFactoryByData
 */
object Placeholder