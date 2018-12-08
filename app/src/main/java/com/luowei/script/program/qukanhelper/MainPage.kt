package com.luowei.script.program.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.luowei.logwherelibrary.logDebug
import com.luowei.script.accessibility.AccessibilityServiceUtils
import com.luowei.script.program.IPage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainPage(private val service: AccessibilityService) : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qkbase.main.MainActivity"
    }

    private val blackList = arrayListOf<String>()

    private var disposable: Disposable? = null

    override fun enter() {
        startScrollDown()
    }


    override fun leave() {
        stopScrollDown()
    }

    private fun startScrollDown() {
        disposable = Observable.interval(2, TimeUnit.SECONDS)
            .map { AccessibilityServiceUtils.scrollVertical(service, down = true);it }
            .filter { it == 2L }
            .map { AccessibilityServiceUtils.click(service) }
            .subscribe()
    }

    private fun stopScrollDown() {
        disposable?.dispose()
    }

    private fun linquClick(accessibilityNodeInfo: AccessibilityNodeInfo) {
        textClick("领取")
    }

    private fun shuaxinClick(accessibilityNodeInfo: AccessibilityNodeInfo) {
        textClick("刷新")
    }

    private fun toutiaoTableCLick() {
        textClick("头条")
    }

    private fun textClick(text: String) {
        service.rootInActiveWindow?.findAccessibilityNodeInfosByText(text)?.getOrNull(0)
            ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    override fun handleAccessibilityEvent(event: AccessibilityEvent) {
        service.rootInActiveWindow?.let {
            linquClick(it)
            if (blackList.size > 5)
                shuaxinClick(it)

        }
        jumpToDetail(service, event)
//        service.rootInActiveWindow?.findAccessibilityNodeInfosByText("领取")?.getOrNull(0)
//            ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

    }

    private fun jumpToDetail(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        val accessibilityNodeInfo = service.rootInActiveWindow
        if (accessibilityNodeInfo == null) {
            logDebug("accessibilityNodeInfo is null")
            return false
        }
//        LayoutInspector.packagetLayoutInfo(accessibilityNodeInfo)

        val recyclerView =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/nt").getOrNull(0)
        if (recyclerView == null) {
            logDebug("recyclerView is null")
            toutiaoTableCLick()
            return false
        }
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChild(i) ?: continue
            val title = child.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/z0").getOrNull(0)?.text?.toString()
            if (title != null && !blackList.contains(title) && child.findAccessibilityNodeInfosByText("广告").isEmpty() &&
                child.findAccessibilityNodeInfosByText("置顶").isEmpty()
            ) {
//                LayoutInspector.packagetLayoutInfo(child)
                blackList.add(title)
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            }
        }
        logDebug("not found jump to detailn")
        return false
    }

}