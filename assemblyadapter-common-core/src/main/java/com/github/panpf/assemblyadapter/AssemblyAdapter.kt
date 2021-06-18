package com.github.panpf.assemblyadapter

import com.github.panpf.assemblyadapter.internal.BaseItemFactory

interface AssemblyAdapter {

    fun getItemFactoryByPosition(position: Int): BaseItemFactory
}