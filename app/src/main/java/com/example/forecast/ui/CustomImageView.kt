package com.example.forecast.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CustomImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(context!!, attrs) {
    var left = 0.5
    var center = 0.6
    var right = 0.3
    var white = Paint()
    var path = Path()
    var shader: Shader? = null

    init {
        white.color = Color.WHITE
        white.textSize = 40f
        white.style = Paint.Style.FILL_AND_STROKE
        white.strokeWidth = 5f
        white.isDither = true
        white.isAntiAlias = true
        shader = LinearGradient(200f, 0f, 200f, 550f, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        white.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        val height = height.toFloat()
        val width = width.toFloat()
        val yStart = (height * left).toFloat()
        val yMiddle = (height * center).toFloat()
        val yStop = (height * right).toFloat()
        val xMiddle = width / 2
        path.reset()
        path.moveTo(0f, yStart)
        path.cubicTo(0f, yStart, xMiddle, yMiddle, width, yStop)
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()
        canvas.drawPath(path, white)
    }
}
