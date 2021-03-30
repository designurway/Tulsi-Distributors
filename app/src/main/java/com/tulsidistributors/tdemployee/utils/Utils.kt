package com.tulsidistributors.tdemployee.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message, Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }

    }.show()
}

fun SimpleArcLoader(context: Context,message:String,isCanclable:Boolean){
    val mDialog = SimpleArcDialog(context)
    val arcConfiguration = ArcConfiguration(context)
    arcConfiguration.loaderStyle = com.leo.simplearcloader.SimpleArcLoader.STYLE.SIMPLE_ARC
    arcConfiguration.text = message
    mDialog.setConfiguration(arcConfiguration)
    mDialog.setCanceledOnTouchOutside(isCanclable)
    mDialog.show()
}

fun showToast(context: Context,message: String){

    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
}

fun ContentResolver.getFileName(uri:Uri):String{

    var name=""
    val cursor = query(uri,null,null,null,null)

    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

    return name

}

