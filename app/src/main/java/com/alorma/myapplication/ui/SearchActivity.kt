package com.alorma.myapplication.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }

}