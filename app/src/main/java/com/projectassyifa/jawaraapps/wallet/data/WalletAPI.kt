package com.projectassyifa.jawaraapps.wallet.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WalletAPI {
    @GET("pick/history_transaction/{id_user}")
    fun getHistoryTransaction(@Header("Authorization")token : String,
                    @Path("id_user")id_user : String ) : Call<ResponseAPI>
}