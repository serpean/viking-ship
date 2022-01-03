package io.github.serpean.vikingship.canvas

import android.graphics.RectF

open class CanvasEntity(
    var centerX: Float,
    var centerY: Float,
    private var size: Float,
    val color: Int
) {
    private lateinit var innerRectF: RectF

    init {
        recalculateInnerRectF()
    }

    fun contains(x: Float, y: Float): Boolean {
        return innerRectF.contains(x, y)
    }

    fun intersect(shape: CanvasEntity): Boolean {
        return innerRectF.intersect(shape.toRectF())
    }

    fun toRectF(): RectF {
        return innerRectF
    }

    fun updatePos(x: Float, y: Float) {
        centerX = x
        centerY = y
        recalculateInnerRectF()
    }

    fun translatePos(x: Float, y: Float) {
        centerX += x
        centerY += y
        recalculateInnerRectF()
    }

    private fun recalculateInnerRectF() {
        innerRectF =
            RectF(
                centerX - size / 2,
                centerY - size / 2,
                centerX + size / 2,
                centerY + size / 2
            )
    }

}
