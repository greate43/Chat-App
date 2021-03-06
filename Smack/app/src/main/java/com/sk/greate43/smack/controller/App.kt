package com.sk.greate43.smack.controller

import android.app.Application
import com.sk.greate43.smack.utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)

        super.onCreate()

    }
}