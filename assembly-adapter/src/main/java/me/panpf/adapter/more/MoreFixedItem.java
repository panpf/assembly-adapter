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

package me.panpf.adapter.more;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.panpf.adapter.FixedItem;

public class MoreFixedItem<DATA> extends FixedItem<DATA> {

    public MoreFixedItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data) {
        super(itemFactory, data);
    }

    public MoreFixedItem(@NonNull MoreItemFactory<DATA> itemFactory) {
        super(itemFactory, null);
    }

    @NonNull
    @Override
    public MoreItemFactory<DATA> getItemFactory() {
        return (MoreItemFactory<DATA>) super.getItemFactory();
    }

    @Override
    protected void enableChanged() {
//        super.enableChanged();
        // no refresh
    }

    @NonNull
    @Override
    public MoreFixedItem setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            loadMoreFinished(false);
        }
        return this;
    }

    public void loadMoreFinished(boolean end) {
        getItemFactory().loadMoreFinished(end);
    }

    public void loadMoreFailed() {
        getItemFactory().loadMoreFailed();
    }
}
