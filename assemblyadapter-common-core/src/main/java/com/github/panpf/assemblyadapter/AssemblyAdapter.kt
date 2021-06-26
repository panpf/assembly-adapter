package com.github.panpf.assemblyadapter

interface AssemblyAdapter {

    fun getItemFactoryByPosition(position: Int): MatchItemFactory
}