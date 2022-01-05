package com.projectassyifa.jawaraapps.user.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject
import android.content.res.Resources
import com.projectassyifa.jawaraapps.login.layout.LoginActivity


class UserRepo @Inject constructor(val userAPI: UserAPI) {
    var userResponse = MutableLiveData<UserModel>()
    var resApi = MutableLiveData<ResponseAPI>()
    var activity : Activity? = null
    var codeError = MutableLiveData<Int>()
//    val dataLogin : SharedPreferences = getSharedPreferences("Mypref", 0);

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
                } else {
                    val settings =
                        context.getSharedPreferences(context.getString(R.string.sp), Context.MODE_PRIVATE)
                    settings.edit().clear().apply()
//                    codeError.value = response.code()
                    Toast.makeText(
                        context,
                        "Sesi anda telah habis , mohon login ulang",
                        Toast.LENGTH_SHORT
                    ).show()
                    context.startActivity(Intent(context,LoginActivity::class.java))
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

    fun userUpdate(token: String,updateUserModel: UpdateUserModel,context: Context,file : File){
//        val id_user = convert(updateUserModel.id_user.toString())
        val nama = convert(updateUserModel.nama.toString())
        val jk = convert(updateUserModel.jk.toString())
        val nik = convert(updateUserModel.nik.toString())
        val tgl_lahir = convert(updateUserModel.tgl_lahir.toString())
        val no_tlp = convert(updateUserModel.no_tlp.toString())
        val alamat = convert(updateUserModel.alamat.toString())
//        val foto_diri = convertFile(foto_diri)
        val fotoKtp = convertFile(file)
        userAPI.userUpdate(token,updateUserModel.id_user.toString(),nama, jk, nik, tgl_lahir, no_tlp, alamat,fotoKtp).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
               if (res != null){
                   if (res.status){
                       resApi.value = res
                   } else {
                       Toast.makeText(context, "${res.message}", Toast.LENGTH_SHORT).show()
                   }
               } else {
                   Toast.makeText(context, "Tejadi kesalahan ${response.message()}", Toast.LENGTH_SHORT).show()
               }
            }
            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Toast.makeText(context, "Failed ${t.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun userUpdateNoPhoto(token: String,updateUserModel: UpdateUserModel,context: Context){
        val nama = convert(updateUserModel.nama.toString())
        val jk = convert(updateUserModel.jk.toString())
        val nik = convert(updateUserModel.nik.toString())
        val tgl_lahir = convert(updateUserModel.tgl_lahir.toString())
        val no_tlp = convert(updateUserModel.no_tlp.toString())
        val alamat = convert(updateUserModel.alamat.toString())
        userAPI.userUpdateNophoto(token,updateUserModel.id_user.toString(),nama, jk, nik, tgl_lahir, no_tlp, alamat).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (res != null){
                    if (res.status){
                        resApi.value = res
                    } else {
                        Toast.makeText(context, "${res.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Tejadi kesalahan ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Toast.makeText(context, "Failed ${t.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convert (string:String) : RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(),string)
    }
    private fun convertFile(file: File): MultipartBody.Part{
        val reqFile : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),file)
        return MultipartBody.Part.createFormData("foto_ktp",file.name,reqFile)
    }
}

