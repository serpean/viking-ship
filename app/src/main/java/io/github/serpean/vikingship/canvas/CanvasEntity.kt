package io.github.serpean.vikingship.canvas

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

// Canvas entity implemented as Square
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

    fun draw(canvas: Canvas?, texture: Bitmap) {
        canvas?.drawBitmap(texture, null, toRectF(), null)
    }

    fun draw(canvas: Canvas?, paint: Paint) {
        paint.color = color
        canvas?.drawRect(toRectF(), paint)
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

    private fun toRectF(): RectF {
        return innerRectF
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
