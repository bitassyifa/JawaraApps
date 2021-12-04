package com.projectassyifa.jawaraapps.user.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {
    @GET("auth/userById/{id}")
    fun userById(@Header("Authorization")token : String,
                 @Path("id")id : String) : Call<ResponseAPI>

    @Multipart
    @POST("auth/userUpdate/{id_user}")
    fun userUpdate(@Header("Authorization")token : String,
                   @Path("id_user") id_user: String,
                   @Part("nama") nama : RequestBody,
                   @Part("jk") jk : RequestBody,
                   @Part("nik") nik : RequestBody,
                   @Part("tgl_lahir") tgl_lahir : RequestBody,
                   @Part("no_tlp") no_tlp : RequestBody,
                   @Part("alamat") alamat : RequestBody,
                   @Part foto_ktp : MultipartBody.Part? = null
                   ) : Call<ResponseAPI>

    @Multipart
    @POST("auth/userUpdateNoPhoto/{id_user}")
    fun userUpdateNophoto(@Header("Authorization")token : String,
                   @Path("id_user") id_user: String,
                   @Part("nama") nama : RequestBody,
                   @Part("jk") jk : RequestBody,
                   @Part("nik") nik : RequestBody,
                   @Part("tgl_lahir") tgl_lahir : RequestBody,
                   @Part("no_tlp") no_tlp : RequestBody,
                   @Part("alamat") alamat : RequestBody,
    ) : Call<ResponseAPI>
}