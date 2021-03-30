package com.tulsidistributors.tdemployee.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Common(val context: Context) {

    fun generateRandomNumber():Int{
        Toast.makeText(context, "Radom Number Generated", Toast.LENGTH_SHORT).show()
        return (((Math.random()*900000)+100000).toInt())
    }

    fun getCurrentDate():String{
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()

        val currentDate = formatter.format(date)
        return currentDate
    }

}