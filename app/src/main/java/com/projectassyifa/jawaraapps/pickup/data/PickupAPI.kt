package com.projectassyifa.jawaraapps.pickup.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import retrofit2.Call
import retrofit2.http.*

interface PickupAPI {
    @POST("pick")
    fun insertPick(@Header("Authorization")token : String,
                    @Body pickModel : PickModelInsert) : Call<ResponseAPI>

    @GET("pick/user/{id_user}")
    fun getUserPick(@Header("Authorization")token : String,
                    @Path("id_user")id_user : String ) : Call<ResponseAPI>

    @GET("pick/status/{id_pickup}")
    fun getStatusPick(@Header("Authorization")token : String,
                      @Path("id_pickup")id_pickup : String) : Call<ResponseAPI>

    @GET("pick/wadah")
    fun getWadah():Call<ResponseAPI>
}