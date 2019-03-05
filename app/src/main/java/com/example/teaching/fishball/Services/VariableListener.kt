package com.example.teaching.fishball.Services

/**
 * Created by teaching on 7/30/2018.
 */
class VariableListener {
    private var boo = false
    private var listener: ChangeListener? = null

    fun isBoo(): Boolean {
        return boo
    }

    fun setBoo(boo: Boolean) {
        this.boo = boo
        if (listener != null) listener!!.onChange()
    }

    fun getListener(): ChangeListener? {
        return listener
    }

    fun setListener(listener: ChangeListener) {
        this.listener = listener
    }

    interface ChangeListener {
        fun onChange()
    }
}