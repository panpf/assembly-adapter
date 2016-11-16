package me.xiaopan.assemblyadaptersample.itemfactory;

import android.support.v4.app.Fragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.fragment.ImageFragment;

public class ImageFragmentItemFactory extends AssemblyFragmentItemFactory<String> {
    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public Fragment createFragment(int position, String string) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(ImageFragment.buildParams(string));
        return imageFragment;
    }
}
