package io.github.serpean.vikingship.util


class RandomPoint : Point {
    constructor(start: Int, end: Int) : super(
        (0..start).random().toFloat(),
        (0..end).random().toFloat()
    )

    private constructor(x: Float, y: Float) : super(x, y)
}