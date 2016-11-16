package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyPagerItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Header;
import me.xiaopan.sketch.SketchImageView;

public class HeaderPagerItemFactory extends AssemblyPagerItemFactory<Header>{
    private View.OnClickListener clickListener;

    public HeaderPagerItemFactory(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof Header;
    }

    @Override
    public View createView(Context context, ViewGroup container, int position, Header header) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_header_image, container, false);

        TextView textView = (TextView) view.findViewById(R.id.text_headerImageFragment);
        textView.setText(header.text);

        SketchImageView imageView = (SketchImageView) view.findViewById(R.id.image_headerImageFragment);
        imageView.displayImage(header.imageUrl);

        view.setOnClickListener(clickListener);

        return view;
    }
}
