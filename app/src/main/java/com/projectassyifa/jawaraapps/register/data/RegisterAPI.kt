package com.projectassyifa.jawaraapps.register.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterAPI {
    @Multipart
    @POST("auth/register")
    fun register(
        @Part("email")email : RequestBody,
        @Part("username")username : RequestBody,
        @Part("no_tlp")no_tlp : RequestBody,
        @Part("password")password : RequestBody
    ):Call<ResponseAPI>
}