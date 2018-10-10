package com.alorma.myapplication.ui.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.VerificationModes
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.hamcrest.Matcher

fun getGenericResult(): Instrumentation.ActivityResult = Instrumentation.ActivityResult(2, Intent())

inline fun <reified T : Activity> getMatcherActivity(): Matcher<Intent> =
        IntentMatchers.hasComponent(T::class.java.name)

inline fun <reified T: Activity> intending() {
    Intents.intending(getMatcherActivity<T>()).respondWith(getGenericResult())
}

inline fun <reified T : Activity> intended(times: Int? = null) {
    val matcherActivity = getMatcherActivity<T>()
    times?.let {
        Intents.intended(matcherActivity, VerificationModes.times(times))
    } ?: Intents.intended(matcherActivity)
}