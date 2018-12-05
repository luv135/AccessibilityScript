package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

interface IPage {
    fun matchPage(className: String): Boolean
    fun enter(service: AccessibilityService)
    fun leave(service: AccessibilityService)
    fun handleAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent): Boolean
}