package dev.forcetower.playtime.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.playtime.R
import dev.forcetower.playtime.databinding.ActivityMainBinding
import dev.forcetower.toolkit.components.BaseActivity
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = rootView.background
        binding.blurView
            .setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(6f)
            .setHasFixedTransformationMatrix(true)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun showSnack(string: String, duration: Int) {
        getSnackInstance(string, duration)?.show()
    }

    override fun getSnackInstance(string: String, duration: Int): Snackbar? {
        return Snackbar.make(binding.root, string, duration)
    }
}