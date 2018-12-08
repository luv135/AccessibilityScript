package com.luowei.script.program.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import com.luowei.script.program.IPage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class QuKanHelper(private val service: AccessibilityService) {
    private val windowMap = HashMap<String, (service: AccessibilityService, event: AccessibilityEvent) -> Unit>()
    private var currentPage: IPage? = null
    private val pages = arrayListOf(MainPage(service), DetailPage(service), DialogPage(service))
    private val errorPage = ErrorPage(service)

    private var subscribe: Disposable? = null


    fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName != "com.jifen.qukan") {
            logDebug("package = ${event.packageName}")
            startApp()
            return
        } else {
            cancelStartApp()
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                windowChange(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                currentPage?.handleAccessibilityEvent(event)
            }
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }
        }
//        AccessibilityServiceUtils.scrollVertical(service)

//        accessibilityNodeInfo ?: return
//        function?.invoke(service, event)
//        LayoutInspector.packagetLayoutInfo(service.rootInActiveWindow)
//        if (event.className == "android.support.v7.widget.RecyclerView") {
//        }
//            rootInActiveWindow.getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
//        if (accessibilityNodeInfo != null) {
//            packagetLayoutInfo(accessibilityNodeInfo)
//        }

    }

    fun startApp() {
        if (subscribe?.isDisposed != true)
            subscribe = Observable.timer(5, TimeUnit.SECONDS)
                .subscribe {
                    val launchIntentForPackage = service.packageManager.getLaunchIntentForPackage("com.jifen.qukan")
                        .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                    service.startActivity(launchIntentForPackage)
                }
    }

    private fun cancelStartApp() {
        subscribe?.dispose()
        subscribe = null
    }

    private var currentWindow: String = ""

    private fun windowChange(
        event: AccessibilityEvent
    ) {
        val className = event.className.toString()
        val page = pages.find { it.matchPage(className) } ?: errorPage
        currentPage?.leave()
        page.enter()
        currentPage = page
        currentPage?.handleAccessibilityEvent(event)
    }
}