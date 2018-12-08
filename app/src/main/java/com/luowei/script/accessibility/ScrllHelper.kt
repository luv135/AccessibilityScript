package com.luowei.script.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.Point
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent

class ScrllHelper {

    private var success = false

    fun watch(service: AccessibilityService, event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

        }
    }

    fun scrollDown(
        service: AccessibilityService,
        callback: AccessibilityService.GestureResultCallback? = null
    ): Boolean {
        val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val length = 50
        val x1 = size.x / 2f
        val x2 = size.x / 2f
        val y1 = size.y / 2f + length
        val y2 = size.y / 2f - length
        val path = Path()
        path.apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
        }
        val gestureDescription =
            GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 10, 1000))
                .build()
        return service.dispatchGesture(gestureDescription, callback, null)
    }
}