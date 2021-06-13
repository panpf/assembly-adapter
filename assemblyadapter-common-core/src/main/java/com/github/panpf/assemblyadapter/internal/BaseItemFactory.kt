package com.github.panpf.assemblyadapter.internal

interface BaseItemFactory {
    fun match(data: Any?): Boolean
}