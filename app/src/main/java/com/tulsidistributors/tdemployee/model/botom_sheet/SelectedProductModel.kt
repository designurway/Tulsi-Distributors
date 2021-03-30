package com.designurway.tdapplication.model

import com.google.gson.annotations.SerializedName

data class SelectedProductModel (
        var proName:String,
        var proQnty:String,
        var proPrice:String,
        var bandId:String,
        var position:Int,
        var secondPrice:Int
)
