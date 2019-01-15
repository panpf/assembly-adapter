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

package me.panpf.adapter.paged;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class DiffableDiffCallback extends DiffUtil.ItemCallback<Object> {

    @Override
    public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
        //noinspection ConstantConditions
        if (oldItem == null && newItem == null) return true;
        //noinspection ConstantConditions
        if (oldItem == null || newItem == null) return false;
        if (oldItem.getClass().equals(newItem.getClass()) && oldItem instanceof Diffable && newItem instanceof Diffable) {
            //noinspection unchecked
            return ((Diffable) oldItem).areItemsTheSame(newItem);
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(@Nullable Object oldItem, @Nullable Object newItem) {
        if (oldItem == null && newItem == null) return true;
        if (oldItem == null || newItem == null) return false;
        if (oldItem.getClass().equals(newItem.getClass()) && oldItem instanceof Diffable && newItem instanceof Diffable) {
            //noinspection unchecked
            return ((Diffable) oldItem).areContentsTheSame(newItem);
        }
        return false;
    }
}
