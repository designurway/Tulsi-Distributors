package com.tulsidistributors.tdemployee.json

import com.tulsidistributors.tdemployee.model.StatusMessageModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TDApi {

    // 08-03-2021

    @FormUrlEncoded
    @POST("executiveLogin")
    suspend fun executiveLogin(
        @Field("emp_id") empId: String,
        @Field("password") password: String
    ): Response<StatusMessageModel>


    @FormUrlEncoded
    @POST("generateOtp")
    suspend fun generateOtp(
        @Field("phone_number") phone: String
    ): Response<StatusMessageModel>


    @FormUrlEncoded
    @POST("verifyPhoneOtp")
    suspend fun verifyPhoneOtp(
        @Field("phone_number") phone: String,
        @Field("otp") otp: String
    ): Response<StatusMessageModel>


    // 09-03-2021

    @FormUrlEncoded
    @POST("updateNewPassword")
    suspend fun updateNewPassword(
        @Field("phone_number") phone: String,
        @Field("password") password: String
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("uploadSelfie")
    suspend fun uploadSelfie(
        @Field("sales_executive_id") salesId: String,
        @Field("login_date") login_date: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("profile_image") profile_image: String
    ):Response<StatusMessageModel>
}