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
package com.github.panpf.assemblyadapter.recycler.divider.test

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.GridDividerItemDecoration
import org.junit.Assert
import org.junit.Test

class GridDividerItemDecorationTest {

    @Test
    fun testBuilder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context)

        /**
         * divider
         */
        GridDividerItemDecoration.Builder(context).build().apply {
            itemDividerProvider.dividerConfig.apply {
                Assert.assertTrue(get(parent, 0, 0)!!.drawable !is ColorDrawable)
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
        }.build().apply {
            itemDividerProvider.dividerConfig.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            itemDividerProvider.dividerConfig.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            divider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            itemDividerProvider.dividerConfig.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * header divider
         */
        GridDividerItemDecoration.Builder(context).apply {
            headerDivider(Divider.space(10))
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * footer divider
         */
        GridDividerItemDecoration.Builder(context).apply {
            footerDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            footerDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            footerDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * headerAndFooterDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(Divider.space(10))
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * useDividerAsHeaderDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsHeaderDivider()
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * useDividerAsFooterDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsFooterDivider()
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * useDividerAsHeaderAndFooterDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            divider(Divider.space(10))
            useDividerAsHeaderAndFooterDivider()
        }.build().apply {
            itemDividerProvider.headerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }


        /**
         * side divider
         */
        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            itemDividerProvider.sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            itemDividerProvider.sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            itemDividerProvider.sideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * side header divider
         */
        GridDividerItemDecoration.Builder(context).apply {
            headerSideDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerSideDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerSideDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * side footer divider
         */
        GridDividerItemDecoration.Builder(context).apply {
            footerSideDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        GridDividerItemDecoration.Builder(context).apply {
            footerSideDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
        }

        GridDividerItemDecoration.Builder(context).apply {
            footerSideDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * headerAndFooterDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterSideDivider(Divider.space(10))
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterSideDivider(Divider.space(10)) {
                disableByPosition(1)
            }
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNull(get(parent, 1, 0))
            }
        }

        GridDividerItemDecoration.Builder(context).apply {
            headerAndFooterSideDivider(DividerConfig.Builder(Divider.space(10)).build())
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * useSideDividerAsHeaderSideDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsHeaderSideDivider()
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            Assert.assertNull(itemDividerProvider.footerSideDividerConfig)
        }

        /**
         * useSideDividerAsFooterSideDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsFooterSideDivider()
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideDividerConfig)
            Assert.assertNull(itemDividerProvider.headerSideDividerConfig)
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }

        /**
         * useSideDividerAsHeaderAndFooterSideDivider
         */
        GridDividerItemDecoration.Builder(context).apply {
            sideDivider(Divider.space(10))
            useSideDividerAsHeaderAndFooterSideDivider()
        }.build().apply {
            Assert.assertNull(itemDividerProvider.headerDividerConfig)
            Assert.assertNull(itemDividerProvider.footerDividerConfig)
            Assert.assertNotNull(itemDividerProvider.sideDividerConfig)
            itemDividerProvider.headerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
            itemDividerProvider.footerSideDividerConfig!!.apply {
                Assert.assertEquals(
                    Color.TRANSPARENT,
                    (get(parent, 0, 0)!!.drawable as ColorDrawable).color
                )
                Assert.assertNotNull(get(parent, 1, 0))
            }
        }
    }
}