package com.example.fundooapp.homepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentHomeBinding
import com.example.fundooapp.homepage.model.HomeService
import com.example.fundooapp.homepage.viewmodel.HomeViewModel
import com.example.fundooapp.homepage.viewmodel.HomeViewModelFactory
import com.example.fundooapp.mainactivity.viewmodel.SharedViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(HomeService())).get(HomeViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        homeViewModel.logout()
        homeViewModel.logoutStatus.observe(viewLifecycleOwner, Observer {
          if (it == true) {
              sharedViewModel.setGoToLoginPageStatus(true)
              sharedViewModel.setGoToHomePageStatus(false)
          }
        })
    }
}