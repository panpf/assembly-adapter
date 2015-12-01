package me.xiaopan.assemblyadaptersample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyAdapter;
import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadaptersample.itemfactory.SpinnerItemFactory;

public class SpinnerFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spinner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_spinnerFragment);

        List<String> stringList = new ArrayList<String>(10);
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");
        stringList.add("5");
        stringList.add("6");
        stringList.add("7");
        stringList.add("8");
        stringList.add("9");
        stringList.add("10");

        AssemblyAdapter adapter = new AssemblyAdapter(stringList);
        adapter.addItemFactory(new SpinnerItemFactory());
        spinner.setAdapter(adapter);
    }
}
