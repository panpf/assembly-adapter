package me.xiaopan.assemblyadaptersample.itemfactory;

import android.support.v4.app.Fragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.bean.Text;
import me.xiaopan.assemblyadaptersample.fragment.TextFragment;

public class TextFragmentItemFactory extends AssemblyFragmentItemFactory<Text> {
    @Override
    public boolean isTarget(Object data) {
        return data instanceof Text;
    }

    @Override
    public Fragment createFragment(int position, Text text) {
        TextFragment textFragment = new TextFragment();
        textFragment.setArguments(TextFragment.buildParams(text.text));
        return textFragment;
    }
}
