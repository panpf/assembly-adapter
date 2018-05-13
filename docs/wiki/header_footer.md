使用 header 和 footer

所有 Adapter 均支持添加 header 和 footer，可以方便的固定显示内容在列表的头部或尾部，更重要的意义在于可以让 GridView、RecyclerView 等也支持 header 和 footer

##### 添加 header、footer

首先定义好一个用于 header 或 footer 的 ItemFactory

然后调用 `addHeaderItem(AssemblyItemFactory, Object)` 或 `addFooterItem(AssemblyItemFactory, Object)` 方法添加即可，如下：
```java
AssemblyListAdapter adapter = new AssemblyListAdapter(objects);

adapter.addHeaderItem(new HeaderItemFactory(), "我是小额头呀！");
...
adapter.addFooterItem(new HeaderItemFactory(), "我是小尾巴呀！");
```

addHeaderItem(AssemblyItemFactory, Object) 和 addFooterItem(AssemblyItemFactory, Object) 的第二个参数是 Item 需要的数据，直接传进去即可

##### 隐藏或显示header、footer
addHeaderItem() 或 addFooterItem() 都会返回一个用于控制 header 或 footer 的 FixedItemInfo 对象，如下：
```java
AssemblyAdapter adapter = new AssemblyAdapter(objects);

FixedItemInfo userFixedItemInfo = adapter.addHeaderItem(new HeaderItemFactory(), "我是小额头呀！");

// 隐藏
userFixedItemInfo.setEnabled(false);

// 显示
userFixedItemInfo.setEnabled(true);
```

由于有了 header 和 footer 那么 Item.getPosition() 方法得到的位置就是 Item 在 Adapter 中的位置，要想得到其在所属部分的真实位置可通过 Adapter 的 `getPositionInPart(int)` 获取