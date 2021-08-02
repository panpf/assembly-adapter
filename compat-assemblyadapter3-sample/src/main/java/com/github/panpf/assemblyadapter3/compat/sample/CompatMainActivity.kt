package com.github.panpf.assemblyadapter3.compat.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.github.panpf.assemblyadapter3.compat.sample.databinding.ActivityMainBinding
import com.github.panpf.assemblyadapter3.compat.sample.ui.CompatMainFragment

class CompatMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainerView.id, CompatMainFragment())
            .commit()
    }
}