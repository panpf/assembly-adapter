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

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.StaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerConfig
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class StaggeredStaggeredGridDividerItemDecorationTest {

    @Test
    fun testConstructor() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * no sideDivider throw exception
         */

        StaggeredGridDividerItemDecoration(
            dividerConfig = null,
            headerDividerConfig = null,
            footerDividerConfig = null,
            sideDividerConfig = null,
            sideHeaderDividerConfig = null,
            sideFooterDividerConfig = null,
            isFullSpanByPosition = {_, _ -> false}
        )

        StaggeredGridDividerItemDecoration(
            dividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            headerDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            footerDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideHeaderDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideFooterDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            isFullSpanByPosition = {_, _ -> false}
        )

        StaggeredGridDividerItemDecoration(
            dividerConfig = null,
            headerDividerConfig = null,
            footerDividerConfig = null,
            sideDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideHeaderDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideFooterDividerConfig = null,
            isFullSpanByPosition = {_, _ -> false}
        )
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration(
                dividerConfig = null,
                headerDividerConfig = null,
                footerDividerConfig = null,
                sideDividerConfig = null,
                sideHeaderDividerConfig = ItemDividerConfig(
                    Divider.space(2).toItemDivider(context), null, null, null, null
                ),
                sideFooterDividerConfig = null,
                isFullSpanByPosition = {_, _ -> false}
            )
        }

        StaggeredGridDividerItemDecoration(
            dividerConfig = null,
            headerDividerConfig = null,
            footerDividerConfig = null,
            sideDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideHeaderDividerConfig = null,
            sideFooterDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            isFullSpanByPosition = {_, _ -> false}
        )
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration(
                dividerConfig = null,
                headerDividerConfig = null,
                footerDividerConfig = null,
                sideDividerConfig = null,
                sideHeaderDividerConfig = null,
                sideFooterDividerConfig = ItemDividerConfig(
                    Divider.space(2).toItemDivider(context), null, null, null, null
                ),
                isFullSpanByPosition = {_, _ -> false}
            )
        }

        StaggeredGridDividerItemDecoration(
            dividerConfig = null,
            headerDividerConfig = null,
            footerDividerConfig = null,
            sideDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideHeaderDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            sideFooterDividerConfig = ItemDividerConfig(
                Divider.space(2).toItemDivider(context), null, null, null, null
            ),
            isFullSpanByPosition = {_, _ -> false}
        )
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration(
                dividerConfig = null,
                headerDividerConfig = null,
                footerDividerConfig = null,
                sideDividerConfig = null,
                sideHeaderDividerConfig = ItemDividerConfig(
                    Divider.space(2).toItemDivider(context), null, null, null, null
                ),
                sideFooterDividerConfig = ItemDividerConfig(
                    Divider.space(2).toItemDivider(context), null, null, null, null
                ),
                isFullSpanByPosition = {_, _ -> false}
            )
        }
    }

    @Test
    fun testIsFullSpanByPosition() {
        val context = InstrumentationRegistry.getInstrumentation().context

        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                headerDivider(Divider.space(20))
            }.build()
        }

        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                footerDivider(Divider.space(20))
            }.build()
        }

        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                headerAndFooterDivider(Divider.space(20))
            }.build()
        }

        assertNoThrow {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                headerDivider(Divider.space(20))
                isFullSpanByPosition { _, _ -> false }
            }.build()
        }

        assertNoThrow {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                footerDivider(Divider.space(20))
                isFullSpanByPosition { _, _ -> false }
            }.build()
        }

        assertNoThrow {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                headerAndFooterDivider(Divider.space(20))
                isFullSpanByPosition { _, _ -> false }
            }.build()
        }
    }

    @Test
    fun testBuilder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context)

        /**
         * divider
         */
        StaggeredGridDividerItemDecoration.Builder(context).build().apply {
            dividerConfig!!.apply {
                Assert.assertTrue(get(parent, 0, 0)!!.drawable !is ColorDrawable)
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply{
            disableDefaultDivider()
        }.build().apply {
            Assert.assertNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
        }.build().apply {
            dividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            dividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            dividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * header divider
         */
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerDivider(Divider.space(10))
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerDivider(Divider.space(10)) {
                disableByPosition(1)
            }
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerDivider(DividerConfig.Builder(Divider.space(10)).build())
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * footer divider
         */
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            footerDivider(Divider.space(10))
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            footerDivider(Divider.space(10)) {
                disableByPosition(1)
            }
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            footerDivider(DividerConfig.Builder(Divider.space(10)).build())
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * headerAndFooterDivider
         */
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(Divider.space(10))
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(DividerConfig.Builder(Divider.space(10)).build())
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * useDividerAsHeaderDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                disableDefaultDivider()
                useDividerAsHeaderDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsHeaderDivider()
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * useDividerAsFooterDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                disableDefaultDivider()
                useDividerAsFooterDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsFooterDivider()
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * useDividerAsHeaderAndFooterDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                disableDefaultDivider()
                useDividerAsHeaderAndFooterDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsHeaderAndFooterDivider()
            isFullSpanByPosition { _, _ -> true }
        }.build().apply {
            headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }


        /**
         * side divider
         */
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * side header divider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideHeaderDivider(Divider.space(10))
            }.build()
        }

            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideDivider(Divider.space(10))
                sideHeaderDivider(Divider.space(10))
            }.build().apply {
                Assert.assertNull(headerDividerConfig)
                Assert.assertNull(footerDividerConfig)
                Assert.assertNotNull(sideDividerConfig)
                sideHeaderDividerConfig!!.apply {
                    Assert.assertEquals(
                        Color.TRANSPARENT,
                        (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                    )
                    Assert.assertNotNull(get(parent, 1, 0))
                }
                Assert.assertNull(sideFooterDividerConfig)
            }

            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideDivider(Divider.space(10))
                sideHeaderDivider(Divider.space(10)) {
                    disableByPosition(1)
                }
            }.build().apply {
                Assert.assertNull(headerDividerConfig)
                Assert.assertNull(footerDividerConfig)
                Assert.assertNotNull(sideDividerConfig)
                sideHeaderDividerConfig!!.apply {
                    Assert.assertEquals(
                        Color.TRANSPARENT,
                        (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                    )
                    Assert.assertNull(get(parent, 1, 0))
                }
                Assert.assertNull(sideFooterDividerConfig)
            }

            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideDivider(Divider.space(10))
                sideHeaderDivider(DividerConfig.Builder(Divider.space(10)).build())
            }.build().apply {
                Assert.assertNull(footerDividerConfig)
                Assert.assertNull(headerDividerConfig)
                Assert.assertNotNull(sideDividerConfig)
                sideHeaderDividerConfig!!.apply {
                    Assert.assertEquals(
                        Color.TRANSPARENT,
                        (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                    )
                    Assert.assertNotNull(get(parent, 1, 0))
                }
                Assert.assertNull(sideFooterDividerConfig)
            }

        /**
         * side footer divider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideFooterDivider(Divider.space(10))
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideFooterDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideFooterDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * sideHeaderAndFooterDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                sideHeaderAndFooterDivider(Divider.space(10))
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideHeaderAndFooterDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideHeaderAndFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            sideHeaderAndFooterDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * useSideDividerAsSideHeaderDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                useSideDividerAsSideHeaderDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsSideHeaderDivider()
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideFooterDividerConfig)
        }

        /**
         * useSideDividerAsSideFooterDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                useSideDividerAsSideFooterDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsSideFooterDivider()
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * useSideDividerAsSideHeaderAndFooterDivider
         */
        assertThrow(IllegalArgumentException::class) {
            StaggeredGridDividerItemDecoration.Builder(context).apply {
                useSideDividerAsSideHeaderAndFooterDivider()
            }.build()
        }

        StaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsSideHeaderAndFooterDivider()
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNotNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }
    }
}