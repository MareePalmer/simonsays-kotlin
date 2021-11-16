package com.seng440.mpa588.mareementions

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class HighScoreAdapter(private val context: Context,
                       private val highscores: List<HighScore>,
                       private val clickListener: (HighScore) -> Unit): RecyclerView.Adapter<HighScoreViewHolder>() {

    private var selectedIndex = RecyclerView.NO_POSITION

    override fun getItemCount(): Int = highscores.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HighScoreViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_row, parent, false)
        val holder = HighScoreViewHolder(view)

        view.setOnClickListener {
            clickListener(highscores[holder.adapterPosition])

            val oldSelectedIndex = selectedIndex
            selectedIndex = holder.adapterPosition
            notifyItemChanged(oldSelectedIndex)
            notifyItemChanged(selectedIndex)
        }

        return holder
    }

    override fun onBindViewHolder(holder: HighScoreViewHolder, i: Int) {
        holder.headlineText.text = "${i + 1}. ${highscores[i]}"
    }
}

class HighScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val headlineText: TextView = view.findViewById(R.id.highScoreText)
}