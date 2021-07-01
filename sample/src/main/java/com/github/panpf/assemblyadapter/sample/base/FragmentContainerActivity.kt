/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
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
package com.github.panpf.assemblyadapter.sample.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.databinding.AcivityFragmentContainerBinding

class FragmentContainerActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context, title: String, subTitle: String?, fragment: Fragment) =
            Intent(context, FragmentContainerActivity::class.java).apply {
                putExtra("title", title)
                putExtra("subTitle", subTitle)
                putExtra("fragmentClassName", fragment::class.java.name)
                putExtra("fragmentArguments", fragment.arguments)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            AcivityFragmentContainerBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val pageTitle = intent.getStringExtra("title") ?: getString(R.string.app_name)
        val pageSubTitle = intent.getStringExtra("subTitle")
        val fragmentClassName = intent.getStringExtra("fragmentClassName")
            ?: throw IllegalArgumentException("Not found param 'fragmentClassName'")
        val fragmentArguments = intent.getBundleExtra("fragmentArguments")

        setSupportActionBar(binding.fragmentContainerToolbar.apply {
            title = pageTitle
            subtitle = pageSubTitle
        })

        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainerContainer.id,
                (Class.forName(fragmentClassName).newInstance() as Fragment).apply {
                    arguments = fragmentArguments
                })
            .commit()
    }
}