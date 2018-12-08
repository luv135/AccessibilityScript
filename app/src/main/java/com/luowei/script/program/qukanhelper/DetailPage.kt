package com.luowei.script.program.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import com.luowei.script.accessibility.AccessibilityServiceUtils
import com.luowei.script.program.IPage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class DetailPage(val service: AccessibilityService) : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qukan.newsdetail.news.NewsDetailActivity"
    }

    override fun enter() {
        logDebug("detail enter")
        startScrollDown()
    }

    private var disposable: Disposable? = null

    private fun startScrollDown() {
        disposable = Observable.interval(2, TimeUnit.SECONDS)
            .map {
                if (Random().nextInt(10) < 8)
                    AccessibilityServiceUtils.scrollVertical(service, down = true)
                else
                    AccessibilityServiceUtils.scrollVertical(service, down = false)
                it
            }.filter { it > 10 + Random().nextInt(2) }.subscribe {
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            }
    }

    private fun stopScrollDown() {
        disposable?.dispose()
    }

    override fun leave() {
        logDebug("detail leave")
        stopScrollDown()

    }

    override fun handleAccessibilityEvent(event: AccessibilityEvent) {
    }
}