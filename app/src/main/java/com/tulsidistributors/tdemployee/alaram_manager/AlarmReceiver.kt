package com.tulsidistributors.tdemployee.alaram_manager

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import com.tulsidistributors.tdemployee.utils.AutoLogout
import com.tulsidistributors.tdemployee.utils.Common
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class AlarmReceiver : BroadcastReceiver() {
    lateinit var userLoginPreferences: UserLoginPreferences

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("abc", "Alarm fired 1")

        userLoginPreferences  = UserLoginPreferences(context.dataStore)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()

        Log.d("abc", "Alarm fired 2")


       get(context)


    }

    fun get(context: Context){

        Log.d("abc", "method called")
        
        CoroutineScope(Dispatchers.Main).launch {
            userLoginPreferences.empIdFlow.collect {
                Log.d("abc", "Emp Id ${it.toString()}")
                showToast(context, "Emp Id ${it.toString()}")

                try {

                    val date = Common().getCurrentDate("yyyy-MM-dd")

                    val response = updateLogoutTimeApiCall(it.toString(),date)

                    if (response.isSuccessful){

                        val responseData = response.body()

                        showToast(context, responseData!!.message)

                        AutoLogout(context).cancelAlarm()

                        userLoginPreferences.saveIsLoggedIn(false)

                        val intent = Intent(context, AuthActivity::class.java)
                        context.startActivity(intent)




                    }else{
                        showToast(context, "Response Error ${response.message()}")
                    }
                }catch (e:java.lang.Exception){
                    showToast(context,"Exception Occurred ${e.message}")
                }
            }
        }
    }



     suspend fun updateLogoutTimeApiCall(empID: String,loginDate:String) : Response<StatusMessageModel> {
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.updateLogoutTime(empID,loginDate)

        }

    }

}