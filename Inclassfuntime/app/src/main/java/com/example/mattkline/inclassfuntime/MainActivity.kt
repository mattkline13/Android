package com.example.mattkline.inclassfuntime

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.example.mattkline.inclassfuntime.R.color.colorAccent
import com.example.mattkline.inclassfuntime.R.color.colorPrimary
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    fun toggleVisibility(view: View) {
        if(textVisibility.isChecked())
            description.visibility = INVISIBLE
        else
            description.visibility = VISIBLE
    }

    fun toggleBackColor(view: View) {
        if(backgroundColor.isChecked())
            description.setBackgroundColor(getResources().getColor(colorPrimary))
        else
            description.setBackgroundColor(getResources().getColor(colorAccent))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        description.visibility = VISIBLE
        return when (item.itemId) {
            R.id.action_help -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
