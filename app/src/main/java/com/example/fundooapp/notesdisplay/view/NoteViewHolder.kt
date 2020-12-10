package com.example.fundooapp.notesdisplay.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Note

class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val content: TextView = view.findViewById(R.id.content)
    private val tittle: TextView = view.findViewById(R.id.titles)

    fun bind(note: Note) {
        content.text = note.content
        tittle.text = note.tittle
    }
}