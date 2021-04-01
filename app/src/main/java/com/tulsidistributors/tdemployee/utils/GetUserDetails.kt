package com.tulsidistributors.tdemployee.utils

import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import kotlinx.coroutines.launch
import java.util.logging.Logger.global

class GetUserDetails(val userLoginPreferences: UserLoginPreferences,val viewLifecycleOwner:LifecycleOwner) {

    fun getEmpId():String{

        var empId:String = "no data"

       userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            empId = it.toString()
            showLog("empId","EmpId $empId")
        })
        showLog("empId","EmpIddddd $empId")

        return empId
    }
}