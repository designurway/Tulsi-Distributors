package com.tulsidistributors.tdemployee.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ActivityHomePageBinding
import com.tulsidistributors.tdemployee.utils.NavToolbarController
import com.tulsidistributors.tdemployee.work_manager.AlarmReceiver
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

//        setLogoutTime()


        var title = intent.extras?.getString("Title")
        if (title != null) {
           navController.navigate(R.id.notificationFragment)
        }
    }


    private fun setLogoutTime() {

        val calNow = Calendar.getInstance();
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 58)
        calendar.set(Calendar.SECOND, 0)

//        val time = calendar.getTimeInMillis()

        //getting the alarm manager

        //Today Set time passed, count to tomorrow
        setalram(calendar.getTimeInMillis())

    }

    private fun setalram(time: Long) {

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //creating a new intent specifying the broadcast receiver
        val i = Intent(this, AlarmReceiver::class.java)

        //creating a pending intent using the intent
        val pi = PendingIntent.getBroadcast(this, 0, i, 0)

        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time, 24 * 60 * 60 * 1000, pi
        );
        //setting the repeating alarm that will be fired every day
//        am.setRepeating(AlarmManager.RTC, time, AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED, pi)
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()

    }


    override fun hideToolBar() {
        homeToolbar.visibility = GONE
    }

    override fun showToolbar() {
        homeToolbar.visibility = VISIBLE
    }


}