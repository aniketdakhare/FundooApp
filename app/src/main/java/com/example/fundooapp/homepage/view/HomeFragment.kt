package com.example.fundooapp.homepage.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.example.fundooapp.notesdisplay.view.NotesViewAdapter
import com.example.fundooapp.util.ViewType
import com.example.fundooapp.util.ViewType.*
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewType: ViewType
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NotesViewAdapter

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
            val notes = it
            adapter = NotesViewAdapter(notes, sharedViewModel)
            sharedViewModel.setNotesAdapter(adapter)
            binding.notesList.adapter = adapter
            displayNotes()
        })
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.isNoteDeleted.observe(viewLifecycleOwner, {
            if (it) {
                sharedViewModel.getAllNotes()
            }
        })
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