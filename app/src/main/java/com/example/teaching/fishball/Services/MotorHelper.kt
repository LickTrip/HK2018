package com.example.teaching.fishball.Services

import android.graphics.Color
import android.graphics.Color.*
import com.example.teaching.fishball.Model.MyConstants

/**
 * Created by teaching on 7/26/2018.
 */

class MotorHelper {
    companion object {
        fun getMyByteArr(motor: String, dir: String, speed: String): Byte {
            val finalString = "0" + motor + speed + dir
            return Integer.parseInt(finalString, 2).toByte()
        }

        fun getMyByteArr(order: String): Byte {
            val finalString = "0" + order + "0000"
            return Integer.parseInt(finalString, 2).toByte()
        }

        fun translateMotorProgress(progress: Int, middle: Boolean): Array<String?> {
            val zero = 7
            val myArr = arrayOfNulls<String>(2) // Arr[speed, direction]
            myArr[0] = translateSpeed(progress)
            when {
                zero <= progress -> {
                    if (middle)
                        myArr[1] = MyConstants.CLCK_DIR
                    else
                        myArr[1] = MyConstants.C_CLCK_DIR
                }
                else -> {
                    if (middle)
                        myArr[1] = MyConstants.C_CLCK_DIR
                    else
                        myArr[1] = MyConstants.CLCK_DIR
                }
            }
            return myArr
        }

        fun translateSpeed(speed: Int): String {
            return when (speed) {
                7 -> MyConstants.SPEED0
                6, 8 -> MyConstants.SPEED1
                5, 9 -> MyConstants.SPEED2
                4, 10 -> MyConstants.SPEED3
                3, 11 -> MyConstants.SPEED4
                2, 12 -> MyConstants.SPEED5
                1, 13 -> MyConstants.SPEED6
                0, 14 -> MyConstants.SPEED7
                else -> {
                    MyConstants.SPEED0
                }
            }
        }

        fun getSpeedPercent(speed: Int): Int {
            return when (speed) {
                7 -> 0
                6, 8 -> 14
                5, 9 -> 28
                4, 10 -> 42
                3, 11 -> 56
                2, 12 -> 70
                1, 13 -> 84
                0, 14 -> 100
                else -> {
                    0
                }
            }
        }

        fun getProgressColor(speed: Int): Int {
            return when (speed) {
                7 -> WHITE
                8, 9, 10, 11, 12, 13, 14 -> GREEN
                6, 5, 4, 3, 2, 1, 0 -> RED
                else -> WHITE
            }
        }
    }


}