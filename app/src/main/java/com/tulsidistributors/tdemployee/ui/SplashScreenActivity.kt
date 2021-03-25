package com.tulsidistributors.tdemployee.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ActivitySplashScreenBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var loginPrefrence:UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPrefrence = UserLoginPreferences(dataStore)

        getUserDetail()

    }

    private fun getUserDetail() {

   loginPrefrence.empIdFlow.asLiveData().observe(this) { empId ->
            Toast.makeText(this, "EmpId ${empId}", Toast.LENGTH_SHORT).show()

            if (empId==null){
                val intent = Intent(this,AuthActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this,HomePageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }




}