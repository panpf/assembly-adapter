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
package com.github.panpf.assemblyadapter.recycler.test.divider

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.AssemblyDividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.AssemblyStaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import org.junit.Assert
import org.junit.Test

class AssemblyStaggeredGridDividerItemDecorationTest {

    @Test
    fun testIsFullSpanByPosition() {
        val context = InstrumentationRegistry.getInstrumentation().context

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            headerDivider(Divider.space(20))
        }.build()

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            footerDivider(Divider.space(20))
        }.build()

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(Divider.space(20))
        }.build()

        assertNoThrow {
            AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
                headerDivider(Divider.space(20))
                isFullSpanByPosition { _, _ -> false }
            }.build()
        }

        assertNoThrow {
            AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
                footerDivider(Divider.space(20))
                isFullSpanByPosition { _, _ -> false }
            }.build()
        }

        assertNoThrow {
            AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).build().apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            disableDefaultDivider()
        }.build().apply {
            Assert.assertNull(dividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            Assert.assertNull(sideFooterDividerConfig)
        }

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            divider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            headerDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            footerDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideFooterDividerConfig)
        }

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            sideHeaderDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(sideFooterDividerConfig)
        }

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(sideDividerConfig)
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideFooterDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
            Assert.assertNull(sideHeaderDividerConfig)
            sideFooterDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
        }

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideFooterDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderAndFooterDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderAndFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
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

        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            sideHeaderAndFooterDivider(AssemblyDividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(headerDividerConfig)
            Assert.assertNull(footerDividerConfig)
            Assert.assertNull(sideDividerConfig)
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
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