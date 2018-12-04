package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.luowei.logwherelibrary.logDebug
import java.util.*

class QuKanHelper {


    private val windowMap = HashMap<String, (service: AccessibilityService, event: AccessibilityEvent) -> Unit>()

    companion object {
        const val MAIN_ACTIVITY = "com.jifen.qkbase.main.MainActivity"
        const val NEWSDETAILACTIVITY = "com.jifen.qukan.newsdetail.news.NewsDetailActivity "
    }

    private val workStack = Stack<String>()

    init {
//        windowMap[MAIN_ACTIVITY] = { service, event -> jumpToDetail(service, event) }

    }

    /*
    2018-12-02 22:17:06.316 15280-15280/com.luowei.qukanhelper D/(QuKanAccessibilityService.kt:22)[onAccessibilityEvent]: Info: eventType: 32 getText :[趣头条]  getClassName:com.jifen.qkbase.main.MainActivity android.view.accessibility.AccessibilityNodeInfo@80006f09; boundsInParent: Rect(0, 0 - 1080, 1920); boundsInScreen: Rect(0, 0 - 1080, 1920); packageName: com.jifen.qukan; className: android.widget.FrameLayout; text: null; error: null; maxTextLength: -1; contentDescription: null; viewIdResName: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]
     */
    private fun jumpToDetail(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        val accessibilityNodeInfo = service.rootInActiveWindow
        accessibilityNodeInfo ?: return false
        val recyclerView =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/nt").getOrNull(0)
        recyclerView ?: return false
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChild(i) ?: continue
            if (child.findAccessibilityNodeInfosByText("广告").isEmpty() && child.findAccessibilityNodeInfosByText("置顶").isEmpty()) {
//                LayoutInspector.printPacketInfo(child)
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            }
        }
        return false


    }

    private fun requestPermission(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        val className = event.className
        if ("android.app.AlertDialog" == className) {
            if (event.text.toString().contains("提示, 系统检测到应用缺少必要权限")) {
                val accessibilityNodeInfo = service.rootInActiveWindow
                accessibilityNodeInfo ?: return false
                accessibilityNodeInfo.findAccessibilityNodeInfosByText("忽略").getOrNull(0)?.performAction(
                    AccessibilityNodeInfo.ACTION_CLICK
                )
                return true
            }
        }
        return false
    }

    private fun mainActivityClickToDetailAcitivity() {

    }

    private var function: ((service: AccessibilityService, event: AccessibilityEvent) -> Unit)? = null

    private var isDetail = false

    fun onAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent) {
        if (event.packageName != "com.jifen.qukan") return
        val eventType = event.eventType
        val accessibilityNodeInfo = service.rootInActiveWindow
        val className = event.className
        logDebug(
            "Info", " eventType: " + AccessibilityEvent.eventTypeToString(eventType) +
                    " getText :" + event.text.toString()
                    + "  getClassName:" + className + " accessibilityNodeInfo:$accessibilityNodeInfo"
        )
        when (eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                requestPermission(service, event)
                when (className) {
                    MAIN_ACTIVITY -> if (workStack.isEmpty()) {
                        if (jumpToDetail(service, event)) {
                        }
                    }
                    NEWSDETAILACTIVITY -> {
                        val path = Path()
                        path.apply {
                            moveTo(100f, 1000f)
                            lineTo(100f, 10f)
                        }
                        val gestureDescription =
                            GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 100, 100))
                                .build()
                        service.dispatchGesture(gestureDescription, null, null)
                    }
                }
            }
        }
//        accessibilityNodeInfo ?: return
//        function?.invoke(service, event)
//        LayoutInspector.printPacketInfo(service.rootInActiveWindow)
//        if (event.className == "android.support.v7.widget.RecyclerView") {
//        }
//            rootInActiveWindow.getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
//        if (accessibilityNodeInfo != null) {
//            printPacketInfo(accessibilityNodeInfo)
//        }

    }
}