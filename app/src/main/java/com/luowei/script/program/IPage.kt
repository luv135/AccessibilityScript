package com.luowei.script.program

import android.view.accessibility.AccessibilityEvent

interface IPage {
    fun matchPage(className: String): Boolean
    fun enter()
    //    fun pasue()
//    fun resum()
    fun leave()
    fun handleAccessibilityEvent(event: AccessibilityEvent)
}