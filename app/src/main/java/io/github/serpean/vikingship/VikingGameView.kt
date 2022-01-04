package io.github.serpean.vikingship

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import io.github.serpean.vikingship.canvas.Rock
import io.github.serpean.vikingship.canvas.Wind


class VikingGameView : View {

    private val startActivity = Intent(context, StartActivity::class.java)

    private val windPlayers: MutableMap<Int, Wind> = mutableMapOf()
    private var game: Game
    // Texture create here to avoid resources resolution issues
    private val vikingShip = BitmapFactory.decodeResource(resources, R.drawable.viking_ship)
    private val island = BitmapFactory.decodeResource(resources, R.drawable.island)
    private val rocks: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.rocks)
    private val wind = BitmapFactory.decodeResource(resources, R.drawable.wind)

    init {
        startActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        val windowsManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var width: Int
        var height: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = windowsManager.currentWindowMetrics.bounds.right
            height = windowsManager.currentWindowMetrics.bounds.bottom
        } else {
            width = windowsManager.defaultDisplay.width
            height = windowsManager.defaultDisplay.height
        }
        val level = (context as MainActivity).intent.extras?.get("level") as Game.Level
        game = Game(width, height, level)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        if (game.isIsland()) {
            Toast.makeText(context, "Land!", Toast.LENGTH_SHORT).show()
            context.startActivity(startActivity)
            return
        }
        if (game.isLooser()) {
            Toast.makeText(context, "Looser!", Toast.LENGTH_SHORT).show()
            context.startActivity(startActivity)
            return
        }
        game.draw(canvas, rocks, island, vikingShip)
        for (path: Wind in windPlayers.values) {
            for (windPlayer: Wind in windPlayers.values) {
                windPlayer.draw(canvas, wind)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val index = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN -> {
                val newWindPlayer = Wind(event.getX(index), event.getY(index));
                Log.i("down id", event.getPointerId(index).toString());
                windPlayers[event.getPointerId(index)] = newWindPlayer;
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                val currentWindPlayerId = event.getPointerId(index)
                Log.i("up id", currentWindPlayerId.toString());
                windPlayers.remove(currentWindPlayerId);
            }
            MotionEvent.ACTION_MOVE -> {
                for (i: Int in windPlayers.keys) {
                    val pointerIndex: Int = event.findPointerIndex(i);
                    val currentX = event.getX(pointerIndex)
                    val currentY = event.getY(pointerIndex)
                    windPlayers[i] = Wind(currentX, currentY)
                    val speed =
                        if (windPlayers.size > 3) ((100..200).random() / 100).toFloat() else 0F
                    game.moveShip(currentX, currentY, speed)
                }
            }
        }
        invalidate()
        return true
    }
}
