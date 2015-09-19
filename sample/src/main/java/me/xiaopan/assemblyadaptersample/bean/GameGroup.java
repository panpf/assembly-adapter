package me.xiaopan.assemblyadaptersample.bean;

import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyGroup;

public class GameGroup implements AssemblyGroup {
    public String title;
    public List<Game> gameList;

    @Override
    public int getChildCount() {
        return gameList != null ? gameList.size() : 0;
    }

    @Override
    public Object getChild(int childPosition) {
        return gameList != null && childPosition < gameList.size() ? gameList.get(childPosition) : null;
    }
}
