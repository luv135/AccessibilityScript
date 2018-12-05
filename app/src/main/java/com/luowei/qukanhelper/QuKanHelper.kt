package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.luowei.logwherelibrary.logDebug
import java.util.*

class QuKanHelper {
    private val windowMap = HashMap<String, (service: AccessibilityService, event: AccessibilityEvent) -> Unit>()
    private var currentPage: IPage? = null
    private val pages = arrayListOf(MainPage(), DetailPage())
    private val errorPage = ErrorPage()

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

    fun onAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent) {
        if (event.packageName != "com.jifen.qukan") return
        val eventType = event.eventType
        val accessibilityNodeInfo = service.rootInActiveWindow
        val className = event.className
        logDebug(
            " eventType: " + AccessibilityEvent.eventTypeToString(eventType) +
                    " getText :" + event.text.toString()
                    + "  getClassName:" + className + "  accessibilityNodeInfo:$accessibilityNodeInfo"
        )

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                windowChange(service, event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                currentPage?.handleAccessibilityEvent(service, event)
            }
        }
//        AccessibilityServiceUtils.scrollDown(service)

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

    private var currentWindow: String = ""

    private fun windowChange(
        service: AccessibilityService,
        event: AccessibilityEvent
    ) {
        val className = event.className.toString()
        val page = pages.find { it.matchPage(className) } ?: errorPage
        if (currentPage != page) {
            currentPage?.leave(service)
            page?.enter(service)
            currentPage = page
        }
        currentPage?.handleAccessibilityEvent(service, event)
    }
}