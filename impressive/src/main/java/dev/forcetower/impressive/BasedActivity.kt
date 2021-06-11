package dev.forcetower.impressive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.core.BasedObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BasedActivity : AppCompatActivity() {
    @Inject lateinit var data: DataInfo
    @Inject lateinit var base: BasedObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("Based AF ${data.something} ${base.value}")
    }
}