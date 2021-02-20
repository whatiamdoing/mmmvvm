package ru.whatiamdoing.mmmvvm

import android.app.Application
import ru.whatiamdoing.mmmvvm.di.AppComponent

/**
 * Created by Dima Ivanov on 2/19/21.
 */

class App: Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.start(this)
    }

}