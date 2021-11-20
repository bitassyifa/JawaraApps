package com.projectassyifa.jawaraapps.maps.data

import com.projectassyifa.jawaraapps.extra.ResponseAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


interface AgentAPI {
    @GET("agent")
    fun agent(@Header("Authorization")token : String) : Call<ResponseAPI>
}