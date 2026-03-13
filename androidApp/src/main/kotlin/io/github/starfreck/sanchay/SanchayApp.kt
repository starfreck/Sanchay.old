package io.github.starfreck.sanchay

import android.app.Application
import io.github.starfreck.sanchay.di.initKoin
import org.koin.android.ext.koin.androidContext

class SanchayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@SanchayApp)
        }
    }
}
