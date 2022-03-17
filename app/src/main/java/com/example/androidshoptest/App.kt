package com.example.androidshoptest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE=this
    }
    companion object{
        private var INSTANCE:App?=null

        fun getINSTANCE():App{
            if (INSTANCE==null){
                INSTANCE= App()
            }
            return INSTANCE!!
        }
    }
}