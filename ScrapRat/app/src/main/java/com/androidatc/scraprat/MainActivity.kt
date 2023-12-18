package com.androidatc.scraprat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.androidatc.scraprat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadSharedPreferences()
        loadName()

        binding.playerNameInput.addTextChangedListener {
            saveName()
        }
    }

    fun playBTNclick(view: View) {
        var intent = Intent(this, SecondaryActivity::class.java)
            .putExtra("ratName", binding.playerNameInput.text.toString())

        startActivity(intent)
    }

    fun settingsBTNclick(view: View) {
        var intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    fun aboutBTNclick(view: View) {
        var intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun loadName() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        var ratName = sharedPreferences.getString("RAT_NAME", "Rat")
        binding.playerNameInput.setText(ratName)
    }

    fun saveName() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString("RAT_NAME", binding.playerNameInput.text.toString())
        }.apply()
    }
    // =================== Preferences ================================
    private fun loadSharedPreferences() {
        fun setRatColor(color: String?) {
            val image = findViewById<ImageView>(R.id.splash_image)
            val rat = "rat$color"
            val imageResource = resources.getIdentifier(rat,
                "drawable", packageName)
            image.setImageResource(imageResource)
        }

        fun setLayoutBackgroundColor(colorResId: Int) {
            val layout = findViewById<ConstraintLayout>(R.id.coordinatorLayout)
            val color = ContextCompat.getColor(this, colorResId)
            layout.setBackgroundColor(color)
        }

        fun setButtonColor(colorResId: Int) {
            val playBTN = findViewById<Button>(R.id.button_play)
            val settingsBTN = findViewById<Button>(R.id.button_settings)
            val aboutBTN = findViewById<Button>(R.id.button_about)
            val color = ContextCompat.getColor(this, colorResId)
            // Set background color for Buttons
            settingsBTN.setBackgroundColor(color)
            aboutBTN.setBackgroundColor(color)
            playBTN.setBackgroundColor(color)
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
        val RAcolor = sharedPreferences.getString("RAT_COLOR", "green")
        val BGcolor = sharedPreferences.getString("BACKGROUND_COLOR", "teal_desaturated")
        val BUcolor = sharedPreferences.getString("BUTTON_COLOR", "purple")
        setRatColor(RAcolor)
        setLayoutBackgroundColor(getColorCode(BGcolor))
        setButtonColor(getColorCode(BUcolor))
    }
}