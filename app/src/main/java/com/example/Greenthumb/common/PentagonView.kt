package com.example.greenthumb.common


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.gardening.R


class PentagonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path: Path = Path()

    init {
        paint.color = ContextCompat.getColor(context, R.color.teal_200)
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val centerX = width / 2
        val centerY = height / 2

        val sideLength = Math.min(width, height) * 0.8f
        val radius = sideLength / (2 * Math.sin(Math.PI / 5).toFloat())

        path.reset()
        path.moveTo(
            centerX + radius * Math.cos((Math.PI / 2).toDouble()).toFloat(),
            centerY - radius * Math.sin((Math.PI / 2).toDouble()).toFloat()
        )

        for (i in 1..5) {
            val angle = (Math.PI / 2 + 2 * Math.PI / 5 * i).toFloat()
            path.lineTo(
                centerX + radius * Math.cos(angle.toDouble()).toFloat(),
                centerY - radius * Math.sin(angle.toDouble()).toFloat()
            )
        }

        canvas.drawPath(path, paint)
    }
}
