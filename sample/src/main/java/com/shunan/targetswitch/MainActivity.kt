package com.shunan.targetswitch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.shunan.target_switch.TargetSwitchAnimListener
import com.shunan.targetswitch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.flag = true
        binding.foregroundTint = ContextCompat.getColor(applicationContext, R.color.purple_200)
        binding.backgroundTint = ContextCompat.getColor(applicationContext, R.color.teal_200)
        binding.duration = 500

        binding.targetSwitch.setAnimListener(object : TargetSwitchAnimListener {
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