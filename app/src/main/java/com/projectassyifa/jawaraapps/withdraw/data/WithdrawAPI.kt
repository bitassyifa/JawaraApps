package com.projectassyifa.jawaraapps.withdraw.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import com.projectassyifa.jawaraapps.extra.Token
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface WithdrawAPI {
    @GET("withdraw/list_bank")
    fun listBank():Call<ResponseAPI>

    @Multipart
    @POST("withdraw/rekening_user")
    fun postRekUser(@Header("Authorization")token: String,
                    @Part("id_user")id_user : RequestBody,
                    @Part("no_rek")no_rek : RequestBody,
                    @Part("bank")bank : RequestBody,
                    @Part("nama") nama : RequestBody) : Call<ResponseAPI>

    @Multipart
    @POST("withdraw")
    fun postWD(@Header("Authorization")token: String,
               @Part("id_user")id_user : RequestBody,
               @Part("no_rek")no_rek : RequestBody,
               @Part("bank")bank : RequestBody,
               @Part("nama") nama : RequestBody,
               @Part("nominal")nominal : RequestBody,
               @Part("note")note:RequestBody) : Call<ResponseAPI>

    @GET("withdraw/rekening_user/{id_user}")
    fun getRekUser(@Header("Authorization")token: String,
                    @Path("id_user")id_string:String) :Call<ResponseAPI>

    @GET("withdraw/expense_history/{id_user}")
    fun getHistoryWD(@Header("Authorization")token: String,
                     @Path("id_user")id_string:String) :Call<ResponseAPI>

}