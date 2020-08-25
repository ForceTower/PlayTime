package dev.forcetower.playtime.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class ScrollingAlphaBehavior @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attributeSet) {
    private val height = context.resources.displayMetrics.heightPixels
    private val triggerSize = height / 4
    private val range = height / 3

    private var scrollAccumulator = 0

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        scrollAccumulator += dyConsumed
        if (scrollAccumulator >= triggerSize) {
            val value = (scrollAccumulator - triggerSize).coerceAtMost(range)

            val percent = ((value * 0.7f) / range).coerceIn(0.0f..0.7f)

            child.alpha = percent
        } else if (scrollAccumulator < triggerSize) {
            child.alpha = 0f
        }

        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed,
            dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed
        )
    }
}