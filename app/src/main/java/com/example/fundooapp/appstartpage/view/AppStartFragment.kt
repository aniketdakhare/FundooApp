package com.example.fundooapp.appstartpage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.appstartpage.viewmodel.AppStartViewModel
import com.example.fundooapp.appstartpage.viewmodel.AppStartViewModelFactory
import com.example.fundooapp.databinding.FragmentAppStartBinding
import com.example.fundooapp.fundoofirebaseauth.LoginService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class AppStartFragment : Fragment() {
    private lateinit var appStartViewModel: AppStartViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentAppStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_start, container, false)
        appStartViewModel = ViewModelProvider(
            this,
            AppStartViewModelFactory(UserService())
        ).get(AppStartViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(LoginService()))[SharedViewModel::class.java]
        binding.appStartViewModel = appStartViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appStartProgressbar.visibility = View.VISIBLE
    }

    private fun setUser() {
        appStartViewModel.checkUserExistence()
        appStartViewModel.isUserLoggedIn.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    sharedViewModel.setGoToHomePageStatus(true)
                    binding.appStartProgressbar.visibility = View.GONE
                }
                false -> {
                    sharedViewModel.setGoToLoginPageStatus(true)
                    binding.appStartProgressbar.visibility = View.GONE
                }
            }
        })
    }

    override fun onStart() {
        setUser()
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}