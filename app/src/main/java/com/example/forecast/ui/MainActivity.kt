package com.example.forecast.ui

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.forecast.R.*
import com.example.forecast.databinding.ActivityMainBinding
import com.example.forecast.di.AppComponent
import com.example.forecast.di.ComponentStorage
import com.example.forecast.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var pagerAdapter: PagerAdapter
    lateinit var appComponent: AppComponent
    lateinit var sharedViewModel: SharedViewModel
    var todayTabColor = color.defaultBackground
    var tomorrowTabColor = color.defaultBackground
    var hoursTabColor = color.defaultBackground
    var label = ""
    var locationPermission = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent = (application as ComponentStorage).component
        sharedViewModel = ViewModelProvider(this, appComponent.getViewModelFactory()).get(SharedViewModel::class.java)

        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        binding.pager2.adapter = pagerAdapter

        val activityResultLauncher = registerForActivityResult(RequestMultiplePermissions()) { result ->
            if (result[Manifest.permission.ACCESS_FINE_LOCATION]!! && result[Manifest.permission.ACCESS_COARSE_LOCATION]!!) {
                lifecycleScope.launch {
                    locationPermission.value = true
                    binding.persistentSearchView.setQueryInputHint(getString(string.initial_hint))
                    sharedViewModel.initialStart()
                }
            }
        }
        activityResultLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))

        TabLayoutMediator(binding.tabs, binding.pager2, true) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = getString(string.today)
                1 -> tab.text = getString(string.tomorrow)
                2 -> tab.text = getString(string.hours)
            }
            tab.view.setOnLongClickListener { true }
        }.attach()

       binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
           override fun onTabSelected(tab: TabLayout.Tab?) {
               tab?.let { setColor(it.position) }
           }

           override fun onTabUnselected(tab: TabLayout.Tab?) {}

           override fun onTabReselected(tab: TabLayout.Tab?) {
               tab?.let { setColor(it.position) }
           }
       })

        binding.pager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                binding.swipe.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
            }
        })

        binding.swipe.setOnRefreshListener {
            lifecycleScope.launch {
                sharedViewModel.swipeRefresh(binding.persistentSearchView.inputQuery)
            }
        }

        sharedViewModel.todayGeneralFlow.observe(this, Observer {
            todayTabColor = it.color
            setColor(binding.tabs.selectedTabPosition)
            binding.swipe.isRefreshing = false
            binding.persistentSearchView.collapse(true)
        })
        sharedViewModel.tomorrowGeneralFlow.observe(this, Observer {
            tomorrowTabColor = it.color
            setColor(binding.tabs.selectedTabPosition)
        })
        sharedViewModel.correctCityName.observe(this, Observer {
            if (it == "error") {
                binding.persistentSearchView.inputQuery = label
            } else {
                binding.persistentSearchView.inputQuery = it
                label = it
            }
        })

        with(binding.persistentSearchView) {
            setOnSearchConfirmedListener { _, query ->
                lifecycleScope.launch {
                    sharedViewModel.enteredCityName.emit(query)
                }
            }
            collapse()
            setQueryTextTypeface(Typeface.DEFAULT)
            setSuggestionTextTypeface(Typeface.DEFAULT)

            setOnClearInputBtnClickListener {
                setQueryInputHint(getString(string.hint))

            }
            setOnSuggestionChangeListener(object : OnSuggestionChangeListener {

                override fun onSuggestionPicked(suggestion: SuggestionItem) {
                    lifecycleScope.launch {
                        sharedViewModel.enteredCityName.emit(suggestion.itemModel.text)
                    }
                }
                override fun onSuggestionRemoved(suggestion: SuggestionItem) {}
            })

            setOnSearchQueryChangeListener { _, oldQuery, newQuery ->

                if (oldQuery.isNotEmpty()) {
                    sharedViewModel.queryForSuggestions.value = newQuery
                    sharedViewModel.suggestionsFlow.observe(this@MainActivity, Observer {
                        binding.persistentSearchView.setSuggestions(it)
                    })
                }
            }
        }

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    private fun setColor(position: Int) {
        var color = 0
        when (position) {
            0 -> color = applicationContext.getColor(todayTabColor)
            1 -> color = applicationContext.getColor(tomorrowTabColor)
            2 -> color = applicationContext.getColor(hoursTabColor)
        }
        binding.tabs.setBackgroundColor(color)
        window.statusBarColor = color
        binding.linear.setBackgroundColor(color)
    }
}