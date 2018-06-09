package me.panpf.adapter.sample.adapter;

import java.util.List;

import me.panpf.adapter.AssemblyRecyclerAdapter;
import me.panpf.adapter.ItemFactory;
import me.panpf.recycler.sticky.StickyRecyclerAdapter;

public class AssemblyStickyRecyclerAdapter extends AssemblyRecyclerAdapter implements StickyRecyclerAdapter {
    @Override
    public boolean isStickyItemByType(int type) {
        List<ItemFactory> itemFactoryList = getItemFactoryList();
        if (itemFactoryList != null && itemFactoryList.size() > 0) {
            for (ItemFactory itemFactory : itemFactoryList) {
                if (itemFactory.getItemType() == type && itemFactory instanceof StickyItemFactory) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface StickyItemFactory {

    }
}
