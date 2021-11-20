package com.projectassyifa.jawaraapps.user.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import com.projectassyifa.jawaraapps.extra.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

class UserRepo @Inject constructor(val userAPI: UserAPI) {
    var userResponse = MutableLiveData<UserModel>()


    fun userById(token: String ,id : String,context: Context){

        userAPI.userById(token,id).enqueue(object :Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code()!= 401){
                    val resData = res?.data
                    val gson = Gson()
                    val listObject : Type = object :TypeToken <List<UserModel>>() {}.type
                    val output : List<UserModel> = gson.fromJson(gson.toJson(resData),listObject)
                    userResponse.value = output[0]
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

