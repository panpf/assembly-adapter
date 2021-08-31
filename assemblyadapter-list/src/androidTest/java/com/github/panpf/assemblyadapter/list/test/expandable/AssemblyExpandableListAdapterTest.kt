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
package com.github.panpf.assemblyadapter.list.test.expandable

import android.database.DataSetObserver
import android.widget.*
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.*

class AssemblyExpandableListAdapterTest {

    private data class Strings(val name: String = "") : ExpandableGroup {

        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any {
            return name[childPosition].toString()
        }
    }

    private class StringsItemFactory :
        ViewItemFactory<Strings>(Strings::class, android.R.layout.activity_list_item)

    private class StringItemFactory :
        ViewItemFactory<String>(String::class, { context, _, _ ->
            TextView(context)
        })

    private class DatesItemFactory :
        ViewItemFactory<Dates>(Dates::class, android.R.layout.expandable_list_content)

    private class DateItemFactory :
        ViewItemFactory<Date>(Date::class, { context, _, _ ->
            ImageView(context)
        })

    private class PlaceholderItemFactory :
        ViewItemFactory<Placeholder>(Placeholder::class, android.R.layout.activity_list_item)

    private data class Dates(val name: String, val dateList: List<Date>?) : ExpandableGroup {
        override fun getChildCount(): Int = dateList?.size ?: 0

        override fun getChild(childPosition: Int): Any = dateList!![childPosition]
    }

    @Test
    fun testConstructor() {
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            Assert.assertEquals("", currentList.joinToString { it.name })
        }

        AssemblyExpandableListAdapter<Strings, String>(
            listOf(StringsItemFactory()),
            listOf(Strings("hello"))
        ).apply {
            Assert.assertEquals("hello", currentList.joinToString { it.name })
        }

        AssemblyExpandableListAdapter<Strings, String>(
            listOf(StringsItemFactory()),
            listOf(Strings("hello"), Strings("world"))
        ).apply {
            Assert.assertEquals("hello, world", currentList.joinToString { it.name })
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyExpandableListAdapter<Strings, String>(listOf())
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        var dataFromObserver: List<Strings>? = null
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = currentList
                }
            })

            Assert.assertEquals("", currentList.joinToString { it.name })
            Assert.assertNull(dataFromObserver)

            submitList(listOf(Strings("hello")))
            Assert.assertEquals("hello", currentList.joinToString { it.name })

            submitList(listOf(Strings("hello"), Strings("world")))
            Assert.assertEquals("hello, world", currentList.joinToString { it.name })

            submitList(null)
            Assert.assertEquals("", currentList.joinToString { it.name })
        }
    }

    @Test
    fun testMethodGetGroupAndChildCount() {
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }

            submitList(listOf(Strings("hello")))
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals(5, getChildrenCount(0))

            submitList(listOf(Strings("good"), Strings("ok"), Strings("bye")))
            Assert.assertEquals(3, groupCount)
            Assert.assertEquals(4, getChildrenCount(0))
            Assert.assertEquals(2, getChildrenCount(1))
            Assert.assertEquals(3, getChildrenCount(2))

            submitList(null)
            Assert.assertEquals(0, groupCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChild() {
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(1)
            }

            submitList(listOf(Strings("hello"), Strings("world")))
            Assert.assertEquals("hello", getGroup(0).name)
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(0, -1)
            }
            Assert.assertEquals("h", getChild(0, 0))
            Assert.assertEquals("e", getChild(0, 1))
            Assert.assertEquals("l", getChild(0, 2))
            Assert.assertEquals("l", getChild(0, 3))
            Assert.assertEquals("o", getChild(0, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(0, 5)
            }
            Assert.assertEquals("world", getGroup(1).name)
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(1, -1)
            }
            Assert.assertEquals("w", getChild(1, 0))
            Assert.assertEquals("o", getChild(1, 1))
            Assert.assertEquals("r", getChild(1, 2))
            Assert.assertEquals("l", getChild(1, 3))
            Assert.assertEquals("d", getChild(1, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChild(1, 5)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildId() {
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(-1L, getGroupId(0))
            Assert.assertEquals(-1L, getGroupId(1))
            Assert.assertEquals(-1L, getChildId(-1, -1))
            Assert.assertEquals(-1L, getChildId(0, 0))
            Assert.assertEquals(-1L, getChildId(1, 1))
        }

        AssemblyExpandableListAdapter<Strings, String>(
            listOf(StringsItemFactory()),
            hasStableIds = true
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, 1)
            }
        }

        AssemblyExpandableListAdapter<Strings, String>(
            listOf(StringsItemFactory()),
            initDataList = listOf(Strings("hello"), Strings("world")),
            hasStableIds = true
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }

            Assert.assertEquals("hello".hashCode().toLong(), getGroupId(0))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(0, -1)
            }
            Assert.assertEquals(getChild(0, 0).hashCode().toLong(), getChildId(0, 0))
            Assert.assertEquals(getChild(0, 1).hashCode().toLong(), getChildId(0, 1))
            Assert.assertEquals(getChild(0, 2).hashCode().toLong(), getChildId(0, 2))
            Assert.assertEquals(getChild(0, 3).hashCode().toLong(), getChildId(0, 3))
            Assert.assertEquals(getChild(0, 4).hashCode().toLong(), getChildId(0, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(0, 5)
            }

            Assert.assertEquals("world".hashCode().toLong(), getGroupId(1))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(1, -1)
            }
            Assert.assertEquals(getChild(1, 0).hashCode().toLong(), getChildId(1, 0))
            Assert.assertEquals(getChild(1, 1).hashCode().toLong(), getChildId(1, 1))
            Assert.assertEquals(getChild(1, 2).hashCode().toLong(), getChildId(1, 2))
            Assert.assertEquals(getChild(1, 3).hashCode().toLong(), getChildId(1, 3))
            Assert.assertEquals(getChild(1, 4).hashCode().toLong(), getChildId(1, 4))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildId(1, 5)
            }

            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(2)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(2, 2)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildTypeCount() {
        AssemblyExpandableListAdapter<Strings, String>(listOf(StringsItemFactory())).apply {
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)
        }

        AssemblyExpandableListAdapter<Strings, String>(
            listOf(
                StringsItemFactory(),
                DateItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, groupTypeCount)
            Assert.assertEquals(2, childTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                StringsItemFactory(),
                StringItemFactory(),
                DatesItemFactory(),
                DateItemFactory()
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupType(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(-1, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(0, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildType(1, 1)
            }

            submitList(
                listOf(
                    Dates("dates", listOf(Date(), Date())),
                    Strings("hello")
                )
            )
            Assert.assertEquals(2, getGroupType(0))
            Assert.assertEquals(0, getGroupType(1))
            Assert.assertEquals(3, getChildType(0, 0))
            Assert.assertEquals(1, getChildType(1, 0))
        }
    }

    @Test
    fun testMethodGetGroupAndChildView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblyExpandableListAdapter<Any, Any>(
            listOf(StringsItemFactory(), StringItemFactory(), DatesItemFactory(), DateItemFactory())
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(-1, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(0, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupView(1, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(-1, -1, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, 0, false, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(1, 1, false, null, parent)
            }

            submitList(listOf(Strings("hello"), Dates("dates", listOf(Date(), Date()))))

            Assert.assertTrue(getGroupView(0, false, null, parent) is LinearLayout)
            Assert.assertTrue(getChildView(0, 0, false, null, parent) is TextView)
            Assert.assertTrue(getGroupView(1, false, null, parent) is ExpandableListView)
            Assert.assertTrue(getChildView(1, 0, false, null, parent) is ImageView)

            val groupView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(groupView, getGroupView(0, false, null, parent))
            Assert.assertSame(groupView, getGroupView(0, false, groupView, parent))

            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, -1, false, null, parent)
            }
            val childView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(childView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(childView, getChildView(0, 1, false, childView, parent))
            assertThrow(StringIndexOutOfBoundsException::class) {
                getChildView(0, 5, false, null, parent)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val stringsItemFactory = StringsItemFactory()
        val datesItemFactory = DatesItemFactory()
        val stringItemFactory = StringItemFactory()
        val dateItemFactory = DateItemFactory()
        AssemblyExpandableListAdapter<Any, Any>(
            listOf(stringsItemFactory, stringItemFactory, datesItemFactory, dateItemFactory)
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            submitList(listOf(Dates("dates", listOf(Date(), Date())), Strings("hello")))
            Assert.assertSame(datesItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(dateItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(dateItemFactory, getItemFactoryByChildPosition(0, 1))
            Assert.assertSame(stringsItemFactory, getItemFactoryByPosition(1))
            Assert.assertSame(stringItemFactory, getItemFactoryByChildPosition(1, 0))
            Assert.assertSame(stringItemFactory, getItemFactoryByChildPosition(1, 1))
        }
    }

    @Test
    fun testPlaceholder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        AssemblyExpandableListAdapter<Strings?, String>(listOf(StringsItemFactory())).apply {
            submitList(listOf(Strings("hello"), null))

            Assert.assertEquals(0, getGroupType(0))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertEquals(0, getGroupType(1))
            }

            getGroupView(0, false, null, parent)
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getGroupView(1, false, null, parent)
            }

            getItemFactoryByPosition(0)
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByPosition(1)
            }
        }

        AssemblyExpandableListAdapter<Strings?, String>(
            listOf(
                StringsItemFactory(),
                PlaceholderItemFactory()
            )
        ).apply {
            submitList(listOf(Strings("hello"), null))

            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(1, getGroupType(1))

            getGroupView(0, false, null, parent)
            getGroupView(1, false, null, parent)

            getItemFactoryByPosition(0)
            getItemFactoryByPosition(1)
        }
    }
}