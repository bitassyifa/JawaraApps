package com.projectassyifa.jawaraapps.login.data

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import com.projectassyifa.jawaraapps.extra.Token
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject


class LoginRepo @Inject constructor(val loginAPI: LoginAPI) {
    var resApi = MutableLiveData<ResponseAPI>()
    var loginResponse = MutableLiveData<LoginResponse>()
    private lateinit var tokenOuth: Token

    fun login(loginModel: LoginModel,context: Context){
        tokenOuth = Token(context)
        val email = convert(loginModel.email.toString())
        val password = convert(loginModel.password.toString())
        loginAPI.login(email, password).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()

                if (res != null) {
                    if (res.status){
                        val resData = res?.data
                        val gson = Gson()
                        val listObject : Type = object : TypeToken<List<LoginResponse>>() {}.type
                        val output : List<LoginResponse> = gson.fromJson(gson.toJson(resData),listObject)
                        resApi.value = res
                        tokenOuth.saveAuthToken(output[0].token!!)
                        loginResponse.value = output[0]
                        Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context,"Connection Failed ", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun convert (string:String) : RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(),string)
    }
}