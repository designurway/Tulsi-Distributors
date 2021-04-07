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

        var title = intent.extras?.getString("Title")

        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()

        if (title != null) {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("Title",title)
            startActivity(intent)
            finish()
        }else{
            getUserDetail()
        }

    }

    private fun getUserDetail() {



    }




}