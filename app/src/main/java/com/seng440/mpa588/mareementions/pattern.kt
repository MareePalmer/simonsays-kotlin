package com.seng440.mpa588.mareementions
import android.widget.Toast
import kotlin.random.Random

class Pattern(var buttonOrder: MutableList<Int>) {

    fun addButtonToPattern(){
        var buttonToAdd = colours.colourArray[Random.nextInt(4)]
        while(buttonOrder.size > 0 && buttonToAdd == buttonOrder.last()) {
            buttonToAdd = colours.colourArray[Random.nextInt(4)]
        }
        buttonOrder.add(buttonToAdd)
    }

    fun checkSameButtonPress(buttonId: Int, currentInd: Int) : Boolean {
        if (this.buttonOrder[currentInd] == buttonId) {
            return true
        }
        return false
    }

}