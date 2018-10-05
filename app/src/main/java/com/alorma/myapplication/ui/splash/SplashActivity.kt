package com.alorma.myapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.myapplication.ui.movies.MoviesActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    val actions: SplashActions by inject()

    val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashViewModel.observe(this) {
            onRoute {
                when (it) {
                    is SplashRoutes.SplashRoute.Main -> openMain()
                    is SplashRoutes.SplashRoute.Error -> openMain()
                }
            }
        }
        splashViewModel reduce actions.load()
    }

    private fun openMain() {
        Intent(this, MoviesActivity::class.java).also { finish() }
    }
}
