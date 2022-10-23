package com.shunan.targetswitch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shunan.TargetSwitch.TargetSwitch
import com.shunan.TargetSwitch.TargetSwitchAnimListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switch = findViewById<TargetSwitch>(R.id.targetSwitch)
        switch.setAnimListener(object : TargetSwitchAnimListener {
            override fun onAnimStart() {
                Toast.makeText(applicationContext, "onAnimStart()", Toast.LENGTH_SHORT).show()

            }

            override fun onAnimEnd() {
                Toast.makeText(applicationContext, "onAnimEnd()", Toast.LENGTH_SHORT).show()
            }

            override fun onAnimValueChanged(value: Float) {
            }

        })
    }
}