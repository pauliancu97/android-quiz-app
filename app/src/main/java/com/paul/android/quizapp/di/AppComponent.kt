package com.paul.android.quizapp.di

import android.content.Context
import android.content.res.Resources
import com.paul.android.quizapp.ui.main.MainActivity
import com.paul.android.quizapp.di.modules.DatabaseModule
import com.paul.android.quizapp.di.modules.NetworkModule
import com.paul.android.quizapp.ui.creategame.CreateGameFragment
import com.paul.android.quizapp.ui.creategame.SelectOptionDialogFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance resources: Resources
        ): AppComponent
    }

    fun inject(selectOptionsDialogFragment: SelectOptionDialogFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(createGameFragment: CreateGameFragment)
}