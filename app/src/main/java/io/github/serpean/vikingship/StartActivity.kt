package io.github.serpean.vikingship

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.github.serpean.vikingship.Game.*


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        hideUI()

        val startButton = findViewById<Button>(R.id.start_button)
        val levelRadioGroup = findViewById<RadioGroup>(R.id.levelRadioGroup)

        startButton.setOnClickListener {
            val mainActivity = Intent(this@StartActivity, MainActivity::class.java)
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            val level = when (levelRadioGroup.checkedRadioButtonId) {
                R.id.easyLevelRadio -> Level.EASY
                R.id.normalLevelRadio -> Level.NORMAL
                R.id.hardLevelRadio -> Level.HARD
                else -> Level.EASY
            }
            mainActivity.putExtra("level", level);
            startActivity(mainActivity)
        }
    }

    private fun hideUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}