package com.tulsidistributors.tdemployee.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomePageBinding
    lateinit var bottonNav:BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottonNav=binding.bottomNav

         navController = Navigation.findNavController(this,R.id.nav_host_fragment)
      /*  val appBarConfiguration= AppBarConfiguration(setOf(R.id.homeFragment,
            R.id.addProductFragment,
            R.id.notificationFragment,R.id.profileFragment))

        setupActionBarWithNavController(navController,appBarConfiguration)*/
    bottonNav.setupWithNavController(navController)


    }


}