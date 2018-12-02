package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug

class QuKanHelper {
    /*
    2018-12-02 22:17:06.316 15280-15280/com.luowei.qukanhelper D/(QuKanAccessibilityService.kt:22)[onAccessibilityEvent]: Info: eventType: 32 getText :[趣头条]  getClassName:com.jifen.qkbase.main.MainActivity android.view.accessibility.AccessibilityNodeInfo@80006f09; boundsInParent: Rect(0, 0 - 1080, 1920); boundsInScreen: Rect(0, 0 - 1080, 1920); packageName: com.jifen.qukan; className: android.widget.FrameLayout; text: null; error: null; maxTextLength: -1; contentDescription: null; viewIdResName: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]
     */
    fun mainActivity() {

    }

    private fun mainActivityClickToDetailAcitivity() {

    }

    fun onAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent) {
        if (event.packageName != "com.jifen.qukan") return
        val eventType = event.eventType
        val accessibilityNodeInfo = service.rootInActiveWindow
        logDebug(
            "Info", " eventType: " + eventType +
                    " getText :" + event.text.toString()
                    + "  getClassName:" + event.className + " $accessibilityNodeInfo"
        )

        val recyclerView =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/nt").getOrNull(0)

        LayoutPrinter.printPacketInfo(service.rootInActiveWindow)
        if (event.className == "android.support.v7.widget.RecyclerView") {
        }
//            rootInActiveWindow.getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
        if (accessibilityNodeInfo != null) {
//            printPacketInfo(accessibilityNodeInfo)
        }


    }
}