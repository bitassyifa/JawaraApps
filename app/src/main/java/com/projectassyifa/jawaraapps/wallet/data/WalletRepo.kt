package com.projectassyifa.jawaraapps.wallet.data

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

class WalletRepo @Inject constructor(val walletAPI: WalletAPI){
    var resHistory = MutableLiveData<List<HistoryTransModel>>()

    fun getHistoryTransaction(token: String ,id_user : String,context: Context){
        walletAPI.getHistoryTransaction(token, id_user).enqueue(object : Callback<ResponseAPI>{
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
               val res = response.body()
                if (response.code()==401){
                    Toast.makeText(
                        context,
                        "Token Expired",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code()==404) {
                    Toast.makeText(
                        context,
                        "Belum ada transaksi masuk",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    if (res?.status == true){
                        val resData = res?.data
                        val gson = Gson()
                        val listObject : Type = object : TypeToken<List<HistoryTransModel>>() {}.type
                        val output : List<HistoryTransModel> = gson.fromJson(gson.toJson(resData),listObject)
                        resHistory.value = output
                    } else {
                        Toast.makeText(
                            context,
                            "${res!!.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
               t.printStackTrace()
            }

        })
    }
}