/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.panpf.assemblyadapter.list.concat.expandable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ConcatExpandableListAdapter extends BaseExpandableListAdapter {
    public static final long NO_ID = -1;
    static final String TAG = "ConcatExpandableListAdapter";
    /**
     * Bulk of the logic is in the controller to keep this class isolated to the public API.
     */
    private final ConcatExpandableListAdapterController mController;

    /**
     * Creates a ConcatExpandableListAdapter with {@link Config#DEFAULT} and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    public ConcatExpandableListAdapter(@NonNull BaseExpandableListAdapter... adapters) {
        this(Config.DEFAULT, adapters);
    }

    /**
     * Creates a ConcatExpandableListAdapter with the given config and the given adapters in the given order.
     *
     * @param config   The configuration for this ConcatExpandableListAdapter
     * @param adapters The list of adapters to add
     * @see Config.Builder
     */
    public ConcatExpandableListAdapter(
            @NonNull Config config,
            @NonNull BaseExpandableListAdapter... adapters) {
        this(config, Arrays.asList(adapters));
    }

    /**
     * Creates a ConcatExpandableListAdapter with {@link Config#DEFAULT} and the given adapters in the given
     * order.
     *
     * @param adapters The list of adapters to add
     */
    public ConcatExpandableListAdapter(@NonNull List<? extends BaseExpandableListAdapter> adapters) {
        this(Config.DEFAULT, adapters);
    }

    /**
     * Creates a ConcatExpandableListAdapter with the given config and the given adapters in the given order.
     *
     * @param config   The configuration for this ConcatExpandableListAdapter
     * @param adapters The list of adapters to add
     * @see Config.Builder
     */
    public ConcatExpandableListAdapter(
            @NonNull Config config,
            @NonNull List<? extends BaseExpandableListAdapter> adapters) {
        mController = new ConcatExpandableListAdapterController(this, config);
        for (BaseExpandableListAdapter adapter : adapters) {
            addAdapter(adapter);
        }
    }

    /**
     * Appends the given adapter to the existing list of adapters and notifies the observers of
     * this {@link ConcatExpandableListAdapter}.
     *
     * @param adapter The new adapter to add
     * @return {@code true} if the adapter is successfully added because it did not already exist,
     * {@code false} otherwise.
     * @see #addAdapter(int, BaseExpandableListAdapter)
     * @see #removeAdapter(BaseExpandableListAdapter)
     */
    public boolean addAdapter(@NonNull BaseExpandableListAdapter adapter) {
        return mController.addAdapter(adapter);
    }

    /**
     * Adds the given adapter to the given index among other adapters that are already added.
     *
     * @param index   The index into which to insert the adapter. ConcatExpandableListAdapter will throw an
     *                {@link IndexOutOfBoundsException} if the index is not between 0 and current
     *                adapter count (inclusive).
     * @param adapter The new adapter to add to the adapters list.
     * @return {@code true} if the adapter is successfully added because it did not already exist,
     * {@code false} otherwise.
     * @see #addAdapter(BaseExpandableListAdapter)
     * @see #removeAdapter(BaseExpandableListAdapter)
     */
    public boolean addAdapter(int index, @NonNull BaseExpandableListAdapter adapter) {
        return mController.addAdapter(index, adapter);
    }

    /**
     * Removes the given adapter from the adapters list if it exists
     *
     * @param adapter The adapter to remove
     * @return {@code true} if the adapter was previously added to this {@code ConcatExpandableListAdapter} and
     * now removed or {@code false} if it couldn't be found.
     */
    public boolean removeAdapter(@NonNull BaseExpandableListAdapter adapter) {
        return mController.removeAdapter(adapter);
    }

    /**
     * Returns an unmodifiable copy of the list of adapters in this {@link ConcatExpandableListAdapter}.
     * Note that this is a copy hence future changes in the ConcatExpandableListAdapter are not reflected in
     * this list.
     *
     * @return A copy of the list of adapters in this ConcatExpandableListAdapter.
     */
    @NonNull
    public List<? extends BaseExpandableListAdapter> getAdapters() {
        return Collections.unmodifiableList(mController.getCopyOfAdapters());
    }


    @Override
    public int getGroupCount() {
        return mController.getGroupCount();
    }

    @Nullable
    @Override
    public Object getGroup(int groupPosition) {
        return mController.getGroup(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mController.getGroupId(groupPosition);
    }

    @Override
    public int getGroupTypeCount() {
        return mController.getGroupTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return mController.getGroupType(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, @Nullable View convertView, @NonNull ViewGroup parent) {
        return mController.getGroupView(groupPosition, isExpanded, convertView, parent);
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return mController.getChildrenCount(groupPosition);
    }

    @Nullable
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mController.getChild(groupPosition, childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mController.getChildId(groupPosition, childPosition);
    }

    @Override
    public int getChildTypeCount() {
        return mController.getChildTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return mController.getChildType(groupPosition, childPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, @Nullable View convertView, @NonNull ViewGroup parent) {
        return mController.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
    }


    @Override
    public boolean hasStableIds() {
        return mController.hasStableIds();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return mController.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        mController.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        mController.onGroupExpanded(groupPosition);
    }


    @NonNull
    public ExpandableListWrapperAndLocalPosition findWrapperAndLocalPosition(int position, ExpandableListWrapperAndLocalPosition wrapperAndLocalPosition) {
        return mController.findWrapperAndLocalPosition(position, wrapperAndLocalPosition);
    }

    /**
     * The configuration object for a {@link ConcatExpandableListAdapter}.
     */
    public static final class Config {
        /**
         * If {@code false}, {@link ConcatExpandableListAdapter} assumes all assigned adapters share a global
         * view type pool such that they use the same view types to refer to the same convertView.
         * <p>
         * Setting this to {@code false} will allow nested adapters to share convertViews but
         * it also means these adapters should not have conflicting view types
         * ({@link BaseExpandableListAdapter#getGroupType(int)} or {@link BaseExpandableListAdapter#getChildType(int, int)}) such that two different adapters return the same
         * view type for different convertViews.
         * <p>
         * By default, it is set to {@code true} which means {@link ConcatExpandableListAdapter} will isolate
         * view types across adapters, preventing them from using the same convertViews.
         */
        public final boolean isolateViewTypes;

        /**
         * Defines whether the {@link ConcatExpandableListAdapter} should support stable ids or not
         * ({@link BaseExpandableListAdapter#hasStableIds()}.
         * <p>
         * There are 3 possible options:
         * <p>
         * {@link StableIdMode#NO_STABLE_IDS}: In this mode, {@link ConcatExpandableListAdapter} ignores the
         * stable
         * ids reported by sub adapters. This is the default mode.
         * <p>
         * {@link StableIdMode#ISOLATED_STABLE_IDS}: In this mode, {@link ConcatExpandableListAdapter} will return
         * {@code true} from {@link ConcatExpandableListAdapter#hasStableIds()} and will <b>require</b> all added
         * {@link BaseExpandableListAdapter}s to have stable ids. As two different adapters may return same stable ids
         * because they are unaware of each-other, {@link ConcatExpandableListAdapter} will isolate each
         * {@link BaseExpandableListAdapter}'s id pool from each other such that it will overwrite the reported stable
         * id before reporting back to the {@link ListView}.
         * <p>
         * {@link StableIdMode#SHARED_STABLE_IDS}: In this mode, {@link ConcatExpandableListAdapter} will return
         * {@code true} from {@link ConcatExpandableListAdapter#hasStableIds()} and will <b>require</b> all added
         * {@link BaseExpandableListAdapter}s to have stable ids. Unlike {@link StableIdMode#ISOLATED_STABLE_IDS},
         * {@link ConcatExpandableListAdapter} will not override the returned item ids. In this mode,
         * child {@link BaseExpandableListAdapter}s must be aware of each-other and never return the same id unless
         * an item is moved between {@link BaseExpandableListAdapter}s.
         * <p>
         * Default value is {@link StableIdMode#NO_STABLE_IDS}.
         */
        @NonNull
        public final StableIdMode stableIdMode;


        /**
         * Default configuration for {@link ConcatExpandableListAdapter} where {@link Config#isolateViewTypes}
         * is set to {@code true} and {@link Config#stableIdMode} is set to
         * {@link StableIdMode#NO_STABLE_IDS}.
         */
        @NonNull
        public static final Config DEFAULT = new Config(true, StableIdMode.NO_STABLE_IDS);

        Config(boolean isolateViewTypes, @NonNull StableIdMode stableIdMode) {
            this.isolateViewTypes = isolateViewTypes;
            this.stableIdMode = stableIdMode;
        }

        /**
         * Defines how {@link ConcatExpandableListAdapter} handle stable ids ({@link BaseExpandableListAdapter#hasStableIds()}).
         */
        public enum StableIdMode {
            /**
             * In this mode, {@link ConcatExpandableListAdapter} ignores the stable
             * ids reported by sub adapters. This is the default mode.
             * Adding an {@link BaseExpandableListAdapter} with stable ids will result in a warning as it will be
             * ignored.
             */
            NO_STABLE_IDS,
            /**
             * In this mode, {@link ConcatExpandableListAdapter} will return {@code true} from
             * {@link ConcatExpandableListAdapter#hasStableIds()} and will <b>require</b> all added
             * {@link BaseExpandableListAdapter}s to have stable ids. As two different adapters may return
             * same stable ids because they are unaware of each-other, {@link ConcatExpandableListAdapter} will
             * isolate each {@link BaseExpandableListAdapter}'s id pool from each other such that it will overwrite
             * the reported stable id before reporting back to the {@link ListView}. In this
             * mode, the value returned from ({@link BaseExpandableListAdapter#getGroupId(int)} or {@link BaseExpandableListAdapter#getChildId(int, int)}) might differ from the
             * value returned from ({@link BaseExpandableListAdapter#getGroupId(int)} or {@link BaseExpandableListAdapter#getChildId(int, int)}).
             * <p>
             * Adding an adapter without stable ids will result in an
             * {@link IllegalArgumentException}.
             */
            ISOLATED_STABLE_IDS,
            /**
             * In this mode, {@link ConcatExpandableListAdapter} will return {@code true} from
             * {@link ConcatExpandableListAdapter#hasStableIds()} and will <b>require</b> all added
             * {@link BaseExpandableListAdapter}s to have stable ids. Unlike {@link StableIdMode#ISOLATED_STABLE_IDS},
             * {@link ConcatExpandableListAdapter} will not override the returned item ids. In this mode,
             * child {@link BaseExpandableListAdapter}s must be aware of each-other and never return the same id
             * unless and item is moved between {@link BaseExpandableListAdapter}s.
             * Adding an adapter without stable ids will result in an
             * {@link IllegalArgumentException}.
             */
            SHARED_STABLE_IDS
        }

        /**
         * The builder for {@link Config} class.
         */
        public static final class Builder {
            private boolean mIsolateViewTypes = DEFAULT.isolateViewTypes;
            private StableIdMode mStableIdMode = DEFAULT.stableIdMode;

            /**
             * Sets whether {@link ConcatExpandableListAdapter} should isolate view types of nested adapters from
             * each other.
             *
             * @param isolateViewTypes {@code true} if {@link ConcatExpandableListAdapter} should override view
             *                         types of nested adapters to avoid view type
             *                         conflicts, {@code false} otherwise.
             *                         Defaults to {@link Config#DEFAULT}'s
             *                         {@link Config#isolateViewTypes} value ({@code true}).
             * @return this
             * @see Config#isolateViewTypes
             */
            @NonNull
            public Builder setIsolateViewTypes(boolean isolateViewTypes) {
                mIsolateViewTypes = isolateViewTypes;
                return this;
            }

            /**
             * Sets how the {@link ConcatExpandableListAdapter} should handle stable ids
             * ({@link BaseExpandableListAdapter#hasStableIds()}). See documentation in {@link Config#stableIdMode}
             * for details.
             *
             * @param stableIdMode The stable id mode for the {@link ConcatExpandableListAdapter}. Defaults to
             *                     {@link Config#DEFAULT}'s {@link Config#stableIdMode} value
             *                     ({@link StableIdMode#NO_STABLE_IDS}).
             * @return this
             * @see Config#stableIdMode
             */
            @NonNull
            public Builder setStableIdMode(@NonNull StableIdMode stableIdMode) {
                mStableIdMode = stableIdMode;
                return this;
            }

            /**
             * @return A new instance of {@link Config} with the given parameters.
             */
            @NonNull
            public Config build() {
                return new Config(mIsolateViewTypes, mStableIdMode);
            }
        }
    }
}
