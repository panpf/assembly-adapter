package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyPagerItemFactory;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.bean.Text;

public class TextPagerItemFactory extends AssemblyPagerItemFactory<Text>{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof Text;
    }

    @Override
    public View createView(Context context, ViewGroup container, int position, Text text) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_text, container, false);

        TextView textView = (TextView) view.findViewById(R.id.text_imageFragment_content);
        textView.setText(text.text);

        return textView;
    }
}
