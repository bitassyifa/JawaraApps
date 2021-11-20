package com.projectassyifa.jawaraapps.maps.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

class AgentRepo @Inject constructor(val agentAPI: AgentAPI) {
    var agentResponse = MutableLiveData<List<AgentModel>>()

    fun agent(token : String,context: Context){
        agentAPI.agent(token).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
             val res = response.body()
                if (response.code() != 401){
                    val resData = res?.data
                    val gson = Gson()
                    val listObject : Type = object : TypeToken<List<AgentModel>>() {}.type
                    val output : List<AgentModel> = gson.fromJson(gson.toJson(resData),listObject)
                    agentResponse.value = output
                    println("Data agent ${agentResponse.value}")
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Request Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}