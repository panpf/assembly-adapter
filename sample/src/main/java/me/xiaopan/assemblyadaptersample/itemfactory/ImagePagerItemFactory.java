package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AssemblyPagerItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.sketch.SketchImageView;

public class ImagePagerItemFactory extends AssemblyPagerItemFactory<String>{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public View createView(Context context, ViewGroup container, int position, String imageUrl) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_image, container, false);

        SketchImageView imageView = (SketchImageView) view.findViewById(R.id.image_imageFragment);
        imageView.displayImage(imageUrl);

        return view;
    }
}
