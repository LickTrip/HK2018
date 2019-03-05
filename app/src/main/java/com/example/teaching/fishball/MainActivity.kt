package com.example.teaching.fishball

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.teaching.fishball.Model.MyConstants
import com.example.teaching.fishball.Services.BluetoothConn
import com.example.teaching.fishball.Services.MotorController
import com.example.teaching.fishball.Services.MotorHelper
import com.example.teaching.fishball.View.VertSeekBar
import com.example.teaching.fishball.View.VerticalSeekBar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.example.teaching.fishball.Services.VariableListener


class MainActivity : AppCompatActivity() {

    private lateinit var bConn: BluetoothConn
    private lateinit var motController: MotorController
    private lateinit var leftSeekBar: SeekBar
    private lateinit var rightSeekBar: SeekBar
    private lateinit var middleSeekBar: SeekBar
    private lateinit var leftSeekBarProgress: TextView
    private lateinit var rightSeekBarProgress: TextView
    private lateinit var middleSeekBarProgress: TextView
    private lateinit var lockSwitch: Switch
    private lateinit var stopButton: Button
    private lateinit var onlineListener: VariableListener
    private lateinit var progressDialog: ProgressDialog


    private var isCalibrate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bConn = BluetoothConn()
        motController = MotorController(bConn)

        leftSeekBar = leftMotorSeekBar as VertSeekBar
        rightSeekBar = rightMotorSeekBar as VertSeekBar
        middleSeekBar = middleMotorSeekBar
        leftSeekBarProgress = textViewLeftSeek
        rightSeekBarProgress = textViewRightSeek
        middleSeekBarProgress = textViewMiddleSeek
        lockSwitch = switch_lock
        stopButton = StopIt
        onlineListener = VariableListener()

        fab.setOnClickListener { view ->
            getConnection(view)
        }
        setSeekBars()

        leftSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val speed = MotorHelper.getSpeedPercent(p1)
                leftSeekBarProgress.setTextColor(MotorHelper.getProgressColor(p1))
                leftSeekBarProgress.text = "Left motor: $speed%"
                if (!lockSwitch.isChecked && !isCalibrate)
                    motController.initLeftMotor(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        rightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val speed = MotorHelper.getSpeedPercent(p1)
                rightSeekBarProgress.setTextColor(MotorHelper.getProgressColor(p1))
                rightSeekBarProgress.text = "Right motor: $speed%"

                if (lockSwitch.isChecked && !isCalibrate) {
                    leftSeekBar.progress = p1
                    motController.initBothMotors(p1)
                } else if (!isCalibrate) {
                    motController.initRightMotor(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        middleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val speed = MotorHelper.getSpeedPercent(p1)
                middleSeekBarProgress.setTextColor(MotorHelper.getProgressColor(p1))
                middleSeekBarProgress.text = "Middle motors: $speed%"
                if (!isCalibrate)
                    motController.initMiddleMotor(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        lockSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                lockSwitch.text = "LOCK"
                leftSeekBar.isEnabled = false
                leftSeekBar.progress = rightSeekBar.progress
            } else {
                lockSwitch.text = "UNLOCK"
                leftSeekBar.isEnabled = true
            }
        }

        onlineListener.setListener(object : VariableListener.ChangeListener {
            override fun onChange() {
                progressDialog.dismiss()
                if (onlineListener.isBoo())
                    onlineBaby()
                else
                    offlineMode()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_disconnect -> {
                bConn.disconnect()
                offlineMode()
                true
            }
            R.id.menu_exit -> {
                bConn.disconnect()
                offlineMode()
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getConnection(view: View) {
        callProgressDialog()
        val isConnStart = bConn.connect(this, MyConstants.MAC_ADDRESS, object : BluetoothConn.ConnectionChangedListener {
            override fun onConnectionChanged(connected: Boolean) {
                runOnUiThread {
                    if (!connected) {
                        onlineListener.setBoo(false)
                        Snackbar.make(view, "Disconnected", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show()
                    } else {
                        onlineListener.setBoo(true)
                        Snackbar.make(view, "Online baby", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show()
                    }
                }
            }
        })

        if (!isConnStart) {
            return
        }
    }

    fun stopMot(view: View) {
        if (motController.orderArduino(MyConstants.STOP_M)) {
            calibrateMot()
            Snackbar.make(view, "Motors are stopped", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        } else {
            Snackbar.make(view, "Stop Err", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }
    }

    @SuppressLint("ResourceAsColor")
    fun onlineBaby() {
        fab.backgroundTintList = ColorStateList.valueOf(Color
                .parseColor("#36b50c"))
        leftSeekBar.isEnabled = true
        rightSeekBar.isEnabled = true
        middleSeekBar.isEnabled = true
        lockSwitch.isEnabled = true
        stopButton.isEnabled = true
    }

    @SuppressLint("ResourceAsColor")
    fun offlineMode() {
        fab.backgroundTintList = ColorStateList.valueOf(Color
                .parseColor("#1c1d20"))
        leftSeekBar.isEnabled = false
        rightSeekBar.isEnabled = false
        middleSeekBar.isEnabled = false
        lockSwitch.isEnabled = false
        stopButton.isEnabled = false
    }

    private fun calibrateMot() {
        //TODO timer pro seekbar
        isCalibrate = true
        leftSeekBar.progress = 0
        leftSeekBar.max = 14
        leftSeekBar.progress = 7

        rightSeekBar.progress = 0
        rightSeekBar.max = 14
        rightSeekBar.progress = 7

        middleSeekBar.progress = 0
        middleSeekBar.max = 14
        middleSeekBar.progress = 7
        isCalibrate = false
    }


    private fun setSeekBars() {
        leftSeekBar.max = 14
        rightSeekBar.max = 14
        middleSeekBar.max = 14
        leftSeekBar.progress = 7
        rightSeekBar.progress = 7
        middleSeekBar.progress = 7
        leftSeekBar.isEnabled = false
        rightSeekBar.isEnabled = false
        middleSeekBar.isEnabled = false
    }

    private fun callProgressDialog() {
        progressDialog = ProgressDialog(this, R.style.ThemeMyProgressDialog)
        progressDialog.setMessage("Connecting..")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large)
        progressDialog.show()
    }
}


