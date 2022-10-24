package com.shunan.targetswitch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.shunan.target_switch.TargetSwitchAnimListener
import com.shunan.targetswitch.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //val switch = findViewById<TargetSwitch>(R.id.targetSwitch)
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


    fun executeAfter(millis: Long, a: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Default) {
            delay(millis)
            launch(Dispatchers.Main) {
                a()
            }
        }
    }

}