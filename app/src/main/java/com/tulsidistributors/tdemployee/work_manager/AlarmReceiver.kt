package com.tulsidistributors.tdemployee.work_manager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import com.tulsidistributors.tdemployee.utils.AutoLogout


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//        val prefrence =UserLoginPreferences(context.dataStore)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()

        Log.d("abc", "Alaram fired")
        AutoLogout(context).cancelAlarm()
        val intent = Intent(context, AuthActivity::class.java)
        context.startActivity(intent)




    }
}