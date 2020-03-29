package com.puddlealley.flux.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.puddlealley.flux.R

/**
 * Screen that is shown after we login
 */
class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
    }

    companion object{
        fun newIntent(context: Context): Intent {
            return Intent(context, DeviceActivity::class.java)
        }
    }

}
