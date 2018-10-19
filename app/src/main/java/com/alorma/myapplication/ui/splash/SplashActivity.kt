package com.alorma.myapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.myapplication.ui.movies.MoviesActivity
import com.alorma.presentation.common.ViewModelRoute
import com.alorma.presentation.splash.SplashActions
import com.alorma.presentation.splash.SplashRoutes
import com.alorma.presentation.splash.SplashViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(),
        ViewModelRoute<SplashRoutes.SplashRoute> {
    val actions: SplashActions by inject()

    val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashViewModel.observe(routeOwner = this)
        splashViewModel reduce actions.load()
    }

    private fun openMain() {
        startActivity(Intent(this, MoviesActivity::class.java)).also { finish() }
    }


    override fun onRoute(route: SplashRoutes.SplashRoute) {
        when (route) {
            is SplashRoutes.SplashRoute.Main -> openMain()
            is SplashRoutes.SplashRoute.Error -> openMain()
        }
    }
}