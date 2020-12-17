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
import com.example.fundooapp.notes.viewmodel.AddNoteViewModel
import com.example.fundooapp.notes.viewmodel.AddNoteViewModelFactory
import com.example.fundooapp.databinding.AddNotesFragmentBinding
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.NotesOperation.*
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class AddNoteFragment(private val note: Note, private val operation: NotesOperation) : Fragment() {

    private lateinit var addNoteViewModel: AddNoteViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: AddNotesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_notes_fragment, container, false)
        addNoteViewModel = ViewModelProvider(this, AddNoteViewModelFactory(NotesService(DBHelper(requireContext())))).get(AddNoteViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserService(), NotesService(
            DBHelper(requireContext())
        )
        ))[SharedViewModel::class.java]
        binding.addNotesViewModel = addNoteViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNoteViewModel.addNoteStatus.observe(viewLifecycleOwner, {
            sharedViewModel.setAddNoteStatus(it)
        })
        addNoteViewModel.updateNoteStatus.observe(viewLifecycleOwner, {
            sharedViewModel.setUpdateNoteStatus(it)
        })
    }

    private fun saveNotes() {
        binding.saveNotesFab.setOnClickListener{
            note.tittle = binding.editTextTittle.text.toString()
            note.content = binding.editTextNotes.text.toString()
            when (operation) {
                ADD -> addNoteViewModel.addNotes(note)
                UPDATE -> addNoteViewModel.updateNotes(note)
            }
            addNoteViewModel.isNotesOperated.observe(viewLifecycleOwner, {
                activity?.supportFragmentManager?.popBackStack()
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