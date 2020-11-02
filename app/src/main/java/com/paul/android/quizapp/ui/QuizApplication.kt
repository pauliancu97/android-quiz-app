package com.paul.android.quizapp.ui

import android.app.Application
import com.facebook.stetho.Stetho

class QuizApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}