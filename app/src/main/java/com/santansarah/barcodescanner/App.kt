package com.santansarah.barcodescanner

import android.app.Application
import com.santansarah.barcodescanner.utils.logging.DebugTree
import com.santansarah.barcodescanner.utils.logging.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else
            Timber.plant(ReleaseTree())

    }


}