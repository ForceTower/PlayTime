package dev.forcetower.playtime.widget.view.notificationbutton

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.google.android.material.button.MaterialButton
import dev.forcetower.playtime.R

class NotificationButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {
    private var _checked = false
        set(value) {
            if (field != value) {
                field = value
                currentDrawable = R.drawable.asld_star_event
                refreshDrawableState()
            }
        }

    @DrawableRes
    private var currentDrawable = 0
        set(value) {
            if (field != value) {
                field = value
                setIconResource(value)
            }
        }

    override fun isChecked() = _checked

    override fun setChecked(checked: Boolean) {
        _checked = checked
    }

    override fun toggle() {
        _checked = !_checked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        val state = if (_checked) stateChecked else stateUnchecked
        mergeDrawableStates(drawableState, state)
        return drawableState
    }

    companion object {
        private val stateChecked = intArrayOf(android.R.attr.state_checked)
        private val stateUnchecked = intArrayOf(-android.R.attr.state_checked)
    }
}