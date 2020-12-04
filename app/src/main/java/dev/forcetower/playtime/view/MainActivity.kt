package dev.forcetower.playtime.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import androidx.core.view.doOnLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.databinding.ActivityMainBinding
import dev.forcetower.playtime.databinding.MenuBottomProfileBinding
import dev.forcetower.toolkit.components.BaseActivity
import dev.forcetower.toolkit.extensions.getPixelsFromDp
import dev.forcetower.toolkit.extensions.inflate


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        val menuView = binding.bottomNav[0] as BottomNavigationMenuView
        val itemView = menuView[2] as BottomNavigationItemView
        val profileBind = binding.bottomNav.inflate<MenuBottomProfileBinding>(R.layout.menu_bottom_profile)

        binding.bottomNav.doOnLayout {
            ((binding.bottomNav.layoutParams as CoordinatorLayout.LayoutParams).behavior as HideBottomViewOnScrollBehavior).slideDown(binding.bottomNav)
        }

        itemView.addView(profileBind.root, FrameLayout.LayoutParams(getPixelsFromDp(36).toInt(), getPixelsFromDp(36).toInt(), Gravity.CENTER))

        Glide.with(this)
            .load("https://avatars1.githubusercontent.com/u/9421614?s=460&u=499efd7b66284bd4436bc74dd982c52f9e076740&v=4")
            .circleCrop()
            .into(profileBind.image)

    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun showSnack(string: String, duration: Int) {
        getSnackInstance(string, duration).show()
    }

    override fun getSnackInstance(string: String, duration: Int): Snackbar {
        return Snackbar.make(binding.root, string, duration).apply { anchorView = binding.bottomNav }
    }
}