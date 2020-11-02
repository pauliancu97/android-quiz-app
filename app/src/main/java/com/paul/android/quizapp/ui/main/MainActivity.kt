package com.paul.android.quizapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.paul.android.quizapp.R
import com.paul.android.quizapp.di.AppComponent
import com.paul.android.quizapp.di.DaggerAppComponent
import javax.inject.Inject
import androidx.fragment.app.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var appComponent: AppComponent
        private set

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory

    private val viewModel: MainActivityViewModel by viewModels {
        mainActivityViewModelFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialize()
    }
}