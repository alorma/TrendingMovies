package com.alorma.myapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.myapplication.ui.movies.MoviesActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var actions: SplashActions

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.observe(this) {
            onRoute {
                when (it) {
                    is SplashRoutes.SplashRoute.Main -> openMain()
                    is SplashRoutes.SplashRoute.Error -> openMain()
                }
            }
        }
        viewModel reduce actions.load()
    }

    private fun openMain() {
        Intent(this, MoviesActivity::class.java).also { finish() }
    }
}
