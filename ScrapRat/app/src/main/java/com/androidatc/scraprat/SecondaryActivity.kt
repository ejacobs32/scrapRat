package com.androidatc.scraprat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.androidatc.scraprat.databinding.ActivitySecondaryBinding
import kotlin.random.Random

class SecondaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondaryBinding
    private val handler = Handler(Looper.getMainLooper())
    private val interval = 1000L // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadSharedPreferences()

        // Start calling your function every 2 seconds
        startPeriodicFunctionCall()
        // Add your name Taken from the Main activity
        var ratName = intent.getStringExtra("ratName")
        if (ratName == "") { ratName = "Rat"}
        binding.textViewNetWorth.text = "${ratName}'s Net Worth"

        binding.buttonSalvege.setOnClickListener { onClickSalvage() }
        binding.selljunk1.setOnClickListener { onClickSellJunk(1) }
        binding.selljunk2.setOnClickListener { onClickSellJunk(2) }
        binding.selljunk3.setOnClickListener { onClickSellJunk(3) }
        binding.buyjunk1.setOnClickListener { onClickBuyJunk(1) }
        binding.buyjunk2.setOnClickListener { onClickBuyJunk(2) }
        binding.buyjunk3.setOnClickListener { onClickBuyJunk(3) }
    }


    private fun startPeriodicFunctionCall() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Call your function here
                timePasses()

                // Schedule the next call after the specified interval
                handler.postDelayed(this, interval)
            }
        }, interval)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the periodic function calls when the activity is destroyed
        handler.removeCallbacksAndMessages(null)
    }


    fun homeBTNclick(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    
    
    // ====================== Game Functions ================================
    private var gameTicks: Int = 0
    private var hoard = mutableListOf<TrashItem>()
    private var buyList = mutableListOf<TrashItem>()
    private var sellList = mutableListOf<TrashItem>()
    private var netWorth = 0

    private fun timePasses() { // Handles time passing to call other functions
        gameTicks += 1
        if (gameTicks == 10) {
            gameTicks = 0
            binding.buttonSalvege.isEnabled = true
            loadSharedPreferences()
            saleOfferChange()
        }
        fillBuySlots()
        fillSellSlots()
        updateSlots()
    }

    private fun saleOfferChange() {

        fun getItem(list:List<TrashItem>): TrashItem {
            val randomNum = Random.nextInt(0, list.size)
            return list[randomNum]
        }
        var item: TrashItem? = null

        if (hoard.size > 0 && sellList.size > 0) {
            val coinFlip = Random.nextInt(0, 1)
            if (coinFlip == 0) {
                item = getItem(sellList)
            } else {
                item = getItem(hoard)
            }
        } else if (sellList.size > 0) {
            item = getItem(sellList)
        } else if (hoard.size > 0) {
            item = getItem(hoard)
        } else {
            return
        }

        item!!.sellPrice = Random.nextInt(0, 100)
    }

    private fun fillBuySlots() {
        val randomPrice = Random.nextInt(0, 100)
        val newTrash = TrashItem(randomPrice)
        newTrash.generate()

        buyList.add(0, newTrash)
        if (buyList.size > 3) {
            buyList.removeAt(3)
        }
    }

    private fun fillSellSlots() {
        if (hoard.size != 0) {
            val randValue = Random.nextInt(0, hoard.size)
            sellList.add(0, hoard[randValue])
            hoard.removeAt(randValue)
        }
        try {
            if (sellList.size > 3) {
                hoard.add(0, sellList[3])
                sellList.removeAt(3)
            }
        } catch (e: Exception) {}
    }

    private fun updateSlots() {
        try {
            changeWhatsInSlot(buyList[0], "buy1")
            changeWhatsInSlot(buyList[1], "buy2")
            changeWhatsInSlot(buyList[2], "buy3")
            changeWhatsInSlot(sellList[0], "sell1")
            changeWhatsInSlot(sellList[1], "sell2")
            changeWhatsInSlot(sellList[2], "sell3")
        } catch (e: Exception) {}

        clearEmptySlots()
        binding.hoardcount.text = (hoard.size + sellList.size).toString()
    }

    private fun clearEmptySlots() {
        val imageResource = resources.getIdentifier("trash0",
            "drawable", packageName)

        if (buyList.size < 1) {
            binding.buyjunk1.setImageResource(imageResource)
            binding.buyjunkPrice1.text = ""
        }
        if (buyList.size < 2) {
            binding.buyjunk2.setImageResource(imageResource)
            binding.buyjunkPrice2.text = ""
        }
        if (buyList.size < 3) {
            binding.buyjunk3.setImageResource(imageResource)
            binding.buyjunkPrice3.text = ""
        }
        if (sellList.size < 1) {
            binding.selljunk1.setImageResource(imageResource)
            binding.selljunkPrice1.text = ""
        }
        if (sellList.size < 2) {
            binding.selljunk2.setImageResource(imageResource)
            binding.selljunkPrice2.text = ""
        }
        if (sellList.size < 3) {
            binding.selljunk3.setImageResource(imageResource)
            binding.selljunkPrice3.text = ""
        }
    }

    private fun onClickSalvage() {
        binding.buttonSalvege.isEnabled = false
        binding.buttonSalvege.setBackgroundColor(ContextCompat.getColor(this, R.color.missing))

        val item = TrashItem(0)
        item.generate()
        hoard.add(item)
    }

    private fun onClickSellJunk(num:Int) {
        if (sellList.size < num) {return}

        val item = sellList[num - 1]
        sellList.removeAt(num - 1)
        netWorth += item.sellPrice
        binding.networth.text = "¢${netWorth}"
        updateSlots()
    }

    private fun onClickBuyJunk(num:Int) {
        if (buyList.size < num) {return}
        val item = buyList[num - 1]
        if (item.buyPrice > netWorth) {return}

        buyList.removeAt(num - 1)
        hoard.add(0,item)
        netWorth -= item.buyPrice
        binding.networth.text = "¢${netWorth}"
        updateSlots()
    }

    private fun changeWhatsInSlot(trash:TrashItem, slot:String) {

        var button = binding.buyjunk1
        var textDisplay   = binding.buyjunkPrice1

        when (slot) {
            "sell1" -> {button      = binding.selljunk1
                        textDisplay = binding.selljunkPrice1 }
            "sell2" -> {button      = binding.selljunk2
                        textDisplay = binding.selljunkPrice2 }
            "sell3" -> {button      = binding.selljunk3
                        textDisplay = binding.selljunkPrice3 }
            "buy1"  -> {button      = binding.buyjunk1
                        textDisplay = binding.buyjunkPrice1 }
            "buy2"  -> {button      = binding.buyjunk2
                        textDisplay = binding.buyjunkPrice2 }
            "buy3"  -> {button      = binding.buyjunk3
                        textDisplay = binding.buyjunkPrice3 }
        }


        val imageResource = resources.getIdentifier(trash.image, "drawable", packageName)
        button.setImageResource(imageResource)

        if (slot.substring(0, 3) == "buy") {
            textDisplay.text = "¢${trash.buyPrice}"
        } else {
            textDisplay.text = "¢${trash.sellPrice}"
            // Change text color if it profitable
            if (trash.sellPrice > trash.buyPrice ) {
                textDisplay.setTextColor(ContextCompat.getColor(this, R.color.green))
            } else {
                textDisplay.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }



    }

    // Class for generating trash
    class TrashItem (buyPrice:Int, sellPrice:Int=0,  image:String="rat3") {
        var buyPrice  = buyPrice
        var sellPrice = sellPrice
        var image     = image

        fun generate() {
            this.sellPrice = Random.nextInt(0, 100)
            this.image     = "trash" + Random.nextInt(1, 12).toString()
        }
    }



    // ====================== Preferences ================================
    private fun loadSharedPreferences() {
        fun setRatColor(color: String?) {
            val image = findViewById<ImageView>(R.id.splash_image)
            val rat = "rat$color"
            val imageResource = resources.getIdentifier(rat,
                "drawable", packageName)
            image.setImageResource(imageResource)
        }

        fun setLayoutBackgroundColor(colorResId: Int) {
            val layout = findViewById<ConstraintLayout>(R.id.gameBack)
            val color = ContextCompat.getColor(this, colorResId)
            layout.setBackgroundColor(color)
        }

        fun setButtonColor(colorResId: Int) {
            val salvegeBTN = findViewById<Button>(R.id.button_salvege)
            val home = findViewById<ImageButton>(R.id.imageButton_home)
            val buyjunk1 = findViewById<ImageButton>(R.id.buyjunk1)
            val buyjunk2 = findViewById<ImageButton>(R.id.buyjunk2)
            val buyjunk3 = findViewById<ImageButton>(R.id.buyjunk3)
            val selljunk1 = findViewById<ImageButton>(R.id.selljunk1)
            val selljunk2 = findViewById<ImageButton>(R.id.selljunk2)
            val selljunk3 = findViewById<ImageButton>(R.id.selljunk3)

            val color = ContextCompat.getColorStateList(this, colorResId)
            // Set background color for Buttons
            salvegeBTN.setBackgroundColor(ContextCompat.getColor(this, colorResId))
            // Set background tint for ImageButton (homeBTN)
            home.setBackgroundTintList(color)
            buyjunk1.setBackgroundTintList(color)
            buyjunk2.setBackgroundTintList(color)
            buyjunk3.setBackgroundTintList(color)
            selljunk1.setBackgroundTintList(color)
            selljunk2.setBackgroundTintList(color)
            selljunk3.setBackgroundTintList(color)
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