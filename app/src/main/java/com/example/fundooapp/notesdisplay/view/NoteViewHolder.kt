package com.example.fundooapp.notesdisplay.view

import android.text.format.DateFormat.format
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Note
import java.util.*

class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val content: TextView = view.findViewById(R.id.content)
    private val tittle: TextView = view.findViewById(R.id.titles)

    fun bind(note: Note) {
        content.text = note.content

        if (note.reminderTime == 0L) tittle.text = note.tittle
        else {
            val time = Calendar.getInstance()
            time.timeInMillis = note.reminderTime
            tittle.text = "${note.tittle} \n\n${format("d MMM, HH:mm a", time)}"
            tittle.setCompoundDrawablesWithIntrinsicBounds(0, 0 , R.drawable.ic_baseline_access_alarm_24, 0)
        }
    }
}