package com.androidatc.scraprat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        loadSharedPreferences()
    }

    fun homeBTNclick(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // =================== Preferences ================================
    private fun loadSharedPreferences() {
        fun setLayoutBackgroundColor(colorResId: Int) {
            val layout = findViewById<ConstraintLayout>(R.id.helpback)
            val color = ContextCompat.getColor(this, colorResId)
            layout.setBackgroundColor(color)
        }

        fun setButtonColor(colorResId: Int) {
            val home = findViewById<ImageButton>(R.id.imageButton_home)
            val color = ContextCompat.getColorStateList(this, colorResId)
            // Set background tint for ImageButton (homeBTN)
            home.setBackgroundTintList(color)
        }

        fun getColorCode(string:String?) :Int {
            when (string) {
                "pink"      -> return R.color.pink
                "red"       -> return R.color.red
                "orange"    -> return R.color.orange
                "yellow"    -> return R.color.yellow
                "green"     -> return R.color.green
                "teal"      -> return R.color.teal
                "blue"      -> return R.color.blue
                "purple"    -> return R.color.purple
                "pink_desaturated"      -> return R.color.pink_desaturated
                "red_desaturated"       -> return R.color.red_desaturated
                "orange_desaturated"    -> return R.color.orange_desaturated
                "yellow_desaturated"    -> return R.color.yellow_desaturated
                "green_desaturated"     -> return R.color.green_desaturated
                "teal_desaturated"      -> return R.color.teal_desaturated
                "blue_desaturated"      -> return R.color.blue_desaturated
                "purple_desaturated"    -> return R.color.purple_desaturated
            }
            return R.color.green_desaturated
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        val BGcolor = sharedPreferences.getString("BACKGROUND_COLOR", "teal_desaturated")
        val BUcolor = sharedPreferences.getString("BUTTON_COLOR", "purple")
        setLayoutBackgroundColor(getColorCode(BGcolor))
        setButtonColor(getColorCode(BUcolor))
    }
}