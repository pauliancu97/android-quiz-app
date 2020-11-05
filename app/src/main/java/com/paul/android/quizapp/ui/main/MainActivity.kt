package com.paul.android.quizapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paul.android.quizapp.R
import com.paul.android.quizapp.di.AppComponent
import com.paul.android.quizapp.di.DaggerAppComponent

class MainActivity : AppCompatActivity() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = DaggerAppComponent.factory().create(applicationContext, resources)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}