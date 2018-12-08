package com.luowei.script

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luowei.script.accessibility.AccessibilityScriptService
import com.luowei.script.accessibility.AccessibilityServiceUtils
import com.luowei.script.program.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!AccessibilityServiceUtils.isAccessibilityServiceEnabled(this, AccessibilityScriptService::class.java)) {
            AccessibilityServiceUtils.goToAccessibilitySetting(this)
        }
        finish()
    }
}
