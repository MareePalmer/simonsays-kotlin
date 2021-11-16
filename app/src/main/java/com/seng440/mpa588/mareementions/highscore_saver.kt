package com.seng440.mpa588.mareementions

import android.content.Context
import android.os.AsyncTask

class HighScoreSaver(val activity: MainGameActivity) : AsyncTask<HighScore, Void, Unit>() {
    override fun doInBackground(vararg score: HighScore?) {
        val fileInputStream = activity.openFileInput("highscores.json")
        var highScores: ArrayList<HighScore> = HighScore.loadHighScoresList(fileInputStream)
        fileInputStream.close()
        val fileOutputStream = activity.openFileOutput("highscores.json", Context.MODE_PRIVATE)
        highScores.add(score[0]!!)
        HighScore.writeHighScoresToFile(fileOutputStream, highScores)
        fileOutputStream.close()
    }

}