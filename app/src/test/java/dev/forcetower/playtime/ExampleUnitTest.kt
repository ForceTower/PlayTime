package dev.forcetower.playtime

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    @Test
    fun `is ok`() {
        println(context)
        println(context.packageName)
    }
}