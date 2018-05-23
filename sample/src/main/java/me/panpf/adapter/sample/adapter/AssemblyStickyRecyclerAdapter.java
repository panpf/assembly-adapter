package me.panpf.adapter.sample.adapter;

import java.util.List;

import me.panpf.adapter.AssemblyRecyclerAdapter;
import me.panpf.adapter.ItemFactory;

public class AssemblyStickyRecyclerAdapter extends AssemblyRecyclerAdapter implements StickyRecyclerItemDecoration.StickyAdapter {
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
