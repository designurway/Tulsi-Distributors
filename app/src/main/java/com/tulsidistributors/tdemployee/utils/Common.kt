package com.tulsidistributors.tdemployee.utils

import android.content.Context
import android.widget.Toast
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Common() {

    fun generateRandomNumber():Int{
        return (((Math.random()*900000)+100000).toInt())
    }

    fun getCurrentDate(pattern:String):String{
        val formatter = SimpleDateFormat(pattern)
        val date = Date()
        val currentDate = formatter.format(date)
        return currentDate
    }

    fun getTime(dateTime:String):String{
        val DateTime = dateTime.split(" ").toTypedArray()
        val time =DateTime[1]
        return time
    }

    fun timeFormatter(time:String):String{
        val timeFormat:DateFormat = SimpleDateFormat("hh:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time:Date = sdf.parse(time)
        val  formattedTime = timeFormat.format(time)
        return formattedTime

    }
}