package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ErrorPage : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qukan.imagenews.ImageNewsDetailActivity"
    }

    override fun enter(service: AccessibilityService) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    override fun leave(service: AccessibilityService) {
    }

    override fun handleAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        return true
    }
}