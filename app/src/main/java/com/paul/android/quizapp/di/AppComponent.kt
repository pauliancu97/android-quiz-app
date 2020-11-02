package com.paul.android.quizapp.di

import android.content.Context
import com.paul.android.quizapp.ui.main.MainActivity
import com.paul.android.quizapp.di.modules.DatabaseModule
import com.paul.android.quizapp.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}