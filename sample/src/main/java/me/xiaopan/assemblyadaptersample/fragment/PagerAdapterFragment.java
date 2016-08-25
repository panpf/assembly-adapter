package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AssemblyPagerAdapter;
import me.xiaopan.assemblyadapter.FixedPagerItemInfo;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Header;
import me.xiaopan.assemblyadaptersample.bean.Text;
import me.xiaopan.assemblyadaptersample.itemfactory.HeaderPagerItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.ImagePagerItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.TextPagerItemFactory;

public class PagerAdapterFragment extends Fragment{
    FixedPagerItemInfo headerItemInfo;
    FixedPagerItemInfo footerItemInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_viewPagerFragment_content);

        Header header = new Header("我是小额头呀！\n你敢戳我，我就敢消失！哼！", "http://www.bz55.com/uploads/allimg/150605/139-150605153433-50.jpg");
        Object[] dataArray = new Object[]{
            "http://www.bz55.com/uploads/allimg/130521/1-130521100358.jpg",
            "http://img4q.duitang.com/uploads/item/201506/12/20150612095354_5w3sj.jpeg",
            "http://img5q.duitang.com/uploads/blog/201412/11/20141211160750_hFrss.jpeg",
            "http://pic.yesky.com/uploadImages/2015/147/00/0VC1P4UAR2V6.jpg",
            "http://img5q.duitang.com/uploads/item/201504/10/20150410H1528_F4rEf.jpeg",
            new Text("分割线又来了"),
            "http://img4.duitang.com/uploads/item/201509/19/20150919212952_femTB.thumb.700_0.jpeg",
            "http://www.bz55.com/uploads/allimg/150422/139-1504221GZ4.jpg",
            "http://www.bz55.com/uploads/allimg/150720/139-150H0110948.jpg",
            "http://www.bz55.com/uploads/allimg/150603/139-150603141319-50.jpg",
            "http://www.bz55.com/uploads/allimg/150326/140-150326141215-50.jpg",
        };
        Header footer = new Header("我是小尾巴呀！\n你敢戳我，我也敢消失！哼！", "http://www.bz55.com/uploads/allimg/150720/139-150H0110925.jpg");

        final AssemblyPagerAdapter adapter = new AssemblyPagerAdapter(dataArray);
        headerItemInfo = adapter.addHeaderItem(new HeaderPagerItemFactory(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerItemInfo.setEnabled(false);
                viewPager.setAdapter(null);
                viewPager.setAdapter(adapter);
            }
        }), header);
        adapter.addItemFactory(new ImagePagerItemFactory());
        adapter.addItemFactory(new TextPagerItemFactory());
        footerItemInfo = adapter.addFooterItem(new HeaderPagerItemFactory(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerItemInfo.setEnabled(false);
                viewPager.setAdapter(null);
                viewPager.setAdapter(adapter);
            }
        }), footer);
        viewPager.setAdapter(adapter);
    }
}