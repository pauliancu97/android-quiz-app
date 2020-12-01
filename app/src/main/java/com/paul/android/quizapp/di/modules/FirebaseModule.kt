package com.paul.android.quizapp.di.modules

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun providerFirebaseDatabase() = FirebaseDatabase.getInstance()
}