package com.projectassyifa.jawaraapps.pickup.data

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

class PickupRepo @Inject constructor(val pickupAPI: PickupAPI) {
    var resApi = MutableLiveData<ResponseAPI>()
    var resPick = MutableLiveData<List<PickupModel>>()
    var resStatus = MutableLiveData<List<PickStatus>>()

    fun insertPick(token: String,pickModelInsert:PickModelInsert,context: Context){
        pickupAPI.insertPick(token,pickModelInsert).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                resApi.value = res
                Toast.makeText(context,"${res?.message} ", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context,"Connection Failed : $t ", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getUserPick(token: String ,id_user : String,context: Context) {
        pickupAPI.getUserPick(token,id_user).enqueue(object :Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code() != 401){
                    val resData = res?.data
                    val gson = Gson()
                    val listObject : Type = object :TypeToken<List<PickupModel>>() {}.type
                    val output : List<PickupModel> = gson.fromJson(gson.toJson(resData),listObject)
                    resPick.value = output
                } else {
                    Toast.makeText(
                        context,
                        "Request Failed : ${response.body()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Toast.makeText(
                    context,
                    " Failed Connection: ${t.printStackTrace()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    fun getStatusPick(token: String,id_pickup: String,context: Context){
        println("REPO $id_pickup")
        pickupAPI.getStatusPick(token, id_pickup).enqueue(object :Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code() !=401){
                    val resData = res?.data
                    val gson = Gson()
                    val listObject : Type = object : TypeToken<List<PickStatus>>() {}.type
                    val output : List<PickStatus> = gson.fromJson(gson.toJson(resData),listObject)
                    resStatus.value = output
                    println("OUTPUT ${resStatus.value!![0].keterangan}")
                } else {
                    println("ELSE")
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Request Failed : ${t.printStackTrace()}",
                    Toast.LENGTH_SHORT
                ).show()
                println("FAILED CONNECT")
            }

        })
    }

}