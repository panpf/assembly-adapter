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
 * When the hasStableIds function of AssemblyListAdapter, AssemblySingleDataListAdapter, AssemblyExpandableListAdapter, AssemblySingleDataExpandableListAdapter is turned on, you can customize the return value of the getItemId() or getGroupId() or getChildId() method by implementing this interface for data
 */
interface ItemId {
    /**
     * Use the getItemId() method of AssemblyListAdapter, AssemblySingleDataListAdapter or the getGroupId() and getChildId() methods of AssemblyExpandableListAdapter, AssemblySingleDataExpandableListAdapter
     */
    val itemId: Long
}