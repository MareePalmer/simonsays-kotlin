package com.seng440.mpa588.mareementions

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.FileNotFoundException

class MainGameActivity : Activity() {

    private val PREFS_FILENAME = "com.seng440.mpa588.mareementions.prefs"
    private val PREF_HAS_SOUND = "has_sound"
    private val PREF_FLASH_SPEED = "flash_speed"
    var prefs: SharedPreferences? = null
    private var expectedPattern: Pattern = Pattern(mutableListOf())
    private var current: Int = 0
    private lateinit var highScoreDialog: AlertDialog
    private lateinit var pauseDialog: AlertDialog
    private lateinit var buttonRed: Button
    private lateinit var buttonBlue: Button
    private lateinit var buttonGreen: Button
    private lateinit var buttonYellow: Button
    private lateinit var pauseMenuButton: FloatingActionButton
    private lateinit var handler: Handler
    private var isPaused: Boolean = false
    private var patternShown: Boolean = false
    private var hasSound = false
    private var buttonSpeed: Long = 1000
    private var toner = ToneGenerator(AudioManager.STREAM_ALARM, 50)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)

        buttonRed = findViewById(R.id.button_red)
        buttonBlue = findViewById(R.id.button_blue)
        buttonGreen = findViewById(R.id.button_green)
        buttonYellow = findViewById(R.id.button_yellow)
        pauseMenuButton = findViewById(R.id.button_pause)

        handler = Handler()

        highScoreDialog = makeHighScoreDialog()
        pauseDialog = makePauseDialog()

        buttonRed.setOnClickListener {
            checkIfFinished(buttonRed.id)
        }

        buttonBlue.setOnClickListener {
            checkIfFinished(buttonBlue.id)
        }

        buttonGreen.setOnClickListener {
            checkIfFinished(buttonGreen.id)
        }

        buttonYellow.setOnClickListener {
            checkIfFinished(buttonYellow.id)
        }

        pauseMenuButton.setOnClickListener {
            isPaused = true
            pauseDialog.show()
        }

        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        hasSound = prefs!!.getBoolean(PREF_HAS_SOUND, false)
        buttonSpeed = prefs!!.getLong(PREF_FLASH_SPEED, 1000)
        expectedPattern.addButtonToPattern()

        showPattern()
    }

    private fun makeHighScoreDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        val form = layoutInflater.inflate(R.layout.dialog_enter_name, null, false)
        builder.setView(form)
        val enterName: EditText = form.findViewById(R.id.playerNameEditText)

        builder.setPositiveButton(R.string.save_high_score) { _, _ ->
            val number = if (expectedPattern.buttonOrder.size > 0) expectedPattern.buttonOrder.size - 2 else expectedPattern.buttonOrder.size
            val score = HighScore(enterName.text.toString(), number)
            try {
                saveHighScore(score)

                val playAgainDialog = makePlayAgainDialog()
                highScoreDialog.cancel()
                playAgainDialog.show()

            } catch (e: FileNotFoundException) {
                Toast.makeText(this, R.string.high_scores_not_found, Toast.LENGTH_LONG).show()
            }
        }
        builder.setCancelable(false)
        highScoreDialog = builder.create()
        return highScoreDialog
    }

    private fun resetGame() {
        expectedPattern = Pattern(mutableListOf())
        current = 0
        expectedPattern.addButtonToPattern()
        patternShown = false
        handler.postDelayed({
            showPattern()
        }, buttonSpeed)
    }


    private fun makePauseDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        val form = layoutInflater.inflate(R.layout.dialog_game_menu, null, false)
        builder.setView(form)
        builder.setTitle(R.string.pause_string)
        val resumeButton: Button = form.findViewById(R.id.button_resume)
        val exitButton: Button = form.findViewById(R.id.button_exit)

        resumeButton.setOnClickListener {
            isPaused = false
            pauseDialog.cancel()
            handler.postDelayed({
                showPattern()
            }, buttonSpeed)
        }
        exitButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        return builder.create()
    }

    private fun showPattern() {
        if(!isPaused && !patternShown) {
            disableButtons()
            val buttonId = expectedPattern.buttonOrder[current]
            var buttonToAnimate: Button = buttonRed
            when (buttonId) {
                buttonRed.id -> buttonToAnimate = buttonRed
                buttonBlue.id -> buttonToAnimate = buttonBlue
                buttonGreen.id -> buttonToAnimate = buttonGreen
                buttonYellow.id -> buttonToAnimate = buttonYellow
            }
            handler.post {
                changeButtonBackground(buttonToAnimate, getColorToChangeTo(buttonToAnimate, false))
                if(hasSound) {
                    toner.startTone(getToneToChangeTo(buttonToAnimate), 1000)
                }
                handler.postDelayed({
                    changeButtonBackground(buttonToAnimate, getColorToChangeTo(buttonToAnimate, true))
                    current += 1
                    if (current == 1) {
                        current += 1
                    }
                    if (current < expectedPattern.buttonOrder.size) {
                        showPattern()
                    } else {
                        current = 0
                        enableButtons()
                        patternShown = true
                    }
                }, buttonSpeed)
            }
        }
    }

    private fun getToneToChangeTo(button: Button): Int {
        when(button.id) {
            buttonRed.id -> return ToneGenerator.TONE_DTMF_1
            buttonBlue.id -> return ToneGenerator.TONE_DTMF_3
            buttonGreen.id -> return ToneGenerator.TONE_DTMF_5
        }
        return ToneGenerator.TONE_DTMF_7
    }

    private fun getColorToChangeTo(button: Button, turnBack: Boolean) : Int {
        var color = 0x00000000

        when(button.id) {
            buttonRed.id -> {
                color = if(turnBack) {
                    R.drawable.button_red
                } else {
                    R.drawable.button_red_lit
                }
            }
            buttonBlue.id -> {
                color = if(turnBack) {
                    R.drawable.button_blue
                } else {
                    R.drawable.button_blue_lit
                }
            }
            buttonGreen.id -> {
                color = if(turnBack) {
                    R.drawable.button_green
                } else {
                    R.drawable.button_green_lit
                }
            }
            buttonYellow.id -> {
                color = if(turnBack) {
                    R.drawable.button_yellow
                } else {
                    R.drawable.button_yellow_lit
                }
            }
            else -> Toast.makeText(this, R.string.button_not_found_somehow, Toast.LENGTH_SHORT).show()
        }
        return color
    }

    private fun changeButtonBackground(button: Button, color:Int) {
        button.setBackgroundResource(color)
    }

    private fun checkIfFinished(buttonId: Int) {
        if (expectedPattern.checkSameButtonPress(buttonId, current)) {
            if (current == (expectedPattern.buttonOrder.size - 1)) {
                current = 0
                nextStage()
            } else {
                current += 1
                if(current == 1) {
                    current += 1
                }
            }
        } else {
            highScoreDialog.show()
        }
    }


    private fun nextStage() {
        Toast.makeText(this, R.string.next_stage_message,Toast.LENGTH_SHORT).show()
        expectedPattern.addButtonToPattern()
        if(expectedPattern.buttonOrder.size == 2) {
            expectedPattern.addButtonToPattern()
        }
        handler.postDelayed({
            patternShown = false
            showPattern()
        }, buttonSpeed)
    }

    private fun makePlayAgainDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.play_again)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            resetGame()
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        return builder.create()
    }


    private fun saveHighScore(score: HighScore) {
        HighScoreSaver(this).execute(score)
        Toast.makeText(this, R.string.high_score_saved, Toast.LENGTH_SHORT).show()
    }

    private fun disableButtons() {
        buttonRed.isEnabled = false
        buttonBlue.isEnabled = false
        buttonGreen.isEnabled = false
        buttonYellow.isEnabled = false
    }

    private fun enableButtons() {
        buttonRed.isEnabled = true
        buttonBlue.isEnabled = true
        buttonGreen.isEnabled = true
        buttonYellow.isEnabled = true
    }

}
