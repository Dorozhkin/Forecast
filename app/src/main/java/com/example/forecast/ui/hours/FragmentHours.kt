package com.example.forecast.ui.hours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.databinding.FragmentHoursBinding
import com.example.forecast.di.AppComponent
import com.example.forecast.di.ComponentStorage
import com.example.forecast.viewmodels.SharedViewModel

class FragmentHours: Fragment() {
    lateinit var binding: FragmentHoursBinding
    lateinit var appComponent: AppComponent
    lateinit var sharedViewModel: SharedViewModel
    var adapter = AdapterHours()

    companion object {
        var instance: FragmentHours? = null
            get() {
                if (field == null) {
                    field = FragmentHours()
                }
                return field
            }
            private set
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHoursBinding.inflate(inflater, container, false)

        appComponent = (requireActivity().application as ComponentStorage).component
        sharedViewModel =  ViewModelProvider(requireActivity(), appComponent.getViewModelFactory()).get(SharedViewModel::class.java)

        val llm = LinearLayoutManager(context)
        llm.orientation = RecyclerView.VERTICAL
        adapter.fragmentHoursBinding = binding
        binding.recycler.layoutManager = llm
        binding.recycler.adapter = adapter


        sharedViewModel.hourDetailFlow.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()
            binding.shimmerContainer.stopShimmerAnimation()
            binding.recycler.visibility = View.VISIBLE
            binding.shimmerContainer.visibility = View.GONE
        })

        binding.shimmerContainer.startShimmerAnimation()
        return binding.root
    }

    override fun onPause() {
        binding.shimmerContainer.stopShimmerAnimation()
        super.onPause()
    }

    override fun onResume() {
        binding.shimmerContainer.startShimmerAnimation()
        super.onResume()
    }
}