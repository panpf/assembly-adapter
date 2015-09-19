package me.xiaopan.assemblyadaptersample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import me.xiaopan.assemblyadaptersample.fragment.ExpandableListViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.ListViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.RecyclerViewFragment;
import me.xiaopan.psts.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_mainActivity_content);
        viewPager.setAdapter(new FragmentListAdapter(getSupportFragmentManager(), new Fragment[]{
                new ListViewFragment(),
                new RecyclerViewFragment(),
                new ExpandableListViewFragment()
        }));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip_mainActivity_tabs);
        tabStrip.setViewPager(viewPager);
    }

    private static class FragmentListAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public FragmentListAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}