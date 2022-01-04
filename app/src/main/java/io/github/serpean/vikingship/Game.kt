package io.github.serpean.vikingship

import io.github.serpean.vikingship.canvas.Island
import io.github.serpean.vikingship.canvas.Rock
import io.github.serpean.vikingship.canvas.Ship

class Game(private val xLength: Int, private val yLength: Int, private val level : Level) {

    private val margin = 100
    var ship = Ship(
        (0..(xLength - margin)).random().toFloat(),
        (0..(yLength - margin)).random().toFloat()
    ) // start point
    val island = Island(
        (0..xLength).random().toFloat(),
        (0..yLength).random().toFloat()
    )
    var rocks = mutableListOf<Rock>()

    init {
        for (i in 0..level.rocks) {
            val posX = (0..xLength).random().toFloat()
            val posY = (0..yLength).random().toFloat()
            val size = (20..100).random().toFloat()
            if(!ship.contains(posX, posY)) { // avoid loose game from start
                rocks.add(Rock(posX, posY, size))
            }
        }
    }

    constructor(xLength: Int, yLength: Int) : this(xLength, yLength, Level.NORMAL)

    fun moveShip(actualX: Float, actualY: Float, speed: Float) {
        if (ship.centerX > margin && ship.centerY < yLength - margin &&
            actualX > ship.centerX && actualY < ship.centerY
        ) {
            ship.translatePos(-speed, speed)
        } else if (ship.centerX < xLength - margin && ship.centerY < yLength - margin &&
            actualX < ship.centerX && actualY < ship.centerY
        ) {
            ship.translatePos(speed, speed)
        } else if (ship.centerX < xLength - margin && ship.centerY > margin &&
            actualX < ship.centerX && actualY > ship.centerY
        ) {
            ship.translatePos(speed, -speed)
        } else if (ship.centerX > margin && ship.centerY > margin &&
            actualX > ship.centerX && actualY > ship.centerY
        ) {
            ship.translatePos(-speed, -speed)
        }
    }

    fun isWinner(): Boolean {
        return island.intersect(ship)
    }

    fun isLooser(): Boolean {
        return rocks.any { it.intersect(ship) }
    }

    enum class Level(val rocks: Int) {
        EASY(10),
        NORMAL(25),
        HARD(50)
    }
}
