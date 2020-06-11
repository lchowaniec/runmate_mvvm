package com.lchowaniec.runmate_me.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.ui.profile.friends.AddFriendFragment
import com.lchowaniec.runmate_me.ui.profile.EditProfileFragment
import com.lchowaniec.runmate_me.util.BottomNavController
import com.lchowaniec.runmate_me.util.setUpNavigation
import com.lchowaniec.runmate_me.util.startAuthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener
{

    lateinit var viewModel: MainViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.run,
            this,
            this)
    }

    override fun getNavGraphId(itemId: Int) = when(itemId){
        R.id.run -> {
            R.navigation.nav_run
        }
        R.id.feed -> {
            R.navigation.nav_feed
        }
        R.id.challenge -> {
            R.navigation.nav_challenge
        }
        R.id.premium -> {
            R.navigation.nav_premium
        }
        R.id.profile -> {
            R.navigation.nav_profile
        }

        else -> {
            R.navigation.nav_run
        }
    }

    override fun onGraphChange() {
//        TODO("What needs to happen when the graph changes?")
    }

    override fun onReselectNavItem(
        navController: NavController,
        fragment: Fragment
    ) = when(fragment){
        is EditProfileFragment -> {
            navController.navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        is AddFriendFragment -> {
            navController.navigate(R.id.action_addFriendFragment_to_profileFragment)
        }




        else -> {
            // do nothing
        }
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)



    }


    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.user == null){
            startAuthActivity()
        }
    }

}
