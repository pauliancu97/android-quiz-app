package com.paul.android.quizapp.di

import android.content.Context
import android.content.res.Resources
import com.paul.android.quizapp.ui.main.MainActivity
import com.paul.android.quizapp.di.modules.DatabaseModule
import com.paul.android.quizapp.di.modules.FirebaseModule
import com.paul.android.quizapp.di.modules.NetworkModule
import com.paul.android.quizapp.ui.creategame.CreateGameFragment
import com.paul.android.quizapp.ui.creategame.SelectOptionDialogFragment
import com.paul.android.quizapp.ui.loading.LoadingFragment
import com.paul.android.quizapp.ui.loadingcreatequiz.LoadingCreateQuizFragment
import com.paul.android.quizapp.ui.playgame.PlayQuizFragment
import com.paul.android.quizapp.ui.start.StartFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class, FirebaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance resources: Resources
        ): AppComponent
    }

    fun inject(loadingCreateQuizFragment: LoadingCreateQuizFragment)
    fun inject(startFragment: StartFragment)
    fun inject(loadingFragment: LoadingFragment)
    fun inject(selectOptionsDialogFragment: SelectOptionDialogFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(createGameFragment: CreateGameFragment)
    fun inject(playQuizFragment: PlayQuizFragment)
}