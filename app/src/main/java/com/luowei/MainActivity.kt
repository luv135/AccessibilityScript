package com.luowei

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luowei.accessibility.AccessibilityServiceUtils
import com.luowei.qukanhelper.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!AccessibilityServiceUtils.isAccessibilityServiceEnabled(this, AccessibilityService::class.java)) {
            AccessibilityServiceUtils.goToAccessibilitySetting(this)
        }
        finish()
    }
}
