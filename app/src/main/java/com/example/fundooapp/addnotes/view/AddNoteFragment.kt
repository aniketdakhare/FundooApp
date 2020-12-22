package com.example.fundooapp.addnotes.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.addnotes.viewmodel.AddNoteViewModel
import com.example.fundooapp.addnotes.viewmodel.AddNoteViewModelFactory
import com.example.fundooapp.databinding.AddNotesFragmentBinding
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.remindersettings.view.SetReminderFragment
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.NotesOperation.ADD
import com.example.fundooapp.util.NotesOperation.UPDATE
import com.example.fundooapp.util.ViewState.Success
import com.example.fundooapp.viewmodel.NotesSharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import kotlinx.android.synthetic.main.main_content_layout.*

class AddNoteFragment(private val note: Note, private val operation: NotesOperation) : Fragment() {

    private lateinit var addNoteViewModel: AddNoteViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var notesSharedViewModel: NotesSharedViewModel
    private lateinit var binding: AddNotesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_notes_fragment, container, false)
        addNoteViewModel = ViewModelProvider(
            this,
            AddNoteViewModelFactory(NotesService(DBHelper(requireContext())))
        ).get(AddNoteViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserService(), NotesService(
                    DBHelper(requireContext())
                )
            )
        )[SharedViewModel::class.java]
        notesSharedViewModel = ViewModelProvider(requireActivity())[NotesSharedViewModel::class.java]
        sharedViewModel.setNoteToWrite(null)
        sharedViewModel.setAddNoteFab(false)
        binding.addNotesViewModel = addNoteViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNoteViewModel.addNoteStatus.observe(viewLifecycleOwner, {
            if (it is Success) {
                sharedViewModel.setAddNoteStatus(it)
                Log.e(TAG, "onViewCreated: add:  ${activity?.supportFragmentManager?.backStackEntryCount}")
                activity?.supportFragmentManager?.popBackStack()
                Log.e(TAG, "onViewCreated: add:  ${activity?.supportFragmentManager?.backStackEntryCount}")

            }
        })
        addNoteViewModel.updateNoteStatus.observe(viewLifecycleOwner, {
            if (it is Success) {
                Log.e(TAG, "onViewCreated: Update: Success")
                sharedViewModel.setUpdateNoteStatus(it)
                Log.e(TAG, "onViewCreated: Update:  ${activity?.supportFragmentManager?.backStackEntryCount}")
                activity?.supportFragmentManager?.popBackStack()
                Log.e(TAG, "onViewCreated: Update:  ${activity?.supportFragmentManager?.backStackEntryCount}")

            }
        })
        notesSharedViewModel.reminderTime.observe(viewLifecycleOwner, {
            note.reminderTime = it.timeInMillis
        })
    }

    private fun saveNotes() {
        binding.saveNotesFab.setOnClickListener {
            when (operation) {
                ADD -> addNoteViewModel.addNotes(note)
                UPDATE -> addNoteViewModel.updateNotes(note)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextTittle.setText(note.tittle)
        binding.editTextNotes.setText(note.content)
        binding.editTextTittle.doOnTextChanged { text, _, _, _ ->
            note.tittle = text.toString()
        }
        binding.editTextNotes.doOnTextChanged { text, _, _, _ ->
            note.content = text.toString()
        }
        saveNotes()
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.setAddNoteFab(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_toolbar_menu, menu)
        addNoteToolbarMenu()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addReminder) {
            SetReminderFragment(note).show((requireActivity() as AppCompatActivity).supportFragmentManager, "Reminder")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNoteToolbarMenu() {
        (activity as AppCompatActivity).supportActionBar?.title = ""

        (activity as AppCompatActivity).toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val TAG = "AddNoteFragment"
    }
}