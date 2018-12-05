package com.luowei.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import com.luowei.qukanhelper.QuKanHelper

class QuKanAccessibilityService : AccessibilityService() {
    private val qukanHelper = QuKanHelper()

    override fun onInterrupt() {
        logDebug("inter")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        if(event.packageName=="com.android.launcher3") {
        qukanHelper.onAccessibilityEvent(this, event)
    }

    private var tabcount = -1
    private var sb: StringBuilder? = null


}