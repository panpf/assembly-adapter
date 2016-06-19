package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.sketch.SketchImageView;

public class HeaderFragment extends Fragment {

    private String text;
    private String imageUrl;

    public static Bundle buildParams(String text, String imageUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        bundle.putString("imageUrl", imageUrl);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle params = getArguments();
        if (params != null) {
            text = params.getString("text");
            imageUrl = params.getString("imageUrl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.text_headerImageFragment);
        textView.setText(text);

        SketchImageView imageView = (SketchImageView) view.findViewById(R.id.image_headerImageFragment);
        imageView.displayImage(imageUrl);
    }
}
