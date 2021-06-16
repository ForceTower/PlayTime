package dev.forcetower.playtime.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import dev.forcetower.playtime.R

class PushUpBehavior @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attributeSet) {
    private lateinit var target: View
    private var targetPosition = -1
    private var scrollAccumulator = 0
    private var originalBottom = -1

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        originalBottom = child.bottom
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency.id == R.id.content
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (!::target.isInitialized) {
            target = dependency.findViewById(R.id.details_anim_group)
            targetPosition = target.top
        }
        return super.onDependentViewChanged(parent, child, dependency)
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
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (targetPosition < 0 || originalBottom < 0) return

        val originalTargetPosition = targetPosition - originalBottom
        scrollAccumulator += dyConsumed

        val difference = scrollAccumulator - originalTargetPosition

        // view collided, they are pass the point
        if (difference >= 0) {
            child.translationY = difference.toFloat() * -1
        } else {
            child.translationY = 0f
        }
    }
}
