package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class DialogPage(val service: AccessibilityService) : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "android.app.AlertDialog"
    }

    override fun enter() {
    }

    private fun requestPermission(event: AccessibilityEvent) {
        if (event.text.toString().contains("提示, 系统检测到应用缺少必要权限")) {
            val accessibilityNodeInfo = service.rootInActiveWindow
            accessibilityNodeInfo ?: return
            accessibilityNodeInfo.findAccessibilityNodeInfosByText("忽略").getOrNull(0)?.performAction(
                AccessibilityNodeInfo.ACTION_CLICK
            )
        }
    }

    override fun leave() {
    }

    override fun handleAccessibilityEvent(event: AccessibilityEvent) {
        requestPermission(event)
    }
}