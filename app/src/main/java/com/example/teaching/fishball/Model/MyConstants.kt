package com.example.teaching.fishball.Model

/**
 * Created by teaching on 7/24/2018.
 */
 class MyConstants {
     companion object {
        /*3 BITS*/
        const val SPEED0 = "000"
        const val SPEED1 = "001"
        const val SPEED2 = "010"
        const val SPEED3 = "011"
        const val SPEED4 = "100"
        const val SPEED5 = "101"
        const val SPEED6 = "110"
        const val SPEED7 = "111"

        /*3 BITS*/
        const val LEFT_M = "000"
        const val RIGHT_M = "001"
        const val MIDDLE_M = "010"
        const val STOP_M = "100"
        const val START_M = "011"
        const val BOTH_M = "101"

        /*1 BIT*/
        const val CLCK_DIR = "0"
        const val C_CLCK_DIR = "1"

        /*MAC ADDRESS*/
        const val MAC_ADDRESS: String = "20:91:48:BC:D8:F1"
    }
}