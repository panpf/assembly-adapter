# AssemblyAdapter

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AssemblyAdapter-green.svg?style=true)](https://android-arsenal.com/details/1/4152)
[![Release Version](https://img.shields.io/github/release/xiaopansky/AssemblyAdapter.svg)](https://github.com/xiaopansky/AssemblyAdapter/releases)

AssemblyAdapter是Android上的一个Adapter扩展库，有了它你就不用再写Adapter了。其支持组合式使用多Item、支持添加header和footer并且还自带加载更多功能

### 特性
>* ``Item一处定义处处使用``. 你只需为每一个item layout写一个ItemFactory，然后使用ItemFactory即可
>* ``便捷的组合式多Item``. 可以使用多个ItemFactory，每个ItemFactory就代表一种itemType
>* ``支持header和footer``. 使用AssemblyAdapter可以让ExpandableListView、GridView、RecyclerView、ViewPager等也支持header和footer
>* ``随意隐藏、显示header或footer``. header和footer还支持通过其setEnabled(boolean)方法控制隐藏或显示
>* ``自带加载更多功能``. 自带滑动到列表底部触发加载更多功能，你只需定义一个专门用于加载更多的ItemFactory即可
>* ``支持常用Adapter``. 支持BaseAdapter、RecyclerView.Adapter、BaseExpandableListAdapter、PagerAdapter、
    FragmentPagerAdapter和FragmentStatePagerAdapter，涵盖了Android开发中常用的大部分Adapter
> * ``支持SpanSize``. AssemblyRecyclerItemFactory支持SpanSize，可轻松实现横跨多列功能
>* ``无性能损耗``. 没有使用任何反射相关的技术，因此无须担心性能问题

### 使用指南

#### 1. 从JCenter导入AssemblyAdapter
```groovy
dependencies{
    compile 'me.xiaopan:assemblyadapter:lastVersionName'
}
```
`lastVersionName`：[![Release Version](https://img.shields.io/github/release/xiaopansky/AssemblyAdapter.svg)](https://github.com/xiaopansky/AssemblyAdapter/releases)`（不带v）`

`最低兼容API 7`

#### 2. 简述
共有6种Adapter：

|Adapter|父类|适用于|支持功能|
|:---|:---|:---|:---|
|AssemblyAdapter|BaseAdapter|ListView、GridView、Spinner、Gallery|多Item、header和footer、加载更多|
|AssemblyRecyclerViewAdapter|RecyclerView.Adapter|RecyclerView|多Item、header和footer、加载更多|
|AssemblyExpandableAdapter|BaseExpandableListAdapter|ExpandableListView|多Item、header和footer、加载更多|
|AssemblyPagerAdapter|PagerAdapter|ViewPager + View|多Item、header和footer|
|AssemblyFragmentPagerAdapter|FragmentPagerFragment|ViewPager + Fragment|多Item、header和footer|
|AssemblyFragmentStatePagerAdapter|FragmentStatePagerFragment|ViewPager + Fragment|多Item、header和footer|

`接下来以AssemblyAdapter为例讲解具体的用法，其它Adapter你只需照葫芦画瓢，然后ItemFactory和Item继承各自专属的类即可，详情请参考sample源码`

AssemblyAdapter分为三部分：
>* Adapter：负责维护数据、itemType以及加载更多的状态
>* ItemFactory：负责匹配数据和创建Item
>* Item：负责itemView的一切，包括创建itemView、设置数据、设置并处理事件

AssemblyAdapter与其它万能Adapter最根本的不同就是其把item相关的处理全部定义在了一个ItemFactory类里面，在使用的时候只需通过Adapter的addItemFactory(AssemblyItemFactory)方法将ItemFactory加到Adapter中即可。

这样的好处就是真正做到了一处定义处处使用，并且可以方便的在一个页面通过多次调用addItemFactory(AssemblyItemFactory)方法使用多个ItemFactory（每个ItemFactory就代表一种ItemType），这正体现了AssemblyAdapter名字中Assembly所表达的意思

另外由于支持多Item，一个Adapter又只有一个数据列表，所以数据列表的数据类型就得是Object

#### 3. 创建ItemFactory

在使用AssemblyAdapter之前得先创建ItemFactory和Item，如下：
```java
public class UserItemFactory extends AssemblyItemFactory<UserItemFactory.UserItem> {

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof User;
    }

    @Override
    public UserListItem createAssemblyItem(ViewGroup parent) {
        return new UserListItem(R.layout.list_item_user, parent);
    }

    public class UserItem extends AssemblyItem<User> {
        private ImageView headImageView;
        private TextView nameTextView;
        private TextView sexTextView;
        private TextView ageTextView;
        private TextView jobTextView;

        public UserListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            headImageView = (ImageView) findViewById(R.id.image_userListItem_head);
            nameTextView = (TextView) findViewById(R.id.text_userListItem_name);
            sexTextView = (TextView) findViewById(R.id.text_userListItem_sex);
            ageTextView = (TextView) findViewById(R.id.text_userListItem_age);
            jobTextView = (TextView) findViewById(R.id.text_userListItem_job);
        }

        @Override
        protected void onConfigViews(Context context) {
            getItemView().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getConext(), "第" + (getPosition() + 1) + "条数据", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected void onSetData(int position, User user) {
            headImageView.setImageResource(user.headResId);
            nameTextView.setText(user.name);
            sexTextView.setText(user.sex);
            ageTextView.setText(user.age);
            jobTextView.setText(user.job);
        }
    }
}
```

详解：
>* `ItemFactory的泛型`是为了限定其createAssemblyItem(ViewGroup)方法返回的类型
>* ItemFactory的`isTarget()`方法是用来匹配数据列表中的数据的，Adapter从数据列表中拿到当前位置的数据后会依次调用其所有的ItemFactory的isTarget(Object)方法，谁返回true就用谁处理当前这条数据
>* ItemFactory的`createAssemblyItem(ViewGroup)`方法用来创建Item，返回的类型必须跟你在ItemFactory上配置的泛型一样
>* `Item的泛型`是用来指定对应的数据类型，会在onSetData和getData()方法中用到
>* Item的`onFindViews(View)`和`onConfigViews(Context)`方法分别用来初始化View和配置View，只会在创建Item的时候`调用一次`，另外在onFindViews方法中你可以直接使用`findViewById(int)`法获取View
>* Item的`onSetData()`方法是用来设置数据的，在`每次getView()的时候都会调用`
>* 你可以通过Item的`getPosition()`和`getData()`方法可直接获取当前所对应的位置和数据，因此你在处理click的时候不再需要通过setTag()来绑定位置和数据了，直接获取即可
>* 你还可以通过过Item的`getItemView()`方法获取当前的itemView

#### 4. 使用ItemFactory

首先你要准备好数据并new一个AssemblyAdapter，然后通过Adapter的`addItemFactory(AssemblyItemFactory)`方法添加ItemFactory即可，如下：
```java
ListView listView = ...;

List<Object> dataList = new ArrayList<Object>;
dataList.add(new User("隔离老王"));
dataList.add(new User("隔壁老李"));

AssemblyAdapter adapter = new AssemblyAdapter(dataList);
adapter.addItemFactory(new UserItemFactory());

listView.setAdapter(adapter);
```

你还可以一次使用多个ItemFactory，如下：

```java
ListView listView = ...;

List<Object> dataList = new ArrayList<Object>;
dataList.add(new User("隔离老王"));
dataList.add(new Game("英雄联盟"));
dataList.add(new User("隔壁老李"));
dataList.add(new Game("守望先锋"));

AssemblyAdapter adapter = new AssemblyAdapter(dataList);
adapter.addItemFactory(new UserItemFactory());
adapter.addItemFactory(new GameItemFactory());

listView.setAdapter(adapter);
```

#### 5. 使用header和footer

所有Adapter均支持添加header和footer，可以方便的固定显示内容在列表的头部或尾部，更重要的意义在于可以让GridView、RecyclerView等也支持header和footer

##### 添加header、footer

首先定义好一个用于header或footer的ItemFactory

然后调用`addHeaderItem(AssemblyItemFactory, Object)`或`addFooterItem(AssemblyItemFactory, Object)`方法添加即可，如下：
```java
AssemblyAdapter adapter = new AssemblyAdapter(objects);

adapter.addHeaderItem(new HeaderItemFactory(), "我是小额头呀！");
...
adapter.addFooterItem(new HeaderItemFactory(), "我是小尾巴呀！");
```

addHeaderItem(AssemblyItemFactory, Object)和addFooterItem(AssemblyItemFactory, Object)的第二个参数是Item需要的数据，直接传进去即可

##### 隐藏或显示header、footer
addHeaderItem()或addFooterItem()都会返回一个用于控制header或footer的FixedItemInfo对象，如下：
```java
AssemblyAdapter adapter = new AssemblyAdapter(objects);

FixedItemInfo userFixedItemInfo = adapter.addHeaderItem(new HeaderItemFactory(), "我是小额头呀！");

// 隐藏
userFixedItemInfo.setEnabled(false);

// 显示
userFixedItemInfo.setEnabled(true);
```

由于有了header和footer那么Item.getPosition()方法得到的位置就是Item在Adapter中的位置，要想得到其在所属部分的真实位置可通过Adapter的`getPositionInPart(int)`获取


#### 6. 使用加载更多功能

首先你需要创建一个继承自AssemblyLoadMoreItemFactory的ItemFactory，AssemblyLoadMoreItemFactory已经将加载更多相关逻辑部分的代码写好了，你只需关心界面即可，如下：

```java
public class LoadMoreItemFactory extends AssemblyLoadMoreItemFactory {

    public LoadMoreListItemFactory(OnLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AssemblyLoadMoreItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreItem extends AssemblyLoadMoreItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            loadingView = findViewById(R.id.text_loadMoreListItem_loading);
            errorView = findViewById(R.id.text_loadMoreListItem_error);
            endView = findViewById(R.id.text_loadMoreListItem_end);
        }

        @Override
        public View getErrorRetryView() {
            return errorView;
        }

        @Override
        public void showLoading() {
            loadingView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showErrorRetry() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showEnd() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.VISIBLE);
        }
    }
}
```

然后调用Adapter的`setLoadMoreItem(AssemblyLoadMoreItemFactory)`方法设置加载更多ItemFactory即可，如下：

```java
AssemblyAdapter adapter = ...;
adapter.setLoadMoreItem(new LoadMoreItemFactory(new OnLoadMoreListener(){
    @Override
    public void onLoadMore(AssemblyAdapter adapter) {
        // 访问网络加载数据
        ...

        boolean loadSuccess = ...;
        if (loadSuccess) {
            // 加载成功时判断是否已经全部加载完毕，然后调用Adapter的loadMoreFinished(boolean)方法设置加载更多是否结束
            boolean loadMoreEnd = ...;
            adapter.loadMoreFinished(loadMoreEnd);
        } else {
            // 加载失败时调用Adapter的loadMoreFailed()方法显示加载失败提示，用户点击失败提示则会重新触发加载更多
            adapter.loadMoreFailed();
        }
    }
}));
```

你还可以通过`setDisableLoadMore(boolean)`方法替代setLoadMoreEnd(boolean)来控制是否禁用加载更多功能，两者的区别在于setLoadMoreEnd(boolean)为true时会在列表尾部显示end提示，而setDisableLoadMore(boolean)则是完全不显示加载更多尾巴

#### 7. 在RecyclerView的GridLayoutManager中一个Item独占一行或任意列

通过RecyclerView的GridLayoutManager我们可以很轻松的实现网格列表，同时GridLayoutManager还为我们提供了SpanSizeLookup接口，可以让我们指定哪一个Item需要独占一行或多列，AssemblyRecyclerAdapter 自然对如此重要的功能也做了封装，让你可以更方便的使用它

首先注册SpanSizeLookup，并通过AssemblyRecyclerAdapter的getSpanSize(int)方法获取每一个位置的Item的SpanSize：

```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    @Override
    public int getSpanSize(int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || !(adapter instanceof AssemblyRecyclerAdapter)) {
            return 1;
        }
        return ((AssemblyRecyclerAdapter) adapter).getSpanSize(position);
    }
});
recyclerView.setLayoutManager(gridLayoutManager);
```

然后创建AssemblyRecyclerAdapter、添加ItemFactory并设置SpanSize

```java
AssemblyRecyclerAdapter adapter = new AssemblyRecyclerAdapter(dataList);
adapter.addItemFactory(new AppListHeaderItemFactory().setSpanSize(4));

recyclerView.setAdapter(adapter);
```

AppListHeaderItemFactory继承自AssemblyRecyclerItemFactory因此其拥有setSpanSize(int)方法

你也可以直接使用fullSpan(RecyclerView)方法设置独占一行，fullSpan方法会通过RecyclerView取出其GridLayoutManager的SpanCount作为SpanSize

```java
AssemblyRecyclerAdapter adapter = new AssemblyRecyclerAdapter(dataList);
adapter.addItemFactory(new AppListHeaderItemFactory().fullSpan(recyclerView));

recyclerView.setAdapter(adapter);
```

fullSpan()方法如果检测到RecyclerView的LayoutManager是StaggeredGridLayoutManager的话，还会自动为Item设置setFullSpan(true)，好让Item在StaggeredGridLayoutManager中可以独占一行

### License
    Copyright (C) 2016 Peng fei Pan <sky@xiaopan.me>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
