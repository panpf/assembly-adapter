package com.github.panpf.assemblyadapter.sample

import androidx.multidex.MultiDexApplication
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.SketchFactory
import com.github.panpf.sketch.decode.AppIconBitmapDecoder
import com.github.panpf.sketch.fetch.AppIconUriFetcher

class MyApplication : MultiDexApplication(), SketchFactory {

    override fun createSketch(): Sketch =
        Sketch.Builder(this)
            .components {
                addFetcher(AppIconUriFetcher.Factory())
                addBitmapDecoder(AppIconBitmapDecoder.Factory())
            }.build()
}