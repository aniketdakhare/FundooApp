package com.example.fundooapp.homepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentHomeBinding
import com.example.fundooapp.homepage.viewmodel.HomeViewModel
import com.example.fundooapp.homepage.viewmodel.HomeViewModelFactory
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.notesdisplay.view.NotesViewAdapter
import com.example.fundooapp.util.NotesOperation.UPDATE
import com.example.fundooapp.util.ViewState.Success
import com.example.fundooapp.util.ViewType
import com.example.fundooapp.util.ViewType.GRID
import com.example.fundooapp.viewmodel.NotesSharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewType: ViewType
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var notesSharedViewModel: NotesSharedViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NotesViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(NotesService(DBHelper(requireContext())))
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(UserService())
        )[SharedViewModel::class.java]
        notesSharedViewModel =
            ViewModelProvider(requireActivity())[NotesSharedViewModel::class.java]
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this
        viewType = GRID
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.notesViewState.observe(viewLifecycleOwner, {
            if (it is Success) {
                adapter = NotesViewAdapter(it.data, { note ->
                    sharedViewModel.setNoteToWrite(Pair(note, UPDATE))
                }, { note ->
                    homeViewModel.deleteNotes(note)
                })
                sharedViewModel.queryText.observe(viewLifecycleOwner, { text ->
                    adapter.filter.filter(text)
                })
                binding.notesList.adapter = adapter
                displayNotes()
            }
        })
        sharedViewModel.addNoteStatus.observe(viewLifecycleOwner, {
            if (it is Success)
                homeViewModel.addNotes(it.data)
        })
        sharedViewModel.updateNoteStatus.observe(viewLifecycleOwner, {
            if (it is Success)
                homeViewModel.updateNotes(it.data)
        })
        notesSharedViewModel.notesDisplayType.observe(viewLifecycleOwner, {
            homeViewModel.displayNotesAsPerType(it)
        })
        sharedViewModel.notesDisplayType.observe(viewLifecycleOwner, {
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