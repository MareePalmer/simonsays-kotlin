package com.seng440.mpa588.mareementions

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton

class SettingsActivity : Activity() {

    private val PREFS_FILENAME = "com.seng440.mpa588.mareementions.prefs"
    private val PREF_HAS_SOUND = "has_sound"
    private val PREF_FLASH_SPEED = "flash_speed"
    var prefs: SharedPreferences? = null
    private lateinit var toggleButton: ToggleButton
    private lateinit var fastSpeedSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toggleButton = findViewById(R.id.toggleSoundButton)
        fastSpeedSwitch = findViewById(R.id.speedSwitch)
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        toggleButton.isChecked = prefs!!.getBoolean(PREF_HAS_SOUND, false)
        val speed = prefs!!.getLong(PREF_FLASH_SPEED, 1000)
        fastSpeedSwitch.isChecked = speed == 500L
    }

    fun saveSettingsAndClose(view: View) {
        //save settings to prefs
        val editor = prefs!!.edit()
        editor.putBoolean(PREF_HAS_SOUND, toggleButton.isChecked)
        if(fastSpeedSwitch.isChecked) {
            editor.putLong(PREF_FLASH_SPEED, 500)
        } else {
            editor.putLong(PREF_FLASH_SPEED, 1000)
        }
        editor.apply()
        Toast.makeText(this, R.string.settings_saved_toast, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}
