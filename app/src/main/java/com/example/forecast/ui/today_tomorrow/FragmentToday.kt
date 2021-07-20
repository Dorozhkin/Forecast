package com.example.forecast.ui.today_tomorrow

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.forecast.databinding.FragmentTodayBinding
import com.example.forecast.di.AppComponent
import com.example.forecast.di.ComponentStorage
import com.example.forecast.ui.MainActivity
import com.example.forecast.viewmodels.SharedViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentToday: Fragment() {
    lateinit var binding: FragmentTodayBinding
    lateinit var appComponent: AppComponent
    lateinit var sharedViewModel: SharedViewModel
    var adapter = TodayTomorrowAdapter()

    companion object {
        var instance: FragmentToday? = null
            get() {
                if (field == null) {
                    field = FragmentToday()
                }
                return field
            }
            private set
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        appComponent = (requireActivity().application as ComponentStorage).component
        sharedViewModel =  ViewModelProvider(requireActivity(), appComponent.getViewModelFactory()).get(SharedViewModel::class.java)

        val llm = LinearLayoutManager(context)
        llm.orientation = RecyclerView.HORIZONTAL
        binding.todayRecycler.layoutManager = llm
        binding.todayRecycler.adapter = adapter

        binding.todayRecycler.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> {
                        rv.parent.requestDisallowInterceptTouchEvent(true)
                        (this@FragmentToday.requireActivity() as MainActivity).binding.swipe.isEnabled = false
                        return false
                    }
                    else -> (this@FragmentToday.requireActivity() as MainActivity).binding.swipe.isEnabled = true
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        sharedViewModel.todayGeneralFlow.observe(viewLifecycleOwner, Observer {
            binding.currentTime.text = it.timestamp
            binding.dayTemp.text = it.dayTemp
            binding.nightTemp.text = it.nightTemp
            binding.currentTemp.text = it.currentTemp
            binding.currentFeelsLike.text = it.feelsTemp
            binding.icon.setImageResource(it.icon)
            binding.description.text = it.description
            binding.probabilityValue.text = it.probability

            binding.shimmerContainer.stopShimmerAnimation()
            binding.shimmerContainer.visibility = View.GONE
            binding.todayConstraint.visibility = View.VISIBLE
        })

        sharedViewModel.hoursDiagramTodayFlow.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()
        })

        lifecycleScope.launch {
            (requireActivity() as MainActivity).locationPermission.collect {
                if(it) {

                    binding.shimmerContainer.visibility = View.VISIBLE
                    binding.shimmerContainer.startShimmerAnimation()
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        binding.shimmerContainer.startShimmerAnimation()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerContainer.stopShimmerAnimation()
        super.onPause()
    }
}