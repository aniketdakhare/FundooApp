package com.example.fundooapp.homepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentHomeBinding
import com.example.fundooapp.homepage.viewmodel.HomeViewModel
import com.example.fundooapp.homepage.viewmodel.HomeViewModelFactory
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.notesdisplay.view.NoteViewHolder
import com.example.fundooapp.util.NotesOperation.DELETE
import com.example.fundooapp.util.NotesOperation.UPDATE
import com.example.fundooapp.util.ViewType
import com.example.fundooapp.util.ViewType.*
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import kotlinx.android.synthetic.main.notes_display_layout.view.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewType: ViewType
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding
    private var notes: List<Note> = listOf()
    private lateinit var adapter: RecyclerView.Adapter<NoteViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(NotesService(DBHelper(requireContext())))
        ).get(HomeViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserService(), NotesService(
                    DBHelper(requireContext())
                )
            )
        )[SharedViewModel::class.java]
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this
        viewType = GRID
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getAllNotes()
        sharedViewModel.notes.observe(viewLifecycleOwner, {
            notes = it
        })

        adapter = object : RecyclerView.Adapter<NoteViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val displayView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.notes_display_layout, parent, false)
                return NoteViewHolder(displayView)
            }

            override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
                val note = notes[position]
                holder.bind(note)
                holder.view.setOnClickListener {
                    sharedViewModel.setNotesOperation(
                        Note(
                            note.tittle,
                            note.content,
                            note.noteId,
                            operation = UPDATE
                        )
                    )
                }

                holder.view.findViewById<ImageView>(R.id.menuIcon).setOnClickListener {
                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                        sharedViewModel.notesOperation(
                            Note(
                                noteId = note.noteId,
                                operation = DELETE
                            )
                        )
                        return@setOnMenuItemClickListener false
                    }
                    popupMenu.show()
                }
            }

            override fun getItemCount(): Int {
                return notes.size
            }
        }
        binding.notesList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.isNotesOperated.observe(viewLifecycleOwner, {
            if (it) displayNotes()
        })
        displayNotes()
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.notesDisplayType.observe(viewLifecycleOwner, Observer {
            this.viewType = it
            displayNotes()
        })
    }

    private fun displayNotes() {
        binding.notesList.layoutManager =
            StaggeredGridLayoutManager(viewType.flag, StaggeredGridLayoutManager.VERTICAL)
        binding.notesList.adapter = adapter
    }
}