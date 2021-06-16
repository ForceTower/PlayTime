package dev.forcetower.playtime.widget.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat

class ScrollingAlphaBehavior @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attributeSet) {
    private val height = context.resources.displayMetrics.heightPixels
    private val triggerSize = height / 4
    private val range = height / 3

    private var scrollAccumulator = 0
    private var accumulatedAlphaPercent = 0f

    private var child: View? = null
    private var animator: ValueAnimator? = null

    private var lockedAlpha = false

    fun dimBackground(nextAlpha: Float = 0.7f) {
        lockedAlpha = true

        animator?.cancel()
        animator = ValueAnimator.ofFloat(child?.alpha ?: 0f, nextAlpha)
        animator?.duration = 250L
        animator?.addUpdateListener {
            child?.alpha = it.animatedValue as Float
        }
        animator?.start()
    }

    fun returnBackground() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(child?.alpha ?: 0f, accumulatedAlphaPercent)
        animator?.duration = 250L
        animator?.addUpdateListener {
            child?.alpha = it.animatedValue as Float
        }
        animator?.doOnEnd { lockedAlpha = false }
        animator?.start()
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        if (this.child == null) this.child = child
        return super.onLayoutChild(parent, child, layoutDirection)
    }

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
            accumulatedAlphaPercent = percent

            if (!lockedAlpha) child.alpha = percent
        } else if (scrollAccumulator < triggerSize) {
            accumulatedAlphaPercent = 0f
            if (!lockedAlpha) child.alpha = 0f
        }

        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed,
            dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed
        )
    }
}
