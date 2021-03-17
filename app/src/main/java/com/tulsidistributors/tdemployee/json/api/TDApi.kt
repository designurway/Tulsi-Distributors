package com.tulsidistributors.tdemployee.json

import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.model.admin_brand.AdminBrandModel
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.model.attendance.AttendanceModel
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOderModel
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOrderData
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductModel
import com.tulsidistributors.tdemployee.model.login.LoginModel
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationDataModel
import retrofit2.Response
import retrofit2.http.*

interface TDApi {

    // 08-03-2021

    @FormUrlEncoded
    @POST("login")
    suspend fun executiveLogin(
        @Field("empId") empId: String,
        @Field("password") password: String
    ): Response<LoginModel>


    @FormUrlEncoded
    @POST("generateOtp")
    suspend fun generateOtp(
        @Field("phone") phone: String
    ): Response<StatusMessageModel>


    @FormUrlEncoded
    @POST("verifyForgotPassOtp")
    suspend fun verifyPhoneOtp(
        @Field("phone") phone: String,
        @Field("otp") otp: String
    ): Response<StatusMessageModel>


    // 09-03-2021

    @FormUrlEncoded
    @POST("changePassword")
    suspend fun updateNewPassword(
        @Field("phone") phone: String,
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
    ): Response<StatusMessageModel>


    @GET("getAssignedOrder")
    suspend fun getAssignedOrder(
        @Query("empId") empId: String
    ): Response<AssignedOrderModel>

    @GET("getCompletedOrders")
    suspend fun getCompletedOrder(
        @Query("empId") empId: String
    ): Response<CompletedOderModel>


    @GET("getAttendance")
    suspend fun getAttendance(
        @Query("empId") empId:String,
        @Query("date") date:String
    ): Response<AttendanceModel>

    @GET("getAdminBrandList")
    suspend fun getAdminBrandList():Response<AdminBrandModel>

    @GET("searchAdminBrand")
    suspend fun searchAdimItem(
        @Query("search") search:String
    ):Response<AdminBrandModel>

    @GET("stockItemList")
    suspend fun getStockItem(
        @Query("brandName") brandName:String,
    ):Response<SearchStockItemModel>

    @GET("searchStockItem")
    suspend fun getStockItemList(
        @Query("brandName") brandName:String,
        @Query("search") search:String,
    ):Response<SearchStockItemModel>

    @FormUrlEncoded
    @POST("postSupportQuery")
    suspend fun postQuery(
        @Field("emp_id") empId: String,
        @Field("message") message:String
    ):Response<StatusMessageModel>

    @GET("getNotification")
    suspend fun getNotification(
        @Query("emp_id") empId: String
    ):Response<NotificationDataModel>

    @GET("getDealerProductItem")
    suspend fun getDealerProductItem(
        @Query("dealer_id") dealer_id:String
    ):Response<DealerProductModel>

    @GET("searchDealerProductItem")
    suspend fun searchDealerProductItem(
        @Query("dealer_id") dealer_id:String,
        @Query("search") search:String
    ):Response<DealerProductModel>
}