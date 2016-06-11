package me.xiaopan.assemblyadaptersample.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyAdapter;
import me.xiaopan.assemblyadapter.OnLoadMoreListener;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Game;
import me.xiaopan.assemblyadaptersample.bean.User;
import me.xiaopan.assemblyadaptersample.itemfactory.GameListItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.LikeFooterItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.LoadMoreItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.UserListItemFactory;

public class ListViewFragment extends Fragment implements OnLoadMoreListener {
    private int nextStart;
    private int size = 20;

    private AssemblyAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list_listViewFragment_content);

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
                int index = 0;
                List<Object> dataList = new ArrayList<Object>(size);
                boolean userStatus = true;
                boolean gameStatus = true;
                while (index < size) {
                    if (index % 2 == 0) {
                        User user = new User();
                        user.headResId = R.mipmap.ic_launcher;
                        user.name = "王大卫" + (index + nextStart + 1);
                        user.sex = userStatus ? "男" : "女";
                        user.age = "" + (index + nextStart + 1);
                        user.job = "实施工程师";
                        user.monthly = "" + 9000 + index + nextStart + 1;
                        dataList.add(user);
                        userStatus = !userStatus;
                    } else {
                        Game game = new Game();
                        game.iconResId = R.mipmap.ic_launcher;
                        game.name = "英雄联盟" + (index + nextStart + 1);
                        game.like = gameStatus ? "不喜欢" : "喜欢";
                        dataList.add(game);
                        gameStatus = !gameStatus;
                    }
                    index++;
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

            @Override
            protected void onPostExecute(List<Object> objects) {
                if (getActivity() == null) {
                    return;
                }

                nextStart += size;
                if (adapter == null) {
                    adapter = new AssemblyAdapter(objects);

                    adapter.addHeaderFactory(new LikeFooterItemFactory(), "我是小额头呀！");
                    adapter.addItemFactory(new UserListItemFactory(getActivity().getBaseContext()));
                    adapter.addItemFactory(new GameListItemFactory(getActivity().getBaseContext()));
                    adapter.addFooterFactory(new LikeFooterItemFactory(), "我是小尾巴呀！");

                    adapter.setLoadMoreItemFactory(new LoadMoreItemFactory(ListViewFragment.this));
                    adapter.setLoadMoreEnd(nextStart >= 100);

                    listView.setAdapter(adapter);
                } else {
                    adapter.addAll(objects);
                    adapter.setLoadMoreEnd(nextStart == 100);
                }
            }
        }.execute("");
    }

    @Override
    public void onLoadMore(AssemblyAdapter adapter) {
        loadData();
    }
}
