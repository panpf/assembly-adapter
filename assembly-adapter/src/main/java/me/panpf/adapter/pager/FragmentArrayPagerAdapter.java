/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.panpf.adapter.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseIntArray;

import java.util.List;

/**
 * {@link Fragment} 数组 {@link FragmentPagerAdapter}
 */
@SuppressWarnings("unused")
public class FragmentArrayPagerAdapter extends FragmentPagerAdapter {

    @NonNull
    private Fragment[] fragments;

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    public FragmentArrayPagerAdapter(@NonNull FragmentManager fm, @NonNull Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public FragmentArrayPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> fragments) {
        this(fm, fragments.toArray(new Fragment[fragments.size()]));
    }

    @NonNull
    public Fragment[] getFragments() {
        return fragments;
    }

    public void setFragments(@NonNull Fragment[] fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        //noinspection ConstantConditions
        return fragments != null ? fragments.length : 0;
    }

    public boolean isEnabledPositionNoneOnNotifyDataSetChanged() {
        return notifyNumberPool != null;
    }

    public void setEnabledPositionNoneOnNotifyDataSetChanged(boolean enabled) {
        if (enabled) {
            notifyNumberPool = new SparseIntArray();
            notifyNumber = 0;
        } else {
            notifyNumberPool = null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (notifyNumberPool != null) notifyNumber++;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (notifyNumberPool != null && notifyNumberPool.get(object.hashCode()) != notifyNumber) {
            notifyNumberPool.put(object.hashCode(), notifyNumber);
            return PagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}