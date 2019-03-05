package com.example.teaching.fishball.Services

import android.view.View
import com.example.teaching.fishball.Model.MyConstants

/**
 * Created by teaching on 7/24/2018.
 */
class MotorController(private val bConn: BluetoothConn) {

    fun startMotor(dir: String, speed: String, motor: String): Boolean {
        val bArr = ByteArray(1)
        bArr[0] = MotorHelper.getMyByteArr(motor, dir, speed)
        return bConn.sendData(bArr)
    }

    fun orderArduino(order: String): Boolean {
        val bArr = ByteArray(1)
        bArr[0] = MotorHelper.getMyByteArr(order)
        return bConn.sendData(bArr)
    }

    fun initLeftMotor(progress : Int){
        val myArr = MotorHelper.translateMotorProgress(progress, false)
        startMotor(myArr[1].toString(), myArr[0].toString(), MyConstants.LEFT_M)
    }

    fun initRightMotor(progress : Int){
        val myArr = MotorHelper.translateMotorProgress(progress, false)
        startMotor(myArr[1].toString(), myArr[0].toString(), MyConstants.RIGHT_M)
    }

    fun initMiddleMotor(progress : Int){
        val myArr = MotorHelper.translateMotorProgress(progress, true)
        startMotor(myArr[1].toString(), myArr[0].toString(), MyConstants.MIDDLE_M)
    }

    fun initBothMotors(progress: Int){
        val myArr = MotorHelper.translateMotorProgress(progress, false)
        startMotor(myArr[1].toString(), myArr[0].toString(), MyConstants.BOTH_M)
    }


}