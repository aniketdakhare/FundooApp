package com.example.fundooapp.mainactivity.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fundooapp.R
import com.example.fundooapp.appstartpage.view.AppStartFragment
import com.example.fundooapp.databinding.ActivityMainBinding
import com.example.fundooapp.homepage.view.HomeFragment
import com.example.fundooapp.login.view.LoginFragment
import com.example.fundooapp.model.DBHelper
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.NotesService
import com.example.fundooapp.model.UserService
import com.example.fundooapp.notes.view.AddNoteFragment
import com.example.fundooapp.profilepage.view.ProfilePage
import com.example.fundooapp.register.view.RegisterFragment
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.NotesOperation.*
import com.example.fundooapp.util.ViewType.GRID
import com.example.fundooapp.util.ViewType.LIST
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.main_content_layout.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var profileImage: CircleImageView
    private lateinit var toggle: ActionBarDrawerToggle
    private var viewType = LIST


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActivity()
        setSupportActionBar(binding.contentLayout.toolbar)
        binding.contentLayout.toolbar.showOverflowMenu()
        setNavigationDrawer()
        goToStartAppPage()
        observeAppNavigation()
    }

    override fun onStart() {
        super.onStart()
        binding.contentLayout.addNotesFab.setOnClickListener {
            sharedViewModel.setNoteToWrite(Pair(Note(), ADD))
        }
    }

    private fun setNavigationDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.contentLayout.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        binding.navigationDrawer.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notify_menu -> {
                    Toast.makeText(this, "Reminders menu selected", Toast.LENGTH_SHORT).show()
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
            }
            return@OnNavigationItemSelectedListener true
        })
    }

    private fun initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sharedViewModel = ViewModelProvider(
            this, SharedViewModelFactory(
                UserService(), NotesService(
                    DBHelper(this)
                )
            )
        )[SharedViewModel::class.java]
    }

    private fun observeAppNavigation() {
        sharedViewModel.goToHomePageStatus.observe(this, {
            if (it == true) goToHomePage()
        })
        sharedViewModel.goToRegisterPageStatus.observe(this, {
            if (it == true) goToRegisterUserPage()
        })
        sharedViewModel.goToLoginPageStatus.observe(this, {
            if (it == true) goToLoginUserPage()
        })
        sharedViewModel.writeNote.observe(this, {
            Log.e("Main", "observeAppNavigation: 09", )
            it?.apply {
            goToNotesPage(it) }
        })
        sharedViewModel.showAddNoteFab.observe(this, {
            operateAddNoteFab(it)
        })
    }

    private fun goToStartAppPage() {
        binding.contentLayout.addNotesFab.hide()
        binding.contentLayout.appBarLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, AppStartFragment())
            commit()
        }
    }

    private fun goToNotesPage(note: Pair<Note, NotesOperation>) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, AddNoteFragment(note.first, note.second))
            addToBackStack(null)
            commit()
        }
    }

    private fun goToRegisterUserPage() {
        binding.contentLayout.addNotesFab.hide()
        binding.contentLayout.appBarLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, RegisterFragment())
            commit()
        }
    }

    private fun goToLoginUserPage() {
        binding.contentLayout.addNotesFab.hide()
        binding.contentLayout.appBarLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, LoginFragment())
            commit()
        }
    }

    private fun goToHomePage() {
        binding.contentLayout.addNotesFab.show()
        binding.contentLayout.appBarLayout.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, HomeFragment())
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)

        val profileItem = menu?.findItem(R.id.profile_menu)
        val view = MenuItemCompat.getActionView(profileItem)
        profileImage = view.findViewById(R.id.toolbar_profile_image)
        profileImage.setOnClickListener {
            ProfilePage().show(supportFragmentManager, "User Profile")
        }
        sharedViewModel.imageUri.observe(this, {
            if (it != null) Glide.with(this).load(it).into(profileImage)
        })
        sharedViewModel.userDetails.observe(this, {
            if (it.imageUrl != "")
                Glide.with(this).load(it.imageUrl).into(profileImage)
        })

        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.setQueryText(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notesView -> {
                viewType = when (viewType) {
                    LIST -> {
                        item.setIcon(R.drawable.ic_outline_view_agenda_24)
                        GRID
                    }
                    GRID -> {
                        item.setIcon(R.drawable.ic_outline_dashboard_24)
                        LIST
                    }
                }
                sharedViewModel.setNotesDisplayType(viewType)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun operateAddNoteFab(status: Boolean) {
        when (status) {
            true -> binding.contentLayout.addNotesFab.show()
            false -> binding.contentLayout.addNotesFab.hide()
        }
    }
}
