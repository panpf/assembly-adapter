package com.github.panpf.assemblyadapter

interface ItemFactory {
    fun match(data: Any): Boolean
}