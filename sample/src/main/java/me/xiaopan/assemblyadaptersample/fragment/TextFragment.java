package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadaptersample.R;

public class TextFragment extends Fragment{

    private String text;

    public static Bundle buildParams(String text) {
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle params = getArguments();
        if (params != null) {
            text = params.getString("text");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.text_imageFragment_content);
        textView.setText(text);
    }
}
