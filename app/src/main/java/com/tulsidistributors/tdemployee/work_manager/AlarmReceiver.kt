package com.tulsidistributors.tdemployee.work_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.datastore.dataStore
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import kotlin.math.log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//        val prefrence =UserLoginPreferences(context.dataStore)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        Log.d("alaram","Alram................")

        val mediaPlayer=MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()

      /*  val intent = Intent(context,AuthActivity::class.java)
        context.startActivity(intent)*/

    }
}