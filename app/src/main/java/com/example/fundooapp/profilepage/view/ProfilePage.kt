package com.example.fundooapp.profilepage.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentProfilePageBinding
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.model.UserService
import com.example.fundooapp.profilepage.viewmodel.ProfileViewModel
import com.example.fundooapp.profilepage.viewmodel.ProfileViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class ProfilePage : DialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_page, container, false)
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(UserService())).get(
            ProfileViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserService(), NotesService(
            DBHelper(requireContext())
        )
        ))[SharedViewModel::class.java]
        binding.profileViewModel = profileViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileCloseIcon.setOnClickListener { dismiss() }
        binding.SignOutButton.setOnClickListener {
            profileViewModel.logout()
            profileViewModel.logoutStatus.observe(viewLifecycleOwner, {
                sharedViewModel.setGoToLoginPageStatus(true)
                sharedViewModel.setGoToHomePageStatus(false)
            })
            dismiss()
        }
        binding.profileImageView.setOnClickListener{
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 200)
        }
        setProfileDetails()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK)
                intent?.data?.let {
                    profileViewModel.uploadImageToFirebase(it)
                    Glide.with(this).load(it).into(binding.profileImageView)
                    sharedViewModel.setImageUri(it)
                }
    }

    private fun setProfileDetails() {
        sharedViewModel.userDetails.observe(viewLifecycleOwner, {
            binding.profileName.text = it.fullName
            binding.profileEmail.text = it.email
            if (it.imageUrl != "")
                Glide.with(this).load(it.imageUrl).into(binding.profileImageView)
        })
        sharedViewModel.imageUri.observe(viewLifecycleOwner, {
            Glide.with(this).load(it).into(binding.profileImageView)
        })
    }
}