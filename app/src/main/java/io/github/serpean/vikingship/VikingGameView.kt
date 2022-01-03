package io.github.serpean.vikingship

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import io.github.serpean.vikingship.canvas.Rock
import io.github.serpean.vikingship.util.Point

class VikingGameView : View {

    private val points: MutableMap<Int, Point> = mutableMapOf()
    private val paint: Paint = Paint()
    private val playerPaint: Paint = Paint()
    private var game: Game? = null

    init {
        playerPaint.isAntiAlias = true
        playerPaint.strokeWidth = 6f
        playerPaint.color = Color.BLUE
        playerPaint.style = Paint.Style.STROKE
        playerPaint.strokeJoin = Paint.Join.ROUND
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {

        for (path: Point in points.values) {
            for (point: Point in points.values) {
                canvas?.drawCircle(point.x, point.y, 100f, playerPaint)
            }
        }

        if (game == null) {
            game = Game(this.measuredWidth, this.measuredHeight)
        }
        if (game!!.isWinner()) {
            Toast.makeText(context, "Winner!", Toast.LENGTH_SHORT).show()
            game = null
            return
        }
        if (game!!.isLooser()) {
            Toast.makeText(context, "Looser!", Toast.LENGTH_SHORT).show()
            game = null
            return
        }
        if (game != null) {
            for (rock: Rock in game!!.rocks) {
                paint.color = rock.color
                canvas?.drawRect(rock.toRectF(), paint)
            }
            paint.color = game!!.endPoint.color
            canvas?.drawRect(game!!.endPoint.toRectF(), paint)
            paint.color = game!!.ship.color
            canvas?.drawRect(game!!.ship.toRectF(), paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val index = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN -> {
                val point = Point(event.getX(index), event.getY(index));
                Log.i("down id", event.getPointerId(index).toString());
                points[event.getPointerId(index)] = point;
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                val currentPointId = event.getPointerId(index)
                Log.i("up id", currentPointId.toString());
                points.remove(currentPointId);
            }
            MotionEvent.ACTION_MOVE -> {
                for (i: Int in points.keys) {
                    val pointerIndex: Int = event.findPointerIndex(i);
                    val currentX = event.getX(pointerIndex)
                    val currentY = event.getY(pointerIndex)
                    points[i] = Point(currentX, currentY)
                    val speed = if (points.size > 3) 1F else 0F
                    game?.moveShip(currentX, currentY, speed)
                }
            }
        }
        invalidate()
        return true
    }
}
