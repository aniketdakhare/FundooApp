package com.example.fundooapp.notes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.notes.viewmodel.NotesViewModel
import com.example.fundooapp.notes.viewmodel.NotesViewModelFactory
import com.example.fundooapp.databinding.AddNotesFragmentBinding
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class AddNotesFragment(val note: Note) : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: AddNotesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_notes_fragment, container, false)
        notesViewModel = ViewModelProvider(this, NotesViewModelFactory(NotesService())).get(NotesViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserService(), NotesService()))[SharedViewModel::class.java]
        binding.addNotesViewModel = notesViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun saveNotes() {
        binding.saveNotesFab.setOnClickListener{
            val tittle = binding.editTextTittle.text.toString()
            val notes = binding.editTextNotes.text.toString()
            sharedViewModel.notesOperation(Note(tittle, notes, note.noteId, operation = note.operation))
            sharedViewModel.isNotesOperated.observe(viewLifecycleOwner, {
                sharedViewModel.setGoToHomePageStatus(true)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextTittle.setText(note.tittle)
        binding.editTextNotes.setText(note.content)
        saveNotes()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}