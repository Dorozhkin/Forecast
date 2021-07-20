package com.example.forecast.ui.today_tomorrow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.databinding.HolderTodayTomorrowBinding
import com.example.forecast.model.data_for_fragments.HourDiagram

class TodayTomorrowAdapter: RecyclerView.Adapter<TodayTomorrowHolder>() {

    var data: List<HourDiagram> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTomorrowHolder {
        val binding = HolderTodayTomorrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayTomorrowHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayTomorrowHolder, position: Int) {
        holder.setValues(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}