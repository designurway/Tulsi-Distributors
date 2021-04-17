  package com.tulsidistributors.tdemployee.json

import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.model.admin_brand.AdminBrandModel
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.model.attendance.AttendanceModel
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOderModel
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductModel
import com.tulsidistributors.tdemployee.model.login.LoginModel
import com.tulsidistributors.tdemployee.model.pending_payment_list.PendingPaymentListModel
import com.tulsidistributors.tdemployee.model.place_order_model.PlaceOrderModel
import com.tulsidistributors.tdemployee.model.placed_order_list.PlacedOrderListModel
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.model.user_detail.UserDetailModel
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    @GET("getAssignedOrders")
    suspend fun getAssignedOrder(
        @Query("empId") empId: String
    ): Response<AssignedOrderModel>

    @GET("getCompletedOrders")
    suspend fun getCompletedOrder(
        @Query("empId") empId: String
    ): Response<CompletedOderModel>


    @GET("getAttendance")
    suspend fun getAttendance(
        @Query("empId") empId: String,
        @Query("date") date: String
    ): Response<AttendanceModel>

    @GET("getAdminBrandList")
    suspend fun getAdminBrandList(): Response<AdminBrandModel>

    @GET("searchAdminBrand")
    suspend fun searchAdimItem(
        @Query("search") search: String
    ): Response<AdminBrandModel>

    @GET("getStockList")
    suspend fun
            getStockItem(
        @Query("brand_id") brandId: String,
    ): Response<SearchStockItemModel>

    @GET("searchStockItem")
    suspend fun getStockItemList(
        @Query("brand_id") brandId: String,
        @Query("search") search: String,
    ): Response<SearchStockItemModel>

    @FormUrlEncoded
    @POST("postSupportQuery")
    suspend fun postQuery(
        @Field("emp_id") empId: String,
        @Field("message") message: String
    ): Response<StatusMessageModel>

    @GET("getNotification")
    suspend fun getNotification(
        @Query("emp_id") empId: String
    ): Response<NotificationDataModel>

    @GET("getDealerProductItem")
    suspend fun getDealerProductItem(
        @Query("dealer_id") dealer_id: String,
        @Query("empId") empId: String
    ): Response<DealerProductModel>

    @GET("searchDealerProductItem")
    suspend fun searchDealerProductItem(
        @Query("dealer_id") dealer_id: String,
        @Query("search") search: String,
        @Query("empId") empId: String
    ): Response<DealerProductModel>

    @Multipart
    @POST("updateProfileImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("phone") phone: RequestBody,
        @Part("name") name: RequestBody,
    ): Response<StatusMessageModel>

    @Multipart
    @POST("uploadSelfie")
    suspend fun uploadSelfie(
        @Part image: MultipartBody.Part,
        @Part("emp_id") saleExecutiveId: RequestBody,
        @Part("loginDate") loginDate: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Response<StatusMessageModel>

    @GET("getPlacedOrderList")
    suspend fun getPlacedOrderList(
        @Query("dealer_id") dealer_id: String,
        @Query("date") date: String
    ): Response<PlacedOrderListModel>


    @POST("placeOrder")
    suspend fun placeOrder(

        @Body order_list: PlaceOrderModel
    ): Response<StatusMessageModel>


    @GET("getUserDetails")
    suspend fun getUserDetails(
        @Query("emp_id") emp_id:String
    ):Response<UserDetailModel>

    @FormUrlEncoded
    @POST("postNotificationToken")
    suspend fun uploadNotifationToken(
        @Field("emp_id") emp_id:String,
        @Field("token") token:String
    ):Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("addProductToDealer")
    suspend fun addProductToDealer(
        @Field("sales_executive_id") sales_executive_id:String,
        @Field("dealer_id") dealer_id:String,
        @Field("product_id") product_id:String,
    ):Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("insertStock")
    suspend fun insertStock(
        @Field("sales_executive_id") sales_executive_id:String,
        @Field("dealer_id") dealer_id:String,
        @Field("product_id") product_id:String,
        @Field("quantity") quantity:String,
    ):Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("addPaymentDetails")
    suspend fun addPaymentDetails(
        @Field("reference_id") reference_no:String,
        @Field("dealer_id") dealer_id:String,
        @Field("pending_amount") pending_amount:String,
        @Field("advance_amount") advance_amount:String,
        @Field("payment_mode") payment_mode:String,
        @Field("remarks") remarks:String,
        @Field("purchase_date") purchase_date:String,
        @Field("total_amount") total_amount:String,
        @Field("due_date") due_date:String,
    ):Response<StatusMessageModel>

    @GET("getPendingPaymentList")
    suspend fun getPendingPaymentList(
        @Query("dealer_id") dealer_id:String
    ):Response<PendingPaymentListModel>


    @FormUrlEncoded
    @POST("updatePendingPaymentDetails")
    suspend fun updatePendingPaymentDetails(
        @Field("reference_id") reference_id:String,
        @Field("dealer_id") dealer_id:String,
        @Field("total_amount") total_amount:String,
        @Field("pending_amount") pending_amount:String,
        @Field("advance_amount") advance_amount:String,
        @Field("payment_mode") payment_mode:String,
        @Field("remarks") remarks:String

    ):Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateLogoutTime")
    suspend fun updateLogoutTime(
        @Field("emp_id") empId: String,
        @Field("login_date") login_date:String
    ):Response<StatusMessageModel>


}

