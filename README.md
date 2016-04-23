#AssemblyAdapter

这是Android上的一个通用Adapter库，有了它你就不需要再写新的Adapter了，并且还自带加载更多功能。支持BaseAdapter、BaseExpandableListAdapter以及RecyclerView.Adapter

This is a generic Android library on the Adapter, with it you do not need to write a new Adapter, and also comes loaded with more features. Supports BaseAdapter, BaseExpandableListAdapter, and RecyclerView.Adapter

##Features
>* 你再也不需要写新的Adapter了，只需为每一个item layout写一个AssemblyItemFactory即可，并且遵循AssemblyItemFactory结构的代码逻辑会前所未有的清晰
>* 可以一次使用多个AssemblyItemFactory，每个AssemblyItemFactory就代表一种itemType
>* 自带加载更多功能，你只需实现一个专门用于加载更多的尾巴即可
>* 支持BaseAdapter、BaseExpandableListAdapter以及RecyclerView.Adapter，已经涵盖了Android开发中用到的所有Adapter
>* 没有使用任何反射相关的代码无须担心性能问题
>* AssemblyItemFactory的扩展性很好，可以满足你的任何需求

English（From Baidu translate）
>* You don't need to write a new Adapter again, just for each layout AssemblyItemFactory write a item can, and follow the AssemblyItemFactory structure of the code logic will never clear
>* You can use more than one AssemblyItemFactory, each AssemblyItemFactory on behalf of a kind of itemType
>* Comes with more features, you just need to implement a dedicated to load more of the tail
>Supports BaseAdapter, BaseExpandableListAdapter, and RecyclerView.Adapter, has covered all the Android development of Adapter
>* There is no need to worry about performance issues without using any reflection related code
>* AssemblyItemFactory is very good and can meet any of your needs.

##Usage Guide
####1. 导入AssemblyAdapter（Import AssemblyAdapter to your project）

#####使用Gradle（Use Gradle）
``从JCenter仓库导入（Import from jcenter）``

```groovy
dependencies{
	compile 'me.xiaopan:assemblyadapter:1.1.0'
}
```

``离线模式（Offline work）``

首先到[releases](https://github.com/xiaopansky/AssemblyAdapter/releases)页面下载最新版的aar包（`这里以assemblyadapter-1.1.0.aar为例，具体请以你下载到的文件名称为准`），并放到你module的libs目录下

然后在你module的build.gradle文件中添加以下代码：
```groovy
repositories{
    flatDir(){
        dirs 'libs'
    }
}

dependencies{
    compile(name:'assemblyadapter-1.1.0', ext:'aar')
}
```
最后同步一下Gradle即可

#####使用Eclipse（Use Eclipse）
1. 首先到[releases](https://github.com/xiaopansky/AssemblyAdapter/releases)页面下载最新版的aar包（`这里以assemblyadapter-1.1.0.aar为例，具体请以你下载到的文件名称为准`）
2. 然后改后缀名为zip并解压
2. 接下来将classes.jar文件重命名为assemblyadapter-1.1.0.jar
3. 最后将assemblyadapter-1.1.0.jar拷贝到你的项目的libs目录下

####2. 配置最低版本（Configure min sdk version）
AssemblyAdapter最低兼容API v7

#####使用Gradle（Use Gradle）
在app/build.gradle文件文件中配置最低版本为7
```groovy
android {
	...

    defaultConfig {
        minSdkVersion 7
        ...
    }
}
```

#####使用Eclipse（Use Eclipse）
在AndroidManifest.xml文件中配置最低版本为7
```xml
<manifest
	...
	>
    <uses-sdk android:minSdkVersion="7"/>
    <application>
    ...
    </application>
</manifest>
```

###3. 使用AssemblyAdapter
``这里只讲解AssemblyAdapter的用法，AssemblyExpandableAdapter和AssemblyRecyclerAdapter的用法跟AssemblyAdapter完全一致，只是Factory和Item所继承的类不一样而已，详情请参考sample源码``

AssemblyAdapter分为三部分：
>* AssemblyAdapter：封装了Adapter部分需要的所有处理
>* AssemblyItemFactory：负责匹配Bean、创建AssemblyItem以及管理扩展参数
>* AssemblyItem：负责创建convertView、设置并处理事件、设置数据

AssemblyAdapter最大的特点如其名字所表达的意思是可以组装的，你可以调用其addItemFactory(AssemblyItemFactory)方法添加多个AssemblyItemFactory，而每一个AssemblyItemFactory就代表一种ItemType，这样就轻松实现了ItemType。因而AssemblyAdapter的数据列表的类型是Object的。

首先创建你的AssemblyItemFactory和AssemblyItem，如下所示：
```java
public class UserListItemFactory extends AssemblyItemFactory<UserListItemFactory.UserListItem> {

    private EventListener eventListener;

    public UserListItemFactory(Context context) {
        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof User;
    }

    @Override
    public UserListItem createAssemblyItem(ViewGroup parent) {
        return new UserListItem(R.layout.list_item_user, parent);
    }

    public class UserListItem extends AssemblyItem<User> {
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
            headImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickHead(getPosition(), getData());
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickName(getPosition(), getData());
                }
            });
            sexTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickSex(getPosition(), getData());
                }
            });
            ageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickAge(getPosition(), getData());
                }
            });
            jobTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClickJob(getPosition(), getData());
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

    public interface EventListener{
        void onClickHead(int position, User user);
        void onClickName(int position, User user);
        void onClickSex(int position, User user);
        void onClickAge(int position, User user);
        void onClickJob(int position, User user);
    }

    private static class EventProcessor implements EventListener {
        private Context context;

        public EventProcessor(Context context) {
            this.context = context;
        }

        @Override
        public void onClickHead(int position, User user) {
            Toast.makeText(context, "别摸我头，讨厌啦！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickName(int position, User user) {
            Toast.makeText(context, "我就叫"+user.name+"，咋地不服啊！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickSex(int position, User user) {
            Toast.makeText(context, "我还就是"+user.sex+"个的了，有本事你捅我啊！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickAge(int position, User user) {
            String message;
            if(user.sex.contains("男") || user.sex.contains("先生")){
                message = "哥今年"+user.age+"岁了，该找媳妇了！";
            }else{
                message = "姐今年"+user.age+"岁了，该找人嫁了！";
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickJob(int position, User user) {
            Toast.makeText(context, "我是名光荣的"+user.job, Toast.LENGTH_SHORT).show();
        }
    }
}
```
在创建AssemblyItemFactory和AssemblyItem的时候需要注意以下几点：
>* AssemblyItemFactory和AssemblyItem的泛型是互相指定的，一定要好好配置，不可省略
>* AssemblyItemFactory.isTarget()方法是用来匹配数据源中的数据的，只有返回true才会使用当前Factory
>* AssemblyItemFactory.createAssemblyItem(ViewGroup)方法用来创建AssembleItem，返回的类型必须跟你在AssemblyItemFactory上配置的泛型一样
>* AssemblyItem的onFindViews(View)和onConfigViews(Context)方法分别用来初始化View和配置View，只会在创建AssemblyItem的时候调用一次
>* AssembleItem.onSetData()方法是用来设置数据的，在每次getView()的时候都会调用
>* 你可以通过getPosition()和getData()方法直接获取当前Item所对应的位置和数据对象，因此你在处理onClick的时候不需要再通过setTag来传递数据了，直接调方法获取即可。getData()返回的对象类型和你在AssemblyItem第一个泛型配置的一样，因此你也不需要再转换类型了
>* 你可以像示例那样将所有需要处理的事件通过接口剥离出去，然后写一个专门的处理类，这样逻辑比较清晰

然后new一个AssemblyAdapter直接通过其addItemFactory方法使用你自定义的UserListItemFactory即可，如下：
```java
ListView listView = ...;
List<Object> dataList = ...;
dataList.add(new User(...));

AssemblyAdapter adapter = new AssemblyAdapter(dataList);
adapter.addItemFactory(new UserListItemFactory(getBaseContext()));
listView.setAdapter(adapter);
```

**一次使用多个AssemblyItemFactory，就像这样**

```java
ListView listView = ...;
List<Object> dataList = ...;
dataList.add(new User(...));
dataList.add(new Game(...));

AssemblyAdapter adapter = new AssemblyAdapter(dataList);
adapter.addItemFactory(new UserListItemFactory(getBaseContext()));
adapter.addItemFactory(new GameListItemFactory(getBaseContext()));
listView.setAdapter(adapter);
```

**使用加载更多功能**

首先你需要创建一个继承自AbstractLoadMoreListItemFactory的ItemFactory，AbstractLoadMoreListItemFactory已经将逻辑部分的代码写好了，你只需关心界面即可，如下：
```java
public class LoadMoreListItemFactory extends AbstractLoadMoreListItemFactory {

    public LoadMoreListItemFactory(OnLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreListItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreListItem extends AbstractLoadMoreListItem {
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
然后调用enableLoadMore(AbstractLoadMoreListItemFactory)方法开启，如下：
```java
AssemblyAdapter adapter = ...;
adapter.enableLoadMore(new LoadMoreListItemFactory(new OnLoadMoreListener(){
	@Override
    public void onLoadMore(AssemblyAdapter adapter) {
        // ... 访问网络加载更多数据，最后调用adapter的loadMoreFinished()方法结束加载
		adapter.loadMoreFinished();
    }
}));
```
注意：
>* 加载完成了你需要调用adapter.loadMoreFinished()方法结束加载
>* 如果加载失败了你需要调用adapter.loadMoreFailed()方法，调用此方法后会自动调用LoadMoreListItem的showErrorRetry()方法显示失败提示，并且点击错误提示可以重新触发onLoadMore()方法
>* 如果没有更多数据可以加载了你就调用adapter.loadMoreEnd()设置结束，loadMoreEnd()方法会自动调用LoadMoreListItem的showEnd()方法显示结束提示，或者调用adapter.disableLoadMore()方法关闭加载更多功能

除此之外还有AssemblyExpandableAdapter和AssemblyRecyclerAdapter分别用在ExpandableListView和RecyclerView，其用法和和AssemblyAdapter完全一样，只是ItemFactory和Item所继承的类不一样而已，详情请参考其[sample](https://github.com/xiaopansky/AssemblyAdapter/tree/master/sample)源码

##License
```java
/*
* Copyright (C) 2015 Peng fei Pan <sky@xiaopan.me>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
```
