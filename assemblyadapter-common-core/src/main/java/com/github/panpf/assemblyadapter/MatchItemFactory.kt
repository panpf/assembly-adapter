package com.github.panpf.assemblyadapter

interface MatchItemFactory {
    fun match(data: Any): Boolean
}