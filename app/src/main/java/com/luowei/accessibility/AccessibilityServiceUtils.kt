package com.luowei.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.Point
import android.provider.Settings
import android.text.TextUtils
import android.view.WindowManager


/**
 * Created by Stardust on 2017/1/26.
 */

object AccessibilityServiceUtils {

    fun goToAccessibilitySetting(context: Context) {
        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun isAccessibilityServiceEnabled(
        context: Context,
        accessibilityService: Class<out AccessibilityService>
    ): Boolean {
        val expectedComponentName = ComponentName(context, accessibilityService)

        val enabledServicesSetting =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledService = ComponentName.unflattenFromString(componentNameString)

            if (enabledService != null && enabledService == expectedComponentName)
                return true
        }

        return false
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
//        var retry = 3
//        while (retry-- > 0 &&
//            !service.dispatchGesture(gestureDescription, callback, null)
//        ) {
//            logDebug("dispatchGesture failure sleep 1 seconds")
//            Thread.sleep(TimeUnit.SECONDS.toMillis(1))
//        }
    }

    fun scrollUp(service: AccessibilityService) {
        val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val length = 50
        val x1 = size.x / 2f
        val x2 = size.x / 2f
        val y1 = size.y / 2f - length
        val y2 = size.y / 2f + length
        val path = Path()
        path.apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
        }
        val gestureDescription =
            GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 100))
                .build()
        service.dispatchGesture(gestureDescription, null, null)
    }


}
