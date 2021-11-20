package com.projectassyifa.jawaraapps.config

import android.app.Application

class JawaraApps : Application() {
    val applicationComponent : ApplicationComponent = DaggerApplicationComponent.create()
}