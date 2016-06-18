package me.xiaopan.assemblyadaptersample.itemfactory;

import android.support.v4.app.Fragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.bean.HeaderImage;
import me.xiaopan.assemblyadaptersample.fragment.HeaderImageFragment;

public class HeaderImageFragmentItemFactory extends AssemblyFragmentItemFactory<HeaderImage> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof HeaderImage;
    }

    @Override
    public Fragment createFragment(int position, HeaderImage headerImage) {
        HeaderImageFragment headerImageFragment = new HeaderImageFragment();
        headerImageFragment.setArguments(HeaderImageFragment.buildParams(headerImage.text, headerImage.imageUrl));
        return headerImageFragment;
    }
}
