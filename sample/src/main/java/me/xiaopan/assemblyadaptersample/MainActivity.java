package me.xiaopan.assemblyadaptersample;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import me.xiaopan.assemblyadapter.FragmentArrayPagerAdapter;
import me.xiaopan.assemblyadaptersample.fragment.ExpandableListViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.GridRecyclerViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.ListViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.PagerAdapterFragment;
import me.xiaopan.assemblyadaptersample.fragment.RecyclerViewFragment;
import me.xiaopan.assemblyadaptersample.fragment.SpinnerFragment;
import me.xiaopan.assemblyadaptersample.fragment.ViewPagerFragment;
import me.xiaopan.psts.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_mainActivity_content);
        viewPager.setAdapter(new FragmentArrayPagerAdapter(getSupportFragmentManager(), new Fragment[]{
                new ListViewFragment(),
                new RecyclerViewFragment(),
                new GridRecyclerViewFragment(),
                new ExpandableListViewFragment(),
                new SpinnerFragment(),
                new ViewPagerFragment(),
                new PagerAdapterFragment(),
        }));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip_mainActivity_tabs);
        tabStrip.setViewPager(viewPager);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getActionBar().setElevation(0);
        }
    }
}