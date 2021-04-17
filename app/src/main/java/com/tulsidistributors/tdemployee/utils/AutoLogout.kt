package com.tulsidistributors.tdemployee.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tulsidistributors.tdemployee.alaram_manager.AlarmReceiver
import java.util.*

class AutoLogout(val context: Context) {
     fun setAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 13)
        calendar.set(Calendar.SECOND, 0)


        val alaramManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1)
            showToast(context,"Alarm Next Day")
        }
        alaramManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)


        showToast(context, "Alarm is Set")

    }


     fun cancelAlarm() {
        val alaramManager:AlarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context,101,intent,0)

        alaramManager.cancel(pendingIntent)

        showToast(context, "Alarm is Cancled")

    }


}