package com.projectassyifa.jawaraapps.login.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LoginAPI {
    @Multipart
    @POST("auth/login")
    fun login(@Part("email")email : RequestBody,
              @Part("password")password : RequestBody):Call<ResponseAPI>
}