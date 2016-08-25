package me.xiaopan.assemblyadaptersample.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyExpandableAdapter;
import me.xiaopan.assemblyadapter.FixedGroupItemInfo;
import me.xiaopan.assemblyadapter.OnGroupLoadMoreListener;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Game;
import me.xiaopan.assemblyadaptersample.bean.GameGroup;
import me.xiaopan.assemblyadaptersample.bean.User;
import me.xiaopan.assemblyadaptersample.bean.UserGroup;
import me.xiaopan.assemblyadaptersample.itemfactory.GameChildItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.GameGroupItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.HeaderGroupItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.LoadMoreGroupItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.UserChildItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.UserGroupItemFactory;

public class ExpandableListViewFragment extends Fragment implements OnGroupLoadMoreListener {
    private int nextStart;
    private int groupSize = 20;
    private int childSize = 5;

    private AssemblyExpandableAdapter adapter;
    private ExpandableListView listView;
    private FixedGroupItemInfo headerItemInfo;
    private FixedGroupItemInfo footerItemInfo;
    private FixedGroupItemInfo headerItemInfo2;
    private FixedGroupItemInfo footerItemInfo2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expandable_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ExpandableListView) view.findViewById(R.id.expandableList_expandableListViewFragment_content);

        if (adapter != null) {
            listView.setAdapter(adapter);
        } else {
            loadData();
        }
    }

    private void loadData() {
        new AsyncTask<String, String, List<Object>>() {

            @Override
            protected List<Object> doInBackground(String... params) {
                List<Object> dataList = new ArrayList<Object>(groupSize);
                for (int w = 0; w < groupSize; w++) {
                    int groupPosition = w + nextStart;
                    if (groupPosition % 2 == 0) {
                        dataList.add(createUserGroup(groupPosition));
                    } else {
                        dataList.add(createGameGroup(groupPosition));
                    }
                }

                if (nextStart != 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return dataList;
            }

            private UserGroup createUserGroup(int groupPosition) {
                UserGroup userGroup = new UserGroup();
                userGroup.userList = new ArrayList<User>(childSize);
                for (int childPosition = 0; childPosition < childSize; childPosition++) {
                    userGroup.userList.add(createUser(groupPosition, childPosition));
                }
                userGroup.title = "用户组 " + (groupPosition + 1) + "(" + userGroup.userList.size() + ")";
                return userGroup;
            }

            private User createUser(int groupPosition, int childPosition) {
                User user = new User();
                user.headResId = R.mipmap.ic_launcher;
                user.name = "王大卫 " + (groupPosition + 1) + "." + (childPosition + 1);
                user.sex = (groupPosition % 2 == 0 && childPosition % 2 == 0) ? "男" : "女";
                user.age = "" + childPosition;
                user.job = "实施工程师";
                user.monthly = "" + 9000 + childPosition + 1;
                return user;
            }

            private GameGroup createGameGroup(int groupPosition) {
                GameGroup gameGroup = new GameGroup();
                gameGroup.gameList = new ArrayList<Game>(childSize);
                for (int childPosition = 0; childPosition < childSize; childPosition++) {
                    gameGroup.gameList.add(createGame(groupPosition, childPosition));
                }
                gameGroup.title = "游戏组 " + (groupPosition + 1) + "(" + gameGroup.gameList.size() + ")";
                return gameGroup;
            }

            private Game createGame(int groupPosition, int childPosition) {
                Game game = new Game();
                game.iconResId = R.mipmap.ic_launcher;
                game.name = "英雄联盟" + (groupPosition + 1) + "." + (childPosition + 1);
                game.like = (groupPosition % 2 != 0 && childPosition % 2 != 0) ? "不喜欢" : "喜欢";
                return game;
            }

            @Override
            protected void onPostExecute(List<Object> objects) {
                if (getActivity() == null) {
                    return;
                }

                nextStart += groupSize;
                if (adapter == null) {
                    adapter = new AssemblyExpandableAdapter(objects);

                    headerItemInfo = adapter.addHeaderItem(new HeaderGroupItemFactory(), "我是小额头呀！");
                    headerItemInfo2 = adapter.addHeaderItem(new HeaderGroupItemFactory(), "唉，我的小额头呢？");
                    adapter.addGroupItemFactory(new GameGroupItemFactory());
                    adapter.addGroupItemFactory(new UserGroupItemFactory());
                    adapter.addChildItemFactory(new GameChildItemFactory(getActivity().getBaseContext()));
                    adapter.addChildItemFactory(new UserChildItemFactory(getActivity().getBaseContext()));
                    adapter.setLoadMoreItem(new LoadMoreGroupItemFactory(ExpandableListViewFragment.this));
                    footerItemInfo = adapter.addFooterItem(new HeaderGroupItemFactory(), "我是小尾巴呀！");
                    footerItemInfo2 = adapter.addFooterItem(new HeaderGroupItemFactory(), "唉，我的小尾巴呢？");

                    listView.setAdapter(adapter);
                } else {
                    adapter.addAll(objects);

                    headerItemInfo2.setEnabled(!headerItemInfo2.isEnabled());
                    footerItemInfo2.setEnabled(!footerItemInfo2.isEnabled());
                }

                boolean loadMoreEnd = nextStart >= 100;
                if (loadMoreEnd) {
                    headerItemInfo.setEnabled(false);
                    footerItemInfo.setEnabled(false);
                }
                adapter.setLoadMoreEnd(loadMoreEnd);
            }
        }.execute("");
    }

    @Override
    public void onLoadMore(AssemblyExpandableAdapter adapter) {
        loadData();
    }
}
