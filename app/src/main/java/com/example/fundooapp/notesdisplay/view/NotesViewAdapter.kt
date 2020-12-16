package com.example.fundooapp.notesdisplay.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Note
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.viewmodel.SharedViewModel

class NotesViewAdapter(val notes: List<Note>, private val  sharedViewModel: SharedViewModel) : RecyclerView.Adapter<NoteViewHolder>(), Filterable {

    private val allNotes = mutableListOf<Note>().apply {
        addAll(notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val displayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_display_layout, parent, false)
        return NoteViewHolder(displayView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = allNotes[position]
        holder.bind(note)
        holder.view.setOnClickListener {
            sharedViewModel.setNoteToWrite(
                Pair(
                    Note(
                        note.tittle,
                        note.content,
                        note.noteId
                    ),
                    NotesOperation.UPDATE
                )
            )
        }

        holder.view.findViewById<ImageView>(R.id.menuIcon).setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                sharedViewModel.deleteNotes(note)
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Note>()

            if (constraint.toString().isEmpty())
                filteredList.addAll(notes)
            else {
                for (note in notes) {
                    if (note.tittle.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(note)
                    }
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            allNotes.clear()
            allNotes.addAll(results?.values as Collection<Note>)
            notifyDataSetChanged()
        }

    }

}
