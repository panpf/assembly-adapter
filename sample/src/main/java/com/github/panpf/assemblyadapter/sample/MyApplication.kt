package com.github.panpf.assemblyadapter.sample

import androidx.multidex.MultiDexApplication
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.SketchConfigurator
import com.github.panpf.sketch.decode.AppIconBitmapDecoder
import com.github.panpf.sketch.fetch.AppIconUriFetcher

class MyApplication : MultiDexApplication(), SketchConfigurator {

    override fun createSketchConfig(): Sketch.Builder.() -> Unit = {
        components {
            addFetcher(AppIconUriFetcher.Factory())
            addBitmapDecoder(AppIconBitmapDecoder.Factory())
        }
    }
}