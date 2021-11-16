package com.seng440.mpa588.mareementions

import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class HighScore (var name: String, var score: Int){

    override fun toString(): String {
        return this.name + ": " + this.score
    }

    fun write(writer: JsonWriter) {
        writer.beginObject()
        writer.name("name").value(name)
        writer.name("score").value(score)
        writer.endObject()
    }

    companion object {
        fun read(reader: JsonReader): HighScore {
            val highScore = HighScore("", -1)

            while (reader.hasNext()) {
                reader.beginObject()
                val key = reader.nextString()
                when (key) {
                    "name" -> highScore.name = reader.nextString()
                    "score" -> highScore.score = reader.nextInt()
                }
                reader.endObject()
            }

            return highScore
        }

        private val gson: Gson = Gson()

        fun loadHighScoresList(file : FileInputStream) : ArrayList<HighScore> {
            try {
                val reader = JsonReader(InputStreamReader(file))
                reader.beginObject()
                val key = reader.nextName()
                var s = ""
                when (key) {
                    "high" -> s = reader.nextString()
                }
                val highScores : ArrayList<HighScore> = gson.fromJson(s, object : TypeToken<ArrayList<HighScore>>() {
                }.type)

                reader.endObject()
                reader.close()
                return highScores
            } catch (e: FileNotFoundException) {
                return ArrayList<HighScore>()
            }
        }

        fun writeHighScoresToFile(file: OutputStream, highScores: ArrayList<HighScore>) {
            val jsonWriter = JsonWriter(OutputStreamWriter(file))
            jsonWriter.setIndent("  ")
            jsonWriter.beginObject()
            val j = gson.toJson(highScores)
            jsonWriter.name("high").value(j)
            jsonWriter.endObject()
            jsonWriter.close()
        }


    }

}