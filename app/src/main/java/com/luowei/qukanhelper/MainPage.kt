package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Message
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.luowei.accessibility.AccessibilityServiceUtils
import com.luowei.accessibility.LayoutInspector
import com.luowei.logwherelibrary.logDebug
import java.util.concurrent.TimeUnit

class MainPage : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qkbase.main.MainActivity"

    }

    private val blackList = arrayListOf<String>()
    private var handler: ScrollHandler? = null

    class ScrollHandler(val service: AccessibilityService) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                EVENT_DETAIL_SCROLL_WAIT_TIMEOUT -> {
                    logDebug("main page, scroll down")
                    AccessibilityServiceUtils.scrollDown(service)
                    sendEmptyMessageDelayed(EVENT_DETAIL_SCROLL_WAIT_TIMEOUT, TimeUnit.SECONDS.toMillis(1))
                }
            }
        }
    }

    private var service: AccessibilityService? = null

    override fun enter(service: AccessibilityService) {
        this.service = service
        handler = ScrollHandler(service)
        handler?.sendEmptyMessageDelayed(EVENT_DETAIL_SCROLL_WAIT_TIMEOUT, TimeUnit.SECONDS.toMillis(2))
    }

    override fun leave(service: AccessibilityService) {
        this.service = null
        handler?.removeMessages(EVENT_DETAIL_SCROLL_WAIT_TIMEOUT)

    }

    companion object {
        const val EVENT_DETAIL_SCROLL_WAIT_TIMEOUT = 1
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

    override fun handleAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        if (requestPermission(service, event)) return true
        if (!jumpToDetail(service, event)) {
            logDebug("not found jump to detailn")
//            AccessibilityServiceUtils.scrollDown(service)
        }
        return true
    }

    private fun jumpToDetail(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        val accessibilityNodeInfo = service.rootInActiveWindow
        accessibilityNodeInfo ?: return false
        val recyclerView =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/nt").getOrNull(0)
        recyclerView ?: return false
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChild(i) ?: continue
            val title = child.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/z0").getOrNull(0)?.text?.toString()
            if (title != null && !blackList.contains(title) && child.findAccessibilityNodeInfosByText("广告").isEmpty() &&
                child.findAccessibilityNodeInfosByText("置顶").isEmpty()
            ) {
                LayoutInspector.printPacketInfo(child)
//                clickChild = child
                blackList.add(title)
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            }
        }
        return false
    }

}