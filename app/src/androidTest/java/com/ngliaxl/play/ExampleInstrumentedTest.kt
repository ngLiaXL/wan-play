package com.ngliaxl.play

import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ldroid.kwei.UseCase
import com.ldroid.kwei.UseCaseHandler
import com.ldroid.kwei.exception.ResponeThrowable
import com.ldroid.kwei.retrofit.BaseUrlProvider
import com.ldroid.kwei.retrofit.OkHttpClientProvider
import com.ngliaxl.play.network.DefaultOkHttpClientFactory
import com.ngliaxl.play.network.PlayUrlFactory
import com.ngliaxl.play.network.usecase.ArticlesUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.ngliaxl.play", appContext.packageName)
    }
}
