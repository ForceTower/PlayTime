package dev.forcetower.playtime.view

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import androidx.core.view.doOnLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.databinding.ActivityMainBinding
import dev.forcetower.toolkit.components.BaseActivity
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val uiViewModel by viewModels<UIViewModel>()

    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.root.doOnLayout {
            NavigationUI.setupWithNavController(binding.bottomNav, navController)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, binding.root)?.isAppearanceLightStatusBars = true

        setupObservers()
//        setupProfileOnBottomNav()
    }

    private fun setupObservers() {
        uiViewModel.onHideBottomNav.observe(
            this,
            EventObserver {
                val params = binding.bottomNav.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior as HideBottomViewOnScrollBehavior

                if (it) {
                    binding.bottomNav.doOnLayout { view -> behavior.slideUp(view) }
                } else {
                    binding.bottomNav.doOnLayout { view -> behavior.slideDown(view) }
                }
            }
        )
    }

    private fun setupProfileOnBottomNav() {
        val menuView = binding.bottomNav[0] as BottomNavigationMenuView
        val itemView = menuView[2] as BottomNavigationItemView
        val icon = itemView.findViewById<ImageView>(R.id.icon)

        Glide.with(this)
            .load("https://avatars1.githubusercontent.com/u/9421614?s=460&u=499efd7b66284bd4436bc74dd982c52f9e076740&v=4")
            .circleCrop()
            .into(icon)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun showSnack(string: String, duration: Int) {
        getSnackInstance(string, duration).show()
    }

    override fun getSnackInstance(string: String, duration: Int): Snackbar {
        return Snackbar.make(binding.root, string, duration).apply { anchorView = binding.bottomNav }
    }
}
