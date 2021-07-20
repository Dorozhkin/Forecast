package com.example.forecast.ui.today_tomorrow

import android.graphics.Canvas
import android.util.DisplayMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.databinding.HolderTodayTomorrowBinding
import com.example.forecast.model.data_for_fragments.HourDiagram



class TodayTomorrowHolder(val binding: HolderTodayTomorrowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(hour: HourDiagram) {
        val displayMetrics = binding.line.context.resources.displayMetrics

        val height = (200 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        val topMargin = (height*hour.tempStampMargin).toInt()

        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, topMargin, 0, 0)
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID

        binding.temp.layoutParams = params
        binding.line.left = hour.leftMargin
        binding.line.right = hour.rightMargin
        binding.line.center = hour.centerMargin

        binding.line.draw(Canvas())

        binding.temp.text = hour.temp
        binding.hour.text = hour.timestamp
    }
}