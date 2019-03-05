package com.example.teaching.fishball

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View

class WelcomeActivity : AppCompatActivity() {

    private var mBlueToothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val REQUEST_ENABLE_BT: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val secondsDelayed: Long = 1
        Handler().postDelayed({
            testBT()
        }, secondsDelayed * 750)
    }

    fun welcomeClick(view: View) {
        //testBT()
    }

    private fun testBT() {
        if (mBlueToothAdapter == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Not Supported")
            builder.setMessage("This device not supporting bluetooth communication")
            builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int ->
                finish()
            })
            builder.show()
        } else {
            if (!mBlueToothAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BT)
            }else{
                callMainActivity()
            }
        }
    }

    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                callMainActivity()
            } else if ((resultCode == Activity.RESULT_CANCELED)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Bluetooth")
                builder.setMessage("Bluetooth must be turn on for communication")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int ->
                    finish()
                })
                builder.show()
            }
        }
    }

    private fun callMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
