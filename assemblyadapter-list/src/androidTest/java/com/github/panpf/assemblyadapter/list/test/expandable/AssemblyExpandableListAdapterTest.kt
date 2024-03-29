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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.ViewExpandableGroupItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.Date

class AssemblyExpandableListAdapterTest {

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private class TextGroupItemFactory :
        ViewExpandableGroupItemFactory<TextGroup>(TextGroup::class, { context, _, _ ->
            FrameLayout(context)
        })

    private data class Image(val resId: Int)

    private data class ImageGroup(val list: List<Image>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.resId.toString() }

        constructor(vararg resIds: Int) : this(resIds.map { Image(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private class ImageGroupItemFactory :
        ViewExpandableGroupItemFactory<ImageGroup>(ImageGroup::class, { context, _, _ ->
            LinearLayout(context)
        })


    private class PlaceholderItemFactory :
        ViewItemFactory<Placeholder>(Placeholder::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            Assert.assertEquals("", currentList.joinToString { it.listJoinToString })
        }

        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory()),
            listOf(TextGroup("hello"))
        ).apply {
            Assert.assertEquals("[hello]", currentList.joinToString { it.listJoinToString })
        }

        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory()),
            listOf(TextGroup("hello"), TextGroup("world"))
        ).apply {
            Assert.assertEquals(
                "[hello], [world]",
                currentList.joinToString { it.listJoinToString })
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyExpandableListAdapter<TextGroup, Text>(listOf())
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        var dataFromObserver: List<TextGroup>? = null
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = currentList
                }
            })

            Assert.assertEquals("", currentList.joinToString { it.listJoinToString })
            Assert.assertEquals(
                "",
                (dataFromObserver ?: emptyList()).joinToString { it.listJoinToString })

            submitList(listOf(TextGroup("hello")))
            Assert.assertEquals(
                "[hello]",
                currentList.joinToString { it.listJoinToString }
            )
            Assert.assertEquals(
                "[hello]",
                (dataFromObserver ?: emptyList()).joinToString { it.listJoinToString }
            )

            submitList(listOf(TextGroup("hello"), TextGroup("world")))
            Assert.assertEquals(
                "[hello], [world]",
                currentList.joinToString { it.listJoinToString }
            )
            Assert.assertEquals(
                "[hello], [world]",
                (dataFromObserver ?: emptyList()).joinToString { it.listJoinToString }
            )

            submitList(null)
            Assert.assertEquals("", currentList.joinToString { it.listJoinToString })
            Assert.assertEquals(
                "",
                (dataFromObserver ?: emptyList()).joinToString { it.listJoinToString }
            )
        }
    }

    @Test
    fun testMethodGetGroupAndChildCount() {
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            Assert.assertEquals(0, groupCount)
            Assert.assertEquals(0, itemCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }

            submitList(listOf(TextGroup("h", "e", "l", "l", "o")))
            Assert.assertEquals(1, groupCount)
            Assert.assertEquals(1, itemCount)
            Assert.assertEquals(5, getChildrenCount(0))

            submitList(
                listOf(
                    TextGroup("g", "o", "o", "d"),
                    TextGroup("o", "k"),
                    TextGroup("b", "y", "e")
                )
            )
            Assert.assertEquals(3, groupCount)
            Assert.assertEquals(3, itemCount)
            Assert.assertEquals(4, getChildrenCount(0))
            Assert.assertEquals(2, getChildrenCount(1))
            Assert.assertEquals(3, getChildrenCount(2))

            submitList(null)
            Assert.assertEquals(0, groupCount)
            Assert.assertEquals(0, itemCount)
            assertThrow(IndexOutOfBoundsException::class) {
                getChildrenCount(0)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChild() {
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getGroup(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            submitList(listOf(TextGroup("hello", "good"), TextGroup("world", "bye")))
            Assert.assertEquals("[hello, good]", getGroup(0).listJoinToString)
            Assert.assertEquals("[hello, good]", getItemData(0).listJoinToString)
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(0, -1)
            }
            Assert.assertEquals(Text("hello"), getChild(0, 0))
            Assert.assertEquals(Text("good"), getChild(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(0, 2)
            }
            Assert.assertEquals("[world, bye]", getGroup(1).listJoinToString)
            Assert.assertEquals("[world, bye]", getItemData(1).listJoinToString)
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(1, -1)
            }
            Assert.assertEquals(Text("world"), getChild(1, 0))
            Assert.assertEquals(Text("bye"), getChild(1, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChild(1, 2)
            }
        }
    }

    @Test
    fun testMethodGetGroupAndChildId() {
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            Assert.assertEquals(-1L, getGroupId(-1))
            Assert.assertEquals(-1L, getGroupId(0))
            Assert.assertEquals(-1L, getGroupId(1))
            Assert.assertEquals(-1L, getChildId(-1, -1))
            Assert.assertEquals(-1L, getChildId(0, 0))
            Assert.assertEquals(-1L, getChildId(1, 1))
        }

        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory()),
        ).apply {
            setHasStableIds(true)
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

        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory()),
            initDataList = listOf(TextGroup("hello", "good"), TextGroup("world", "bye")),
        ).apply {
            setHasStableIds(true)
            assertThrow(IndexOutOfBoundsException::class) {
                getGroupId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(-1, -1)
            }

            Assert.assertEquals(getGroup(0).hashCode().toLong(), getGroupId(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, -1)
            }
            Assert.assertEquals(getChild(0, 0).hashCode().toLong(), getChildId(0, 0))
            Assert.assertEquals(getChild(0, 1).hashCode().toLong(), getChildId(0, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(0, 2)
            }

            Assert.assertEquals(getGroup(1).hashCode().toLong(), getGroupId(1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, -1)
            }
            Assert.assertEquals(getChild(1, 0).hashCode().toLong(), getChildId(1, 0))
            Assert.assertEquals(getChild(1, 1).hashCode().toLong(), getChildId(1, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildId(1, 2)
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
        AssemblyExpandableListAdapter<TextGroup, Text>(listOf(TextGroupItemFactory())).apply {
            Assert.assertEquals(1, groupTypeCount)
            Assert.assertEquals(1, childTypeCount)
        }

        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(
                TextGroupItemFactory(),
                ImageItemFactory()
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
                TextGroupItemFactory(),
                TextItemFactory(),
                ImageGroupItemFactory(),
                ImageItemFactory()
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
                    ImageGroup(android.R.drawable.btn_default, android.R.drawable.btn_dialog),
                    TextGroup("hello")
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
            listOf(
                TextGroupItemFactory(),
                TextItemFactory(),
                ImageGroupItemFactory(),
                ImageItemFactory()
            )
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

            submitList(
                listOf(
                    TextGroup("hello", "world"),
                    ImageGroup(android.R.drawable.btn_default, android.R.drawable.btn_dialog)
                )
            )

            Assert.assertTrue(getGroupView(0, false, null, parent) is FrameLayout)
            Assert.assertTrue(getChildView(0, 0, false, null, parent) is TextView)
            Assert.assertTrue(getGroupView(1, false, null, parent) is LinearLayout)
            Assert.assertTrue(getChildView(1, 0, false, null, parent) is ImageView)

            val groupView = getGroupView(0, false, null, parent)
            Assert.assertNotSame(groupView, getGroupView(0, false, null, parent))
            Assert.assertSame(groupView, getGroupView(0, false, groupView, parent))

            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, -1, false, null, parent)
            }
            val childView = getChildView(0, 0, false, null, parent)
            Assert.assertNotSame(childView, getChildView(0, 0, false, null, parent))
            Assert.assertSame(childView, getChildView(0, 0, false, childView, parent))
            assertThrow(IndexOutOfBoundsException::class) {
                getChildView(0, 2, false, null, parent)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val textGroupItemFactory = TextGroupItemFactory()
        val imageGroupItemFactory = ImageGroupItemFactory()
        val textItemFactory = TextItemFactory()
        val imageItemFactory = ImageItemFactory()

        AssemblyExpandableListAdapter<Any, Any>(
            listOf(textGroupItemFactory, textItemFactory, imageGroupItemFactory, imageItemFactory)
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

            submitList(
                listOf(
                    ImageGroup(android.R.drawable.btn_default, android.R.drawable.btn_dialog),
                    TextGroup("hello", "world")
                )
            )
            Assert.assertSame(imageGroupItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 1))
            Assert.assertSame(textGroupItemFactory, getItemFactoryByPosition(1))
            Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 0))
            Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 1))
        }

        AssemblyExpandableListAdapter<Any, Any>(
            listOf(textGroupItemFactory, textItemFactory)
        ).apply {
            submitList(
                listOf(
                    ImageGroup(android.R.drawable.btn_default, android.R.drawable.btn_dialog),
                    TextGroup("hello", "world")
                )
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(imageGroupItemFactory, getItemFactoryByPosition(0))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 0))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 1))
            }
            Assert.assertSame(textGroupItemFactory, getItemFactoryByPosition(1))
            Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 0))
            Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 1))
        }

        AssemblyExpandableListAdapter<Any, Any>(
            listOf(imageGroupItemFactory, imageItemFactory)
        ).apply {
            submitList(
                listOf(
                    ImageGroup(android.R.drawable.btn_default, android.R.drawable.btn_dialog),
                    TextGroup("hello", "world")
                )
            )
            Assert.assertSame(imageGroupItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 0))
            Assert.assertSame(imageItemFactory, getItemFactoryByChildPosition(0, 1))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(textGroupItemFactory, getItemFactoryByPosition(1))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 0))
            }
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertSame(textItemFactory, getItemFactoryByChildPosition(1, 1))
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val textItemFactory = TextItemFactory()
        val imageItemFactory = ImageItemFactory()
        val placeholderItemFactory = PlaceholderItemFactory()

        AssemblyExpandableListAdapter<Any, Any>(listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByData(Image(android.R.drawable.alert_dark_frame))
            )
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(Date())
            }

            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByChildData(Image(android.R.drawable.alert_dark_frame))
            )
            Assert.assertSame(textItemFactory, getItemFactoryByChildData(Text("hello")))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByChildData(Date())
            }
        }

        AssemblyExpandableListAdapter<Any?, Any>(listOf(textItemFactory, imageItemFactory)).apply {
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(null)
            }
        }
        AssemblyExpandableListAdapter<Any?, Any>(
            listOf(textItemFactory, imageItemFactory, placeholderItemFactory)
        ).apply {
            Assert.assertSame(placeholderItemFactory, getItemFactoryByData(null))
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val textItemFactory = TextItemFactory()
        val imageItemFactory = ImageItemFactory()
        val placeholderItemFactory = PlaceholderItemFactory()

        AssemblyExpandableListAdapter<Any, Any>(listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByClass(ImageItemFactory::class.java)
            )
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewItemFactory::class.java)
            }
        }
        AssemblyExpandableListAdapter<Any?, Any>(
            listOf(textItemFactory, imageItemFactory, placeholderItemFactory)
        ).apply {
            Assert.assertSame(
                placeholderItemFactory,
                getItemFactoryByClass(PlaceholderItemFactory::class.java)
            )
        }
    }

    @Test
    fun testPlaceholder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        AssemblyExpandableListAdapter<TextGroup?, Text>(listOf(TextGroupItemFactory())).apply {
            submitList(listOf(TextGroup("hello"), null))

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

        AssemblyExpandableListAdapter<TextGroup?, Text>(
            listOf(
                TextGroupItemFactory(),
                PlaceholderItemFactory()
            )
        ).apply {
            submitList(listOf(TextGroup("hello"), null))

            Assert.assertEquals(0, getGroupType(0))
            Assert.assertEquals(1, getGroupType(1))

            getGroupView(0, false, null, parent)
            getGroupView(1, false, null, parent)

            getItemFactoryByPosition(0)
            getItemFactoryByPosition(1)
        }
    }

    @Test
    fun testMethodHasObservers() {
        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory(), TextItemFactory())
        ).apply {
            Assert.assertFalse(hasObservers())

            val dataObserver1 = object : DataSetObserver() {}
            val dataObserver2 = object : DataSetObserver() {}

            registerDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            registerDataSetObserver(dataObserver2)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver2)
            Assert.assertFalse(hasObservers())
        }
    }

    @Test
    fun testMethodIsChildSelectable() {
        AssemblyExpandableListAdapter<TextGroup, Text>(
            listOf(TextGroupItemFactory(), TextItemFactory()),
            listOf(
                TextGroup("hello"),
                TextGroup("hello", "world", "good"),
                TextGroup("hello", "bye")
            )
        ).apply {
            Assert.assertNull(childSelectable)

            childSelectable = { adapter, groupPosition, childPosition ->
                adapter.getChild(groupPosition, childPosition)
                groupPosition == 1 && childPosition == 2
            }
            Assert.assertNotNull(childSelectable)

            assertThrow(IndexOutOfBoundsException::class) {
                isChildSelectable(-1, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                isChildSelectable(0, -1)
            }
            Assert.assertFalse(isChildSelectable(0, 0))
            assertThrow(IndexOutOfBoundsException::class) {
                isChildSelectable(0, 1)
            }
            Assert.assertFalse(isChildSelectable(1, 0))
            Assert.assertFalse(isChildSelectable(1, 1))
            Assert.assertTrue(isChildSelectable(1, 2))
            Assert.assertFalse(isChildSelectable(2, 0))
            Assert.assertFalse(isChildSelectable(2, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                isChildSelectable(3, 0)
            }
        }
    }
}