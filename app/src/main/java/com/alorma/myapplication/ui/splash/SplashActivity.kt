package com.alorma.myapplication.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.splash.di.SplashModule
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var actions: SplashActions

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component add SplashModule(this) inject this

        viewModel.observe(this) {}
        viewModel reduce actions.load()
    }
}
