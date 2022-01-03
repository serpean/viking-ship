package io.github.serpean.vikingship

import android.graphics.Color
import io.github.serpean.vikingship.canvas.CanvasEntity
import io.github.serpean.vikingship.canvas.Rock
import io.github.serpean.vikingship.canvas.Ship

class Game(private val xLength: Int, private val yLength: Int) {

    var ship = Ship(
        (0..xLength).random().toFloat(),
        (0..yLength).random().toFloat()
    ) // startpoint
    val endPoint = CanvasEntity(
        (0..xLength).random().toFloat(),
        (0..yLength).random().toFloat(),
        100F,
        Color.BLUE
    )
    var rocks = mutableListOf<Rock>()

    init {
        for (i in 0..50) {
            val posX = (0..xLength).random().toFloat()
            val posY = (0..yLength).random().toFloat()
            val size = (20..100).random().toFloat()
            rocks.add(Rock(posX, posY, size))
        }
    }

    fun moveShip(actualX: Float, actualY: Float, speed: Float) {
        if (actualX > ship.centerX && actualY > ship.centerY) {
            ship.translatePos(speed, speed)
        } else if (actualX < ship.centerX && actualY > ship.centerY) {
            ship.translatePos(-speed, speed)
        } else if (actualX < ship.centerX && actualY < ship.centerY) {
            ship.translatePos(-speed, -speed)
        } else if (actualX > ship.centerX && actualY < ship.centerY) {
            ship.translatePos(speed, -speed)
        }
    }

    fun isWinner(): Boolean {
        return endPoint.intersect(ship)
    }

    fun isLooser(): Boolean {
        return rocks.any { it.intersect(ship) }
    }
}
