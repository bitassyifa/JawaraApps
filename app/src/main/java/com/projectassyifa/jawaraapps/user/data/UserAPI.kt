package com.projectassyifa.jawaraapps.user.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserAPI {
    @GET("auth/userById/{id}")
    fun userById(@Header("Authorization")token : String,
    @Path("id")id : String) : Call<ResponseAPI>
}