package com.seng440.mpa588.mareementions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView

class MainMenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AnimationUtils.loadAnimation(this, R.anim.spin_button_animation).also { buttonSpin ->
            findViewById<ImageView>(R.id.imageView).startAnimation(buttonSpin)
        }
    }

    fun playGame(view: View) {
        val intent = Intent(this, MainGameActivity::class.java)
        startActivity(intent)
    }

    fun openHighScores(view: View) {
        val intent = Intent(this, HighScoreActivity::class.java)
        startActivity(intent)
    }

    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
