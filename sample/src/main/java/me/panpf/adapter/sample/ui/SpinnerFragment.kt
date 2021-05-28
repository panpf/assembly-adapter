package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import me.panpf.adapter.AssemblyListAdapter
import me.panpf.adapter.sample.databinding.FmSpinnerBinding
import me.panpf.adapter.sample.item.SpinnerItem
import java.util.*

class SpinnerFragment : BaseBindingFragment<FmSpinnerBinding>() {

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmSpinnerBinding {
        return FmSpinnerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmSpinnerBinding, savedInstanceState: Bundle?) {
        val stringList = ArrayList<String>(10)
        stringList.add("1")
        stringList.add("2")
        stringList.add("3")
        stringList.add("4")
        stringList.add("5")
        stringList.add("6")
        stringList.add("7")
        stringList.add("8")
        stringList.add("9")
        stringList.add("10")

        val adapter = AssemblyListAdapter(stringList)
        adapter.addItemFactory(SpinnerItem.Factory())
        binding.spinnerFmSpinner.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = "Spinner"
    }
}
