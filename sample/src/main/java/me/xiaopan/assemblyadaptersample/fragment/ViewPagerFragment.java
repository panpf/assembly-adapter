package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AssemblyFragmentStatePagerAdapter;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Header;
import me.xiaopan.assemblyadaptersample.bean.Text;
import me.xiaopan.assemblyadaptersample.itemfactory.HeaderFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.ImageFragmentItemFactory;
import me.xiaopan.assemblyadaptersample.itemfactory.TextFragmentItemFactory;

public class ViewPagerFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_viewPagerFragment_content);

        Header header = new Header("我是小额头呀！", "http://img4.imgtn.bdimg.com/it/u=16705507,1328875785&fm=21&gp=0.jpg");
        Object[] dataArray = new Object[]{
                "http://news.yzz.cn/public/images/100829/68_184435_2_lit.jpg",
                "http://fj.china.com.cn/uploadfile/2016/0604/1465022181606906.jpg",
                "http://www.nrhui.com/uploads/allimg/160522/6-160522194532W7.jpg",
                "http://image-qzone.mamaquan.mama.cn/upload/2015/03/20/9d4e2fda0e904bcca07a_w300X405_w196X264.jpeg",
                "http://img1.imgtn.bdimg.com/it/u=2055412405,1351161078&fm=21&gp=0.jpg",
                new Text("华丽的分割线"),
                "http://image.xinmin.cn/2012/03/01/20120301080259557027.jpg",
                "http://img3.cache.netease.com/photo/0026/2013-06-06/90MP0B4N43AJ0026.jpg",
                "http://img3.imgtn.bdimg.com/it/u=533822629,3189843728&fm=21&gp=0.jpg",
                "http://www.bz55.com/uploads/allimg/150326/140-150326115T2.jpg",
                "http://www.ycmhz.com.cn/jiahe/UploadFiles_8568/201110/2011101817440022.jpg",
        };
        Header footer = new Header("我是小尾巴呀！", "http://img2.imgtn.bdimg.com/it/u=4104440447,1888517305&fm=21&gp=0.jpg");

        AssemblyFragmentStatePagerAdapter adapter = new AssemblyFragmentStatePagerAdapter(getChildFragmentManager(), dataArray);
        adapter.addHeaderItem(new HeaderFragmentItemFactory(), header);
        adapter.addItemFactory(new ImageFragmentItemFactory());
        adapter.addItemFactory(new TextFragmentItemFactory());
        adapter.addFooterItem(new HeaderFragmentItemFactory(), footer);
        viewPager.setAdapter(adapter);
    }
}
