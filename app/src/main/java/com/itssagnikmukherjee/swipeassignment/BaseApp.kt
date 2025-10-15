package com.itssagnikmukherjee.swipeassignment

import android.app.Application
import com.itssagnikmukherjee.swipeassignment.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApp)
            modules(appModule)
        }
    }
}