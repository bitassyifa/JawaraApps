package com.projectassyifa.jawaraapps.withdraw.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

class WithdrawRepo @Inject constructor(val withdrawAPI: WithdrawAPI) {
    var resBank = MutableLiveData<List<ListBankModel>>()
    var resAccountBank = MutableLiveData<List<AccountBankModel>>()
    var resApi = MutableLiveData<ResponseAPI>()
    var resWD = MutableLiveData<List<WithdrawHistoryModel>>()


    fun listbank(context: Context){
        withdrawAPI.listBank().enqueue(object : Callback<ResponseAPI> {
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code() == 200){
                    val resData = res?.data
                    val gson = Gson()
                    val listObj : Type = object : TypeToken<List<ListBankModel>>() {}.type
                    val output : List<ListBankModel> = gson.fromJson(gson.toJson(resData),listObj)
                    resBank.value = output
                } else {

                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
    fun postRekUser(token : String, accountBankModel: AccountBankModel , context: Context){
        val id_user = convert(accountBankModel.id_user)
        val no_rek = convert(accountBankModel.no_rek)
        val bank = convert(accountBankModel.bank)
        val nama = convert(accountBankModel.nama)

        withdrawAPI.postRekUser(token,id_user, no_rek, bank, nama).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                    resApi.value = res
            }
            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun postWD(token: String,withdrawModel: WithdrawModel){
        val idUser = convert(withdrawModel.id_user)
        val noRek = convert(withdrawModel.no_rek)
        val bank = convert(withdrawModel.bank)
        val nama = convert(withdrawModel.nama)
        val nominal = convert(withdrawModel.nominal.toString())
        val note = convert(withdrawModel.note)
        withdrawAPI.postWD(token, idUser, noRek, bank, nama, nominal, note).enqueue(object  : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                resApi.value = res
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getRekUser(token: String,id_user:String,context: Context){
        withdrawAPI.getRekUser(token,id_user).enqueue(object: Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code() == 401){
                    Toast.makeText(context, "Token expired", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 404){
                    Toast.makeText(context, res!!.message, Toast.LENGTH_SHORT).show()
                } else {
                    val resData = res?.data
                    val gson = Gson()
                    val listObj : Type = object : TypeToken<List<AccountBankModel>>() {}.type
                    val output : List<AccountBankModel> = gson.fromJson(gson.toJson(resData),listObj)
                    resAccountBank.value = output
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getHistoryWD(token: String,id_user:String,context: Context){
        withdrawAPI.getHistoryWD(token,id_user).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                val res = response.body()
                if (response.code() == 401){
                    Toast.makeText(context, "Token expired", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 404){
                    Toast.makeText(context,"Data tidak di temukan", Toast.LENGTH_SHORT).show()
                } else {
                    val resData = res?.data
                    val gson = Gson()
                    val listObj : Type = object : TypeToken<List<WithdrawHistoryModel>>() {}.type
                    val output : List<WithdrawHistoryModel> = gson.fromJson(gson.toJson(resData),listObj)
                    resWD.value = output
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                t.printStackTrace()
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