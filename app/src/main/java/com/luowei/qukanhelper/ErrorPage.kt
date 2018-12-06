package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ErrorPage(val service: AccessibilityService) : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qukan.imagenews.ImageNewsDetailActivity"
    }

    override fun enter() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    override fun leave() {
    }

    override fun handleAccessibilityEvent(event: AccessibilityEvent) {
    }
}