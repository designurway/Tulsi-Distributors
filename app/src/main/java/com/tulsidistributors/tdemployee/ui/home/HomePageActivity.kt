package com.tulsidistributors.tdemployee.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ActivityHomePageBinding
import com.tulsidistributors.tdemployee.utils.NavToolbarController
import java.util.*

class HomePageActivity : AppCompatActivity(), NavToolbarController {

    lateinit var homeToolbar: Toolbar

    lateinit var binding: ActivityHomePageBinding
    lateinit var bottonNav: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeToolbar = binding.homeToolbar
        bottonNav = binding.bottomNav

        setSupportActionBar(homeToolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.addProductFragment,
                R.id.notificationFragment, R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottonNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            /*if (destination.id == R.id.attendenceFragment){
                homeToolbar.visibility = GONE
            }*/
        }
    }

    override fun hideToolBar() {
        homeToolbar.visibility = GONE
    }

    override fun showToolbar() {
        homeToolbar.visibility = VISIBLE
    }


}