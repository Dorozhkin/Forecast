package com.example.forecast.ui.hours

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.databinding.FragmentHoursBinding
import com.example.forecast.databinding.HolderHoursBinding
import com.example.forecast.model.data_for_fragments.HourDetail

class AdapterHours : RecyclerView.Adapter<HolderHours>() {

    var data: List<HourDetail> = emptyList()
    var fragmentHoursBinding: FragmentHoursBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderHours {
        val binding = HolderHoursBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = HolderHours(binding)

        val listener = View.OnClickListener {
            val hour: HourDetail = data[holder.absoluteAdapterPosition]
            hour.isSelected = !hour.isSelected
            this.notifyItemChanged(holder.absoluteAdapterPosition)
        }

        holder.binding.alwaysVisiblePart.setOnClickListener(listener)
        return holder
    }

    override fun onBindViewHolder(holder: HolderHours, position: Int) {
        holder.setValues(data[position])
        var isExpanded = data[position].isSelected

        holder.binding.expandablePart.visibility = if (isExpanded) View.VISIBLE else View.GONE
        if (holder.binding.expandablePart.visibility == View.VISIBLE) {
            fragmentHoursBinding!!.recycler.smoothScrollToPosition(holder.absoluteAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}