package com.luowei.script.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import com.luowei.script.program.qukanhelper.QuKanHelper

class AccessibilityScriptService : AccessibilityService() {
    private val qukanHelper = QuKanHelper(this)

    override fun onInterrupt() {
        logDebug("inter")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        logDebug("connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType = event.eventType
        val accessibilityNodeInfo = rootInActiveWindow
        val className = event.className
//        logDebug(
//            " eventType: " + AccessibilityEvent.eventTypeToString(eventType) +
//                    " getText :" + event.text.toString()
//                    + "  getClassName:" + className
//        )
//        LayoutInspector.packagetLayoutInfo(accessibilityNodeInfo).logDebug()
        qukanHelper.onAccessibilityEvent(event)
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            disableSelf()
        }
        return super.onKeyEvent(event)
    }
}