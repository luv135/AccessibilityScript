package com.luowei.qukanhelper

import android.view.accessibility.AccessibilityEvent

interface IPage {
    fun matchPage(className: String): Boolean
    fun enter()
    fun leave()
    fun handleAccessibilityEvent(event: AccessibilityEvent)
}