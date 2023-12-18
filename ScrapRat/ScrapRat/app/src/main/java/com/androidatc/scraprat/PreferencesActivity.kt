package com.androidatc.scraprat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.androidatc.scraprat.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        loadSharedPreferences()
        checkButtonsAtStart()

        binding.ratColors.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_RCR  -> savePreferences("red",    "RAT_COLOR")
                R.id.radioButton_RCO  -> savePreferences("orange", "RAT_COLOR")
                R.id.radioButton_RCY  -> savePreferences("yellow", "RAT_COLOR")
                R.id.radioButton_RCG  -> savePreferences("green",  "RAT_COLOR")
                R.id.radioButton_RCT  -> savePreferences("teal",   "RAT_COLOR")
                R.id.radioButton_RCB  -> savePreferences("blue",   "RAT_COLOR")
                R.id.radioButton_RCPr -> savePreferences("purple", "RAT_COLOR")
                R.id.radioButton_RCPi -> savePreferences("pink",   "RAT_COLOR")
            }
        }
        binding.backgroundColors.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_BGCR  -> savePreferences("red_desaturated",    "BACKGROUND_COLOR")
                R.id.radioButton_BGCO  -> savePreferences("orange_desaturated", "BACKGROUND_COLOR")
                R.id.radioButton_BGCY  -> savePreferences("yellow_desaturated", "BACKGROUND_COLOR")
                R.id.radioButton_BGCG  -> savePreferences("green_desaturated",  "BACKGROUND_COLOR")
                R.id.radioButton_BGCT  -> savePreferences("teal_desaturated",   "BACKGROUND_COLOR")
                R.id.radioButton_BGCB  -> savePreferences("blue_desaturated",   "BACKGROUND_COLOR")
                R.id.radioButton_BGCPr -> savePreferences("purple_desaturated", "BACKGROUND_COLOR")
                R.id.radioButton_BGCPi -> savePreferences("pink_desaturated",   "BACKGROUND_COLOR")
            }
        }
        binding.buttonColors.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonBCR  -> savePreferences("red",      "BUTTON_COLOR")
                R.id.radioButtonBCO  -> savePreferences("orange",   "BUTTON_COLOR")
                R.id.radioButtonBCY  -> savePreferences("yellow",   "BUTTON_COLOR")
                R.id.radioButtonBCG  -> savePreferences("green",    "BUTTON_COLOR")
                R.id.radioButtonBCT  -> savePreferences("teal",     "BUTTON_COLOR")
                R.id.radioButtonBCB  -> savePreferences("blue",     "BUTTON_COLOR")
                R.id.radioButtonBCPr -> savePreferences("purple",   "BUTTON_COLOR")
                R.id.radioButtonBCPi -> savePreferences("pink",     "BUTTON_COLOR")
            }
        }
    }

    private fun savePreferences(color:String, type:String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString(type, color)
        }.apply()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
        fun setRatColor(color: String?) {
            val image = findViewById<ImageView>(R.id.imageView)
            val rat = "rat$color"
            val imageResource = resources.getIdentifier(rat,
                "drawable", packageName)
            image.setImageResource(imageResource)
        }

        fun setLayoutBackgroundColor(colorResId: Int) {
            val layout = findViewById<ConstraintLayout>(R.id.constraintlayoutPreferences)
            val color = ContextCompat.getColor(this, colorResId)
            layout.setBackgroundColor(color)
        }

        fun setButtonColor(colorResId: Int) {
            val homeBTN = findViewById<ImageButton>(R.id.imageButton_home)
            val color = ContextCompat.getColorStateList(this, colorResId)
            homeBTN.setBackgroundTintList(color)
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


    fun homeBTNclick(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun checkButtonsAtStart() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        val RAcolor = sharedPreferences.getString("RAT_COLOR", "green")
        val BGcolor = sharedPreferences.getString("BACKGROUND_COLOR", "teal_desaturated")
        val BUcolor = sharedPreferences.getString("BUTTON_COLOR", "purple")


        when (RAcolor) {
            "red"       -> binding.radioButtonRCR.isChecked  = true
            "orange"    -> binding.radioButtonRCO.isChecked  = true
            "yellow"    -> binding.radioButtonRCY.isChecked  = true
            "green"     -> binding.radioButtonRCG.isChecked  = true
            "teal"      -> binding.radioButtonRCT.isChecked  = true
            "blue"      -> binding.radioButtonRCB.isChecked  = true
            "purple"    -> binding.radioButtonRCPr.isChecked = true
            "pink"      -> binding.radioButtonRCPi.isChecked = true
        }
        when (BGcolor) {
            "red_desaturated"       -> binding.radioButtonBGCR.isChecked  = true
            "orange_desaturated"    -> binding.radioButtonBGCO.isChecked  = true
            "yellow_desaturated"    -> binding.radioButtonBGCY.isChecked  = true
            "green_desaturated"     -> binding.radioButtonBGCG.isChecked  = true
            "teal_desaturated"      -> binding.radioButtonBGCT.isChecked  = true
            "blue_desaturated"      -> binding.radioButtonBGCB.isChecked  = true
            "purple_desaturated"    -> binding.radioButtonBGCPr.isChecked = true
            "pink_desaturated"      -> binding.radioButtonBGCPi.isChecked = true
        }
        when (BUcolor) {
            "red"       -> binding.radioButtonBCR.isChecked = true
            "orange"    -> binding.radioButtonBCO.isChecked = true
            "yellow"    -> binding.radioButtonBCY.isChecked = true
            "green"     -> binding.radioButtonBCG.isChecked = true
            "teal"      -> binding.radioButtonBCT.isChecked = true
            "blue"      -> binding.radioButtonBCB.isChecked = true
            "purple"    -> binding.radioButtonBCPr.isChecked = true
            "pink"      -> binding.radioButtonBCPi.isChecked = true
        }
    }
}
