package com.luowei.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import com.luowei.logwherelibrary.logDebug

object LayoutInspector {
    fun printPacketInfo(root: AccessibilityNodeInfo?) {
        root ?: return
        val toString = analysisPacketInfo(root, 0).insert(0, "---\n").toString()
        logDebug(toString)

    }

    private fun analysisPacketInfo(info: AccessibilityNodeInfo, tabcount: Int): StringBuilder {
        val sb = StringBuilder()
        if (tabcount > 0) {
            for (i in 0 until tabcount) {
                sb!!.append("\t")
            }
        }
        var name = info.className.toString()
        val split = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        name = split[split.size - 1]
        if ("TextView" == name) {
            val text = info.text
            sb!!.append("text:").append(text)
        } else if ("Button" == name) {
            val text = info.text
            sb!!.append("Button:").append(text)
        } else {
            sb!!.append(name)
        }
        sb!!.append(" id:${info.viewIdResourceName}").append("\n")

        val count = info.childCount
        if (count > 0) {
            for (i in 0 until count) {
                if (info.getChild(i) != null) {
                    sb.append(
                        analysisPacketInfo(
                            info.getChild(i),
                            tabcount + 1
                        )
                    )
                }
            }
        }
        return sb
    }
}