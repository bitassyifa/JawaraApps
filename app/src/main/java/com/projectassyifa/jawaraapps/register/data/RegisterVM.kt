package com.projectassyifa.jawaraapps.register.data

import android.content.Context
import javax.inject.Inject

class RegisterVM @Inject constructor(var registerRepo: RegisterRepo){
    var resApi = registerRepo.resApi

    fun register(registerModel: RegisterModel,context: Context){
        registerRepo.register(registerModel, context)
    }
}