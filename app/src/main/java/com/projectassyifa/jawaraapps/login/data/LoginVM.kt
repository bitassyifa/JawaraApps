package com.projectassyifa.jawaraapps.login.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.projectassyifa.jawaraapps.extra.ResponseAPI
import javax.inject.Inject

class LoginVM @Inject constructor(var loginRepo: LoginRepo) {
    var resApi = loginRepo.resApi
    var loginResponse = loginRepo.loginResponse

    fun login(loginModel: LoginModel,context: Context){
        loginRepo.login(loginModel, context)
    }
}