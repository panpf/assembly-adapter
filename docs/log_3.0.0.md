Adapter：
>* ``新增``. 支持header和footer，如下:
    ```java
    AssemblyAdapter adapter = new AssemblyAdapter(objects);    
    adapter.addHeaderItem(new LikeFooterItemFactory(), "我是小额头呀！");
    adapter.addItemFactory(new UserListItemFactory(getBaseContext()));
    adapter.addItemFactory(new GameListItemFactory(getBaseContext()));
    adapter.addFooterItem(new LikeFooterItemFactory(), "我是小尾巴呀！");
    adapter.setLoadMoreItem(new LoadMoreItemFactory(ListViewFragment.this));   
    listView.setAdapter(adapter);
    ```
>* ``新增``. 由于支持header和footer因此不得不增加一个getPositionInPart(int position)方法用于获取position在其各自区域的位置
>* ``新增``. 新增AssemblyPagerAdapter、AssemblyFragmentPagerAdapter、AssemblyFragmentStatePagerAdapter以及FragmentArrayPagerAdapter用于支持ViewPager 
>* ``修改``. enableLoadMore()方法改名为setLoadMoreItem()
>* ``修改``. setEnableLoadMore(boolean)方法去掉了，新增了一个setDisableLoadMore(boolean)用来关闭或开启加载更多功能
>* ``修改``. 不定长构造函数(Object... dataArray)改为(Object[] dataArray) 
    
ItemFactory：
>* ``修改``. AbstractLoadMoreListItemFactory重命名为AssemblyLoadMoreItemFactory
>* ``修改``. AbstractLoadMoreRecyclerItemFactory重命名为AssemblyLoadMoreRecyclerItemFactory
>* ``修改``. AbstractLoadMoreGroupItemFactory重命名为AssemblyLoadMoreGroupItemFactory

Item：
>* ``新增``. 新增ContentSetter，可以更方便的设置各种内容，如下
    ```java
    @Override
    protected void onSetData(int position, Game game) {
        getSetter()
                .setImageResource(R.id.image_gameListItem_icon, game.iconResId)
                .setText(R.id.text_gameListItem_name, game.name)
                .setText(R.id.text_gameListItem_like, game.like);
    }
    ```
>* ``修改``. AssemblyGroupItem的onSetData(int groupPosition, boolean isExpanded, BEAN bean)方法的参数改为onSetData(int position, BEAN bean)
>* ``修改``. AssemblyGroupItem的getGroupPosition()方法名改为getPosition()
>* ``修改``. AssemblyChildItem的onSetData(int groupPosition, int childPosition, boolean isLastChild, BEAN bean)方法的参数改为onSetData(int position, BEAN bean)
>* ``修改``. AssemblyChildItem的getChildPosition()方法名改为getPosition()
