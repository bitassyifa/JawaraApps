package com.projectassyifa.jawaraapps.register.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectassyifa.jawaraapps.extra.MessageError
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

class RegisterRepo @Inject constructor(val registerAPI: RegisterAPI){
    var resApi = MutableLiveData<ResponseAPI>()
    var resErr = MutableLiveData<List<MessageError>>()

    fun register(registerModel: RegisterModel,context: Context){
        val username = convert(registerModel.username)
        val email = convert(registerModel.email)
        val password = convert(registerModel.password)

        registerAPI.register(email, username, password).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (res?.status == true){
                    resApi.value = res
                } else {
                    val err = res?.errors
                    val gson = Gson()
                    val errObj : Type = object : TypeToken<List<MessageError>>() {}.type
                    val output : List<MessageError> = gson.fromJson(gson.toJson(err),errObj)
                    resErr.value = output
                    Toast.makeText(context, "${output[0].field} ${output[0].type}", Toast.LENGTH_SHORT).show()

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