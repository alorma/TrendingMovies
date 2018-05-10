package com.alorma.myapplication.config

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener

class DexOpenerAndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        DexOpener.builder(context)
                .buildConfig(com.alorma.myapplication.BuildConfig::class.java) // Set the BuildConfig class
                .build()
                .installTo(cl)
        return super.newApplication(cl, className, context)
    }
}