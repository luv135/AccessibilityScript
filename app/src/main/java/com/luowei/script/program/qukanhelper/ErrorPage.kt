package com.luowei.script.program.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import com.luowei.script.program.IPage

class ErrorPage(val service: AccessibilityService) : IPage {
    override fun matchPage(className: String): Boolean {
        logDebug("error page $className")
        return true
//        return className == "com.jifen.qukan.imagenews.ImageNewsDetailActivity"
    }

    override fun enter() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    override fun leave() {
    }

    override fun handleAccessibilityEvent(event: AccessibilityEvent) {
    }
}