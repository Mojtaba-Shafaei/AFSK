package com.shafaei.paradox

import android.app.Application
import com.shafaei.paradox.di.decoder
import com.shafaei.paradox.di.mainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AFSKApplication : Application() {
 override fun onCreate() {
  super.onCreate()
  startKoin {
   androidContext(this@AFSKApplication)
   modules(
    decoder,
    mainViewModel
   )
  }
 }
}