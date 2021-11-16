package com.seng440.mpa588.mareementions

import android.app.Activity
import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import android.content.Intent
import android.provider.ContactsContract





class HighScoreActivity : Activity() {

    private var highScores: ArrayList<HighScore> = ArrayList()

    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadHighScoresList()
        setContentView(R.layout.activity_high_score)
        var listView: RecyclerView = findViewById(android.R.id.list)
        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager
        listView.adapter = HighScoreAdapter(this, highScores.sortedBy { highScore: HighScore -> highScore.score }.reversed()) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.send_message_message)
            builder.setPositiveButton(R.string.yes) { _, _ ->
                Toast.makeText(this, R.string.through_to_messages, Toast.LENGTH_SHORT).show()
                val sendIntent = Intent(Intent.ACTION_VIEW)
                sendIntent.putExtra("sms_body", "I just got a high score of ${it.score} in MareeMentions!")
                sendIntent.type = "vnd.android-dir/mms-sms"
                startActivity(sendIntent)
            }
            builder.setNegativeButton(R.string.no) { _, _->
            }
            builder.show()
        }
    }

    fun loadHighScoresList() {
        try {
            val file = openFileInput("highscores.json")
            highScores = HighScore.loadHighScoresList(file)
            file.close()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, R.string.high_scores_not_found, Toast.LENGTH_SHORT).show()
        }
    }


}
