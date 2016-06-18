package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.sketch.SketchImageView;

public class ImageFragment extends Fragment {

    private String imageUrl;

    public static Bundle buildParams(String imageUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", imageUrl);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle params = getArguments();
        if (params != null) {
            imageUrl = params.getString("imageUrl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SketchImageView imageView = (SketchImageView) view.findViewById(R.id.image_imageFragment);
        imageView.displayImage(imageUrl);
    }
}
