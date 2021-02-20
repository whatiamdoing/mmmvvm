package ru.whatiamdoing.mmmvvm.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.*
import ru.whatiamdoing.mmmvvm.App
import ru.whatiamdoing.mmmvvm.ui.AppActivity

/**
 * Created by Dima Ivanov on 2/20/21.
 */

@Component(
    modules = [
        AppModule::class,
        NavigationModule::class
    ],
    dependencies = []
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun build(): AppComponent
    }

    companion object {
        fun start(application: App): AppComponent =
            with(DaggerAppComponent.builder()) {
                application(application)
                build()
            }
    }

    fun inject(app: App)
    fun inject(activity: AppActivity)
}

@Module
object AppModule {

    @Provides
    fun provideContext(application: App): Context = application

}

@Module
object NavigationModule {

    @Provides
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    fun provideRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Provides
    fun provideNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()

}