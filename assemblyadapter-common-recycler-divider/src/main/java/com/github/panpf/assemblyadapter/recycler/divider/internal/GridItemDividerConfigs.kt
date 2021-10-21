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
package com.github.panpf.assemblyadapter.recycler.divider.internal

class GridItemDividerConfigs(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig?,
    val sideHeaderDividerConfig: ItemDividerConfig?,
    val sideFooterDividerConfig: ItemDividerConfig?,
) {

    val isHaveSide: Boolean =
        sideDividerConfig != null

    val isNoSide: Boolean =
        sideDividerConfig == null

    val isOnlySideHeader: Boolean =
        sideHeaderDividerConfig != null && sideFooterDividerConfig == null

    val isOnlySideFooter: Boolean =
        sideHeaderDividerConfig == null && sideFooterDividerConfig != null

    val isHaveSideHeaderAndFooter: Boolean =
        sideHeaderDividerConfig != null && sideFooterDividerConfig != null

    val isNoSideHeaderAndFooter: Boolean =
        sideHeaderDividerConfig == null && sideFooterDividerConfig == null
}