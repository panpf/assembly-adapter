package me.xiaopan.assemblyadaptersample.itemfactory;

import android.support.v4.app.Fragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.bean.Header;
import me.xiaopan.assemblyadaptersample.fragment.HeaderFragment;

public class HeaderFragmentItemFactory extends AssemblyFragmentItemFactory<Header> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof Header;
    }

    @Override
    public Fragment createFragment(int position, Header header) {
        HeaderFragment headerFragment = new HeaderFragment();
        headerFragment.setArguments(HeaderFragment.buildParams(header.text, header.imageUrl));
        return headerFragment;
    }
}
