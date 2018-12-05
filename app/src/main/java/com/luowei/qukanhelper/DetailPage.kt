package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Message
import android.view.accessibility.AccessibilityEvent
import com.luowei.accessibility.AccessibilityServiceUtils
import com.luowei.logwherelibrary.logDebug
import java.util.*
import java.util.concurrent.TimeUnit

class DetailPage : IPage {
    override fun matchPage(className: String): Boolean {
        return className == "com.jifen.qukan.newsdetail.news.NewsDetailActivity"
    }

    private var handler: ScrollHandler? = null

    class ScrollHandler(val service: AccessibilityService) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                EVENT_SCROLL -> {
                    logDebug("DetailPage page, scroll down")
                    if (Random().nextInt(10) < 8)
                        AccessibilityServiceUtils.scrollDown(service)
                    else
                        AccessibilityServiceUtils.scrollUp(service)
                    sendEmptyMessageDelayed(EVENT_SCROLL, TimeUnit.SECONDS.toMillis(1))
                }
                EVENT_BACK -> {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                }
            }
        }
    }

    private var service: AccessibilityService? = null

    override fun enter(service: AccessibilityService) {
        this.service = service
        handler = ScrollHandler(service)
        logDebug("detail enter")
        handler?.sendEmptyMessageDelayed(EVENT_SCROLL, TimeUnit.SECONDS.toMillis(1))
        handler?.sendEmptyMessageDelayed(EVENT_BACK, TimeUnit.SECONDS.toMillis(7))
    }

    override fun leave(service: AccessibilityService) {
        this.service = null
        logDebug("detail leave")
        handler?.removeMessages(EVENT_SCROLL)
        handler?.removeMessages(EVENT_BACK)

    }

    companion object {
        const val EVENT_SCROLL = 1
        const val EVENT_BACK = 3
    }

    override fun handleAccessibilityEvent(service: AccessibilityService, event: AccessibilityEvent): Boolean {
        return true
    }
}